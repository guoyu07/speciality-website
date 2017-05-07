package com.hiczp.web.speciality.configuration;

import com.hiczp.web.speciality.entity.SortEntity;
import com.hiczp.web.speciality.repository.ConfigRepository;
import com.hiczp.web.speciality.service.SortService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Before(value = "normalController() && args(modelAndView,..)")
    public void beforeNormalController(ModelAndView modelAndView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Boolean isLogined = authentication instanceof UsernamePasswordAuthenticationToken || authentication instanceof RememberMeAuthenticationToken;
        modelAndView.addObject("isLogined", isLogined);
    }

    @AfterReturning(pointcut = "normalController()", returning = "modelAndView")
    public void afterNormalControllerReturning(ModelAndView modelAndView) {
        //如果是重定向视图则不添加信息
        if (modelAndView.getView() instanceof RedirectView) {
            modelAndView.getModel().remove("isLogined");
            return;
        }

        //附加设置信息到返回值
        configRepository.findByKeyIn(Arrays.asList("specialityName",
                "universityName",
                "copyright",
                "icp")).forEach(configEntity ->
                modelAndView.addObject(configEntity.getKey(), configEntity.getValue())
        );

        //附加导航栏分类信息到返回值
        List<SortEntity> rootSorts = sortService.getNavbarRootSorts();
        modelAndView.addObject("rootSorts", rootSorts)
                .addObject("childSorts", sortService.getAllChildSortsMap(rootSorts));
    }


    @AfterReturning(pointcut = "execution(org.springframework.web.servlet.ModelAndView com.hiczp.web.speciality.controller.admin.*.*(..))", returning = "modelAndView")
    public void afterAdminControllerReturning(JoinPoint joinPoint, ModelAndView modelAndView) {
        if (modelAndView.getView() instanceof RedirectView) {
            return;
        }

        //添加头像 URL
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        modelAndView.addObject("email", email)
                .addObject("avatarPath", "https://www.gravatar.com/avatar/" + DigestUtils.md5DigestAsHex(email.trim().toLowerCase().getBytes()));

        //添加 activeSidebarItem
        if (modelAndView.getModel().get("activeSidebarItem") == null) {
            Pattern pattern = Pattern.compile("Admin(.*)Controller");
            Matcher matcher = pattern.matcher(joinPoint.getTarget().getClass().getSimpleName());
            if (matcher.find()) {
                modelAndView.addObject("activeSidebarItem", matcher.group(1).toLowerCase());
            }
        }
    }
}
