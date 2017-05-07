package com.hiczp.web.speciality.controller.admin;

import com.hiczp.web.speciality.entity.SortEntity;
import com.hiczp.web.speciality.exception.BadRequestException;
import com.hiczp.web.speciality.exception.SortNotFoundException;
import com.hiczp.web.speciality.model.SortFormModel;
import com.hiczp.web.speciality.repository.SortRepository;
import com.hiczp.web.speciality.service.SortService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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
    public ModelAndView sort(ModelAndView modelAndView) {
        modelAndView.setViewName("/admin/sort_list");
        return modelAndView.addObject("sortEntities", sortService.getTreeListText());
    }

    @PostMapping("/sort")
    public ModelAndView sort(ModelAndView modelAndView, Integer[] ids, Integer[] taxis) {
        if (ids.length != taxis.length) {
            throw new BadRequestException("The length of ids[] and taxis[] does not match");
        }
        sortService.saveTaxis(ids, taxis);

        modelAndView.setView(new RedirectView("/admin/sort", true, true, false));
        return modelAndView;
    }

    @GetMapping("/sort/{id}")
    public ModelAndView sort(ModelAndView modelAndView, SortFormModel sortFormModel, @PathVariable Integer id) {
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

        modelAndView.setViewName("/admin/sort");
        return modelAndView.addObject("sortFormModel", sortFormModel)
                .addObject("parentSorts", sortService.getTreeListText());
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

        modelAndView.setViewName("/admin/sort");
        return modelAndView.addObject("sortFormModel", sortFormModel)
                .addObject("parentSorts", sortService.getTreeListText())
                .addObject("message", message);
    }

    @GetMapping("/new_sort")
    public ModelAndView newSort(ModelAndView modelAndView, SortFormModel sortFormModel) {
        modelAndView.setViewName("/admin/sort");
        return modelAndView.addObject("sortFormModel", sortFormModel)
                .addObject("parentSorts", sortService.getTreeListText());
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

        modelAndView.setViewName("/admin/sort");
        return modelAndView.addObject("sortFormModel", sortFormModel)
                .addObject("parentSorts", sortService.getTreeListText())
                .addObject("message", message);
    }
}
