/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import java.util.Arrays;
import static org.nst.dms.domain.Role.ADMIN;
import static org.nst.dms.domain.Role.UPLOADER;
import static org.nst.dms.domain.Role.USER;
import org.nst.dms.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Jelena
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @RequestMapping(method = RequestMethod.GET)
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
}
