package com.hiczp.web.speciality;

import com.hiczp.web.speciality.entity.ConfigEntity;
import com.hiczp.web.speciality.entity.UserEntity;
import com.hiczp.web.speciality.repository.ConfigRepository;
import com.hiczp.web.speciality.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by czp on 17-5-19.
 */
@Component
public class ApplicationCommandLineRunner implements CommandLineRunner {
    private UserRepository userRepository;
    private ConfigRepository configRepository;

    public ApplicationCommandLineRunner(UserRepository userRepository, ConfigRepository configRepository) {
        this.userRepository = userRepository;
        this.configRepository = configRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        Scanner scanner = new Scanner(System.in);
        //1 号用户不存在
        if (userRepository.findOne(1) == null) {
            System.out.println("#1 用户不存在, 进入创建用户向导");

            System.out.println("请输入一个邮箱作为管理员用户名");
            String email = scanner.nextLine();
            System.out.println("请输入一个密码");
            String password = scanner.nextLine();
            System.out.println("请为自己取一个昵称");
            String nick = scanner.nextLine();

            UserEntity userEntity = new UserEntity(1, email, password, nick);
            userRepository.save(userEntity);
            System.out.println("#1 用户创建完毕");
            System.out.printf("用户名: %s, 密码: %s, 昵称: %s\n", email, password, nick);
        }

        //添加默认的设置项
        Map<String, String> configItems = new LinkedHashMap<>();
        configItems.put("universityName", "高校名称");
        configItems.put("specialityName", "院系专业名");
        configItems.put("icp", "备案号");
        configItems.put("copyright", "©2017 版权信息");
        configItems.put("indexSorts", "[]");
        configItems.put("navbarSorts", "[]");
        configItems.put("mainIndexSort", "0");
        configItems.put("carouselImages", "[]");
        List<String> configs = StreamSupport.stream(configRepository.findAll().spliterator(), true).map(ConfigEntity::getKey).collect(Collectors.toList());
        configItems.forEach((key, value) -> {
            if (!configs.contains(key)) {
                configRepository.save(new ConfigEntity(key, value));
            }
        });
    }
}
