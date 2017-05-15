package com.hiczp.web.speciality.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by czp on 17-5-15.
 */
@Controller
@RequestMapping("/admin/tool")
public class AdminToolController {
    @GetMapping("/actuator")
    public ModelAndView actuator(ModelAndView modelAndView) {
        return modelAndView;
    }
}
