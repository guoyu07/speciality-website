package com.hiczp.web.speciality.controller.admin;

import com.hiczp.web.speciality.entity.SortEntity;
import com.hiczp.web.speciality.exception.SortNotFoundException;
import com.hiczp.web.speciality.model.SortFormModel;
import com.hiczp.web.speciality.repository.SortRepository;
import com.hiczp.web.speciality.service.SortService;
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
 * Created by czp on 17-3-11.
 */
@Controller
@RequestMapping("/admin")
public class AdminSortController {
    private SortService sortService;
    private SortRepository sortRepository;

    public AdminSortController(SortService sortService, SortRepository sortRepository) {
        this.sortService = sortService;
        this.sortRepository = sortRepository;
    }

    @GetMapping("/sort")
    public ModelAndView sort(ModelAndView modelAndView, Pageable pageable) {
        modelAndView.setViewName("/admin/sort_list");
        return modelAndView;
    }

    @GetMapping("/sort/{id}")
    public ModelAndView sort(ModelAndView modelAndView, SortFormModel sortFormModel, @PathVariable Integer id) {
        SortEntity sortEntity = sortRepository.findOne(id);
        if (sortEntity != null) {
            sortFormModel.setName(sortEntity.getName());
            sortFormModel.setParent(sortEntity.getParent());
            sortFormModel.setSortType(sortEntity.getType());
            sortFormModel.setTaxis(sortEntity.getTaxis());
        } else {
            throw new SortNotFoundException();
        }
        sortFormModel.setParentSorts(sortService.getTreeListText());
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
        sortFormModel.setParentSorts(sortService.getTreeListText());
        modelAndView.setViewName("/admin/sort");
        return modelAndView.addObject("activeSidebarItem", "sort")
                .addObject("action", "/admin/sort/" + id)
                .addObject("sortFormModel", sortFormModel)
                .addObject("message", message);
    }

    @GetMapping("/new_sort")
    public ModelAndView newSort(ModelAndView modelAndView, SortFormModel sortFormModel) {
        sortFormModel.setParentSorts(sortService.getTreeListText());
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
        sortFormModel.setParentSorts(sortService.getTreeListText());
        modelAndView.setViewName("/admin/new_sort");
        return modelAndView.addObject("activeSidebarItem", "sort")
                .addObject("action", "/admin/new_sort")
                .addObject("sortFormModel", sortFormModel)
                .addObject("message", message);
    }
}
