package com.hiczp.web.speciality.service;

import com.hiczp.web.speciality.entity.UserEntity;
import com.hiczp.web.speciality.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Created by czp on 17-2-27.
 */
@Service
public class DefaultUserDetailService implements UserDetailsService {
    private UserRepository userRepository;

    public DefaultUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(String.format("Username '%s' not found", username));
        }
        return new User(username, userEntity.getPassword(), Arrays.asList(() -> "ROLE_BASIC"));
    }
}
