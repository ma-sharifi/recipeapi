package com.example.recipea.config.security;

import com.example.recipea.security.JwtAuthorizationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Mahdi Sharifi
 */

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterAfter(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/v1/users/login").permitAll()
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest()
                .authenticated();
        ////Since the H2 database console runs inside a frame, we need to enable this in Spring Security.
        //for h2-console can be load. Frame options are needed to prevent a browser to load your HTML page in an <iframe> or a <frame> tag and for H2 Console page to load, you need to disable this option.
        http.headers().frameOptions().disable();
    }

    /**
     * Allow swagger load the data
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("mahdi")
                .password("sharifi")
                .authorities("ADMIN");
    }

}