/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.config.security;

import org.nst.dms.domain.Role;
import org.nst.elasticsearch.handlers.ElasticSearchSuccessLoginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 *
 * @author Jelena
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private ElasticSearchSuccessLoginHandler elasticSearchSuccessLoginHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter, CsrfFilter.class);

        http.authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/companies/**").hasAuthority(Role.ADMIN.name())
                .antMatchers("/users/**").hasAuthority(Role.ADMIN.name())
                .antMatchers("/processes/**").hasAuthority(Role.USER.name())
                .antMatchers("/documents/**").hasAuthority(Role.UPLOADER.name())
                .anyRequest().authenticated()
                .and().exceptionHandling().accessDeniedPage("/403")
                .and().formLogin().loginPage("/login").permitAll()
                .successHandler(elasticSearchSuccessLoginHandler)
                .and().logout().logoutUrl("/logout");

    }

}
