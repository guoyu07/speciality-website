package com.hiczp.web.speciality.configuration;

import com.hiczp.web.speciality.entity.SortEntity;
import com.hiczp.web.speciality.repository.ConfigRepository;
import com.hiczp.web.speciality.service.SortService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by czp on 17-2-14.
 */
@Aspect
@Configuration
public class AspectConfiguration {
    private SortService sortService;
    private ConfigRepository configRepository;

    public AspectConfiguration(SortService sortService, ConfigRepository configRepository) {
        this.sortService = sortService;
        this.configRepository = configRepository;
    }

    //切入每个普通页面
    @Pointcut("execution(org.springframework.web.servlet.ModelAndView com.hiczp.web.speciality.controller.*.*(..))")
    private void normalController() {
    }

    @AfterReturning(pointcut = "normalController()", returning = "result")
    public void afterNormalControllerReturning(Object result) {
        ModelAndView modelAndView = (ModelAndView) result;
        //附加设置信息到返回值
        modelAndView.addObject("specialityName", configRepository.findByKey("speciality-name").getValue())
                .addObject("universityName", configRepository.findByKey("university-name").getValue())
                .addObject("copyright", configRepository.findByKey("copyright").getValue())
                .addObject("icp", configRepository.findByKey("icp").getValue());

        //附加导航栏分类信息到返回值
        List<SortEntity> rootSorts = sortService.getRootSorts();
        List<List<SortEntity>> allChildSorts = new ArrayList<>(rootSorts.size());
        rootSorts.forEach((rootSort) -> allChildSorts.add(sortService.getChildSorts(rootSort)));
        modelAndView.addObject("rootSorts", rootSorts)
                .addObject("childSorts", allChildSorts);
    }
}
