package com.hiczp.web.speciality.controller.admin;

import com.hiczp.web.speciality.entity.UserEntity;
import com.hiczp.web.speciality.model.UserProfileFormModel;
import com.hiczp.web.speciality.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by czp on 17-5-12.
 */
@Controller
@RequestMapping("/admin/profile")
public class AdminProfileController {
    private UserRepository userRepository;

    public AdminProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ModelAndView profileGet(ModelAndView modelAndView, UserProfileFormModel userProfileFormModel) {
        UserEntity userEntity = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        userProfileFormModel.setEmail(userEntity.getEmail());
        userProfileFormModel.setNick(userEntity.getNick());
        return modelAndView.addObject("userProfileFormModel", userProfileFormModel);
    }

    @PostMapping
    public ModelAndView profilePost(ModelAndView modelAndView, @Valid UserProfileFormModel userProfileFormModel, BindingResult bindingResult) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String message = "";
        if (!bindingResult.hasErrors()) {
            UserEntity userEntity = userRepository.findByEmail(email);
            String password = userProfileFormModel.getPassword();
            if (password != null && !password.equals("")) {
                userEntity.setPassword(password);
            }
            userEntity.setNick(userProfileFormModel.getNick());
            userRepository.save(userEntity);
        } else {
            message = "表单错误";
        }
        userProfileFormModel.setEmail(email);
        userProfileFormModel.setPassword(null);
        return modelAndView.addObject("userProfileFormModel", userProfileFormModel)
                .addObject("message", message);
    }
}
