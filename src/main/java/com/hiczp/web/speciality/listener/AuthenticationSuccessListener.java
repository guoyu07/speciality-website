package com.hiczp.web.speciality.listener;

import com.hiczp.web.speciality.entity.LoginLogEntity;
import com.hiczp.web.speciality.repository.LoginLogRepository;
import com.hiczp.web.speciality.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Created by czp on 17-3-5.
 */
@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {
    private UserRepository userRepository;
    private LoginLogRepository loginLogRepository;

    public AuthenticationSuccessListener(UserRepository userRepository, LoginLogRepository loginLogRepository) {
        this.userRepository = userRepository;
        this.loginLogRepository = loginLogRepository;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent authenticationSuccessEvent) {
        Authentication authentication = authenticationSuccessEvent.getAuthentication();
        String email = authentication.getName();
        Integer userId = userRepository.findByEmail(email).getId();
        LoginLogEntity loginLogEntity = new LoginLogEntity();
        loginLogEntity.setUser(userId);
        loginLogEntity.setIp(((WebAuthenticationDetails) authentication.getDetails()).getRemoteAddress());
        loginLogEntity.setTime(new Timestamp(System.currentTimeMillis()));
        loginLogRepository.save(loginLogEntity);
    }
}
