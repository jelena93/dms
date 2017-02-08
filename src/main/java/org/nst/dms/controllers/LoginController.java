/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.nst.dms.dto.UserDto;
import org.nst.dms.domain.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Jelena
 */
@Controller
public class LoginController {

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String homePage(Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        userDto.setBreadcrumbs(Arrays.asList("Dashboard"));
        switch (userDto.getActiveRole()) {
            case ADMIN:
                return "admin_home";
            case USER:
                return "user_home";
            case UPLOADER:
                return "uploader_home";
            default:
                return "login";
        }
    }

    @RequestMapping(value = {"/role/{role}"}, method = RequestMethod.GET)
    public String changeUserRole(Authentication authentication, @PathVariable("role") String role) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        if (role.equals(Role.ADMIN.name())) {
            userDto.setActiveRole(Role.ADMIN);
        }
        if (role.equals(Role.USER.name())) {
            userDto.setActiveRole(Role.USER);
        }
        if (role.equals(Role.UPLOADER.name())) {
            userDto.setActiveRole(Role.UPLOADER);
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }

}
