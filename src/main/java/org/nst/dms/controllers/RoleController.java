/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import org.nst.dms.domain.Role;
import org.nst.dms.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Jelena
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    @RequestMapping(value = {"/{role}"}, method = RequestMethod.GET)
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
}
