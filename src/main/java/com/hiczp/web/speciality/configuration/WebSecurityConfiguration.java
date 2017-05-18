package com.hiczp.web.speciality.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by czp on 17-2-27.
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private UserDetailsService userDetailsService;

    public WebSecurityConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        //rest
        http.authorizeRequests().antMatchers("/api/admin/**").authenticated()
                .and()
                .httpBasic();

        //page
        http.authorizeRequests().antMatchers("/account/logout", "/admin/**").authenticated()
                .and()
                .formLogin().loginPage("/account/login").defaultSuccessUrl("/admin")
                .and().rememberMe()
                .and().logout().logoutUrl("/account/logout").logoutSuccessUrl("/");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}
