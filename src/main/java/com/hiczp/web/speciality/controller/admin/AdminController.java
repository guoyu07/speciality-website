package com.hiczp.web.speciality.controller.admin;

import com.hiczp.web.speciality.entity.SortEntity;
import com.hiczp.web.speciality.exception.SortNotFoundException;
import com.hiczp.web.speciality.model.SortFormModel;
import com.hiczp.web.speciality.repository.ArticleRepository;
import com.hiczp.web.speciality.repository.LoginLogRepository;
import com.hiczp.web.speciality.repository.SortRepository;
import com.hiczp.web.speciality.repository.UserRepository;
import org.apache.catalina.util.ServerInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootVersion;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by czp on 17-2-27.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    private SortRepository sortRepository;
    private UserRepository userRepository;
    private ArticleRepository articleRepository;
    private LoginLogRepository loginLogRepository;

    public AdminController(SortRepository sortRepository, UserRepository userRepository, ArticleRepository articleRepository, LoginLogRepository loginLogRepository) {
        this.sortRepository = sortRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.loginLogRepository = loginLogRepository;
    }

    @GetMapping("")
    public ModelAndView index(ModelAndView modelAndView, @Value("${project.version}") String projectVersion) {
        modelAndView.setViewName("/admin/index");
        return modelAndView.addObject("activeSidebarItem", "overview")
                .addObject("projectVersion", projectVersion)
                .addObject("springBootVersion", SpringBootVersion.getVersion())
                .addObject("ServerInfo", ServerInfo.class)
                .addObject("System", System.class)
                .addObject("recentlyArticles", articleRepository.findTop5ByAndPublishTrueOrderByCreateTimeDesc())
                .addObject("sortCount", sortRepository.count())
                .addObject("articleCount", articleRepository.count())
                .addObject("userCount", userRepository.count())
                .addObject("recentlyLoginLogs", loginLogRepository.findTop5ByOrderByTimeDesc());
    }

    @GetMapping("/sort")
    public ModelAndView sort(ModelAndView modelAndView, Pageable pageable) {
        modelAndView.setViewName("/admin/sort_list");
        return modelAndView;
    }

    @GetMapping("/sort/{id}")
    public ModelAndView sort(ModelAndView modelAndView, @PathVariable Integer id) {
        SortFormModel sortFormModel = new SortFormModel();
        SortEntity sortEntity = sortRepository.findOne(id);
        if (sortEntity != null) {
            sortFormModel.setId(sortEntity.getId());
            sortFormModel.setName(sortEntity.getName());
            sortFormModel.setParent(sortEntity.getParent());
            sortFormModel.setSortType(sortEntity.getType());
            sortFormModel.setTaxis(sortEntity.getTaxis());
        } else {
            throw new SortNotFoundException();
        }
        //TODO
        sortFormModel.setParentSorts(sortRepository.findAll());
        modelAndView.setViewName("/admin/sort");
        return modelAndView.addObject("activeSidebarItem", "sort")
                .addObject("action", "/admin/sort/" + id)
                .addObject("sortFormModel", sortFormModel);
    }

    @PostMapping("/sort/{id}")
    public ModelAndView sort(ModelAndView modelAndView, @PathVariable Integer id, @Valid SortFormModel sortFormModel, BindingResult bindingResult) {
        String message = "";
        if (!bindingResult.hasErrors()) {
            SortEntity sortEntity = sortRepository.findOne(id);
            if (sortEntity != null) {
                sortEntity.setName(sortFormModel.getName());
                sortEntity.setParent(sortFormModel.getParent());
                sortEntity.setType(sortFormModel.getSortType());
                sortEntity.setTaxis(sortFormModel.getTaxis());
                sortRepository.save(sortEntity);
            } else {
                throw new SortNotFoundException();
            }
        } else {
            message = "表单错误";
        }
        //TODO
        sortFormModel.setParentSorts(sortRepository.findAll());
        modelAndView.setViewName("/admin/sort");
        return modelAndView.addObject("activeSidebarItem", "sort")
                .addObject("action", "/admin/sort/" + id)
                .addObject("sortFormModel", sortFormModel)
                .addObject("message", message);
    }

    @GetMapping("/new_sort")
    public ModelAndView newSort(ModelAndView modelAndView, SortFormModel sortFormModel) {
        //TODO
        sortFormModel.setParentSorts(sortRepository.findAll());
        modelAndView.setViewName("/admin/new_sort");
        return modelAndView.addObject("activeSidebarItem", "sort")
                .addObject("action", "/admin/new_sort")
                .addObject("sortFormModel", sortFormModel);
    }

    @PostMapping("/new_sort")
    public ModelAndView newSort(ModelAndView modelAndView, @Valid SortFormModel sortFormModel, BindingResult bindingResult) {
        String message = "";
        if (!bindingResult.hasErrors()) {
            if (sortRepository.findByName(sortFormModel.getName()) == null) {
                SortEntity sortEntity = new SortEntity();
                sortEntity.setName(sortFormModel.getName());
                sortEntity.setParent(sortFormModel.getParent());
                sortEntity.setType(sortFormModel.getSortType());
                sortEntity.setTaxis(sortFormModel.getTaxis());
                sortRepository.save(sortEntity);
            } else {
                message = "分类已存在";
                bindingResult.addError(new FieldError(SortFormModel.class.getName(), "name", message));
            }
        } else {
            message = "表单错误";
        }
        //TODO
        sortFormModel.setParentSorts(sortRepository.findAll());
        modelAndView.setViewName("/admin/new_sort");
        return modelAndView.addObject("activeSidebarItem", "sort")
                .addObject("action", "/admin/new_sort")
                .addObject("sortFormModel", sortFormModel)
                .addObject("message", message);
    }

    @GetMapping("/login_log")
    public ModelAndView loginLog(ModelAndView modelAndView, Pageable pageable) {
        modelAndView.setViewName("/admin/login_log");
        return modelAndView.addObject("activeSidebarItem", "user")
                .addObject("recentlyLoginLogs", loginLogRepository.findByOrderByTimeDesc(pageable))
                .addObject("path", "/admin/login_log");
    }
}
