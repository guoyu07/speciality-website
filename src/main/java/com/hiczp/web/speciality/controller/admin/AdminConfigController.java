package com.hiczp.web.speciality.controller.admin;

import com.alibaba.fastjson.JSON;
import com.hiczp.web.speciality.entity.ConfigEntity;
import com.hiczp.web.speciality.model.ConfigFormModel;
import com.hiczp.web.speciality.repository.ConfigRepository;
import com.hiczp.web.speciality.service.SortService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by czp on 17-5-13.
 */
@Controller
@RequestMapping("/admin/config")
public class AdminConfigController {
    private SortService sortService;
    private ConfigRepository configRepository;

    public AdminConfigController(SortService sortService, ConfigRepository configRepository) {
        this.sortService = sortService;
        this.configRepository = configRepository;
    }

    @GetMapping
    public ModelAndView configGet(ModelAndView modelAndView, ConfigFormModel configFormModel) {
        Map<String, String> map = StreamSupport.stream(configRepository.findAll().spliterator(), true).collect(Collectors.toMap(ConfigEntity::getKey, ConfigEntity::getValue));
        configFormModel.setUniversityName(map.get("universityName"));
        configFormModel.setSpecialityName(map.get("specialityName"));
        configFormModel.setCopyright(map.get("copyright"));
        configFormModel.setIcp(map.get("icp"));
        configFormModel.setNavbarSorts(JSON.parseArray(map.get("navbarSorts"), Integer.class).toArray(new Integer[0]));
        configFormModel.setMainIndexSort(Integer.valueOf(map.get("mainIndexSort")));
        configFormModel.setIndexSorts(JSON.parseArray(map.get("indexSorts"), Integer.class).toArray(new Integer[0]));
        configFormModel.setCarouselImages(JSON.parseArray(map.get("carouselImages"), String.class).toArray(new String[0]));
        return modelAndView.addObject("configFormModel", configFormModel)
                .addObject("sortEntities", sortService.getTreeListText())
                .addObject("rootSorts", sortService.getRootSorts());
    }

    @PostMapping
    public ModelAndView configPost(ModelAndView modelAndView, @Valid ConfigFormModel configFormModel, BindingResult bindingResult) {
        String message = "";
        if (!bindingResult.hasErrors()) {
            Map<String, ConfigEntity> map = StreamSupport.stream(configRepository.findAll().spliterator(), true).collect(Collectors.toMap(ConfigEntity::getKey, Function.identity()));
            map.get("universityName").setValue(configFormModel.getUniversityName());
            map.get("specialityName").setValue(configFormModel.getSpecialityName());
            map.get("copyright").setValue(configFormModel.getCopyright());
            map.get("icp").setValue(configFormModel.getIcp());
            Integer[] navbarSort = configFormModel.getNavbarSorts();
            if (navbarSort == null) {
                navbarSort = new Integer[0];
            }
            map.get("navbarSorts").setValue(JSON.toJSONString(navbarSort));
            map.get("mainIndexSort").setValue(configFormModel.getMainIndexSort().toString());
            Integer[] indexSorts = configFormModel.getIndexSorts();
            if (indexSorts == null) {
                indexSorts = new Integer[0];
            }
            map.get("indexSorts").setValue(JSON.toJSONString(indexSorts));
            String[] carouselImages = configFormModel.getCarouselImages();
            if (carouselImages == null) {
                carouselImages = new String[0];
            }
            map.get("carouselImages").setValue(JSON.toJSONString(carouselImages));
            configRepository.save(map.entrySet().parallelStream().map(Map.Entry::getValue).collect(Collectors.toList()));
        } else {
            message = "表单错误";
        }
        return modelAndView.addObject("configFormModel", configFormModel)
                .addObject("sortEntities", sortService.getTreeListText())
                .addObject("rootSorts", sortService.getRootSorts())
                .addObject("message", message);
    }
}
