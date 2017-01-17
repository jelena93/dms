/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import java.util.ArrayList;
import java.util.List;
import org.nst.dms.config.security.SecurityUser;
import org.nst.dms.domain.Company;
import org.nst.dms.domain.Role;
import org.nst.dms.domain.User;
import org.nst.dms.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.service.UserService;
import org.springframework.security.core.Authentication;

/**
 *
 * @author Jelena
 */
@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyService;

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public ModelAndView getAddUser(Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        user.getBreadcrumbs().clear();
        user.getBreadcrumbs().add("Users");
        user.getBreadcrumbs().add("Add user");
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        roles.add(Role.USER);
        roles.add(Role.UPLOADER);
        List<Company> companies = companyService.findAll();
        ModelAndView mv = new ModelAndView("add_user");
        mv.addObject("roles", roles);
        mv.addObject("companies", companies);
        return mv;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ModelAndView save(String username, String password, String name, String surname, int company, String[] roles) {
        Company c = companyService.findOne(company);
        List<Role> rolesArr = new ArrayList<>();
        for (String role : roles) {
            if (role.equals(Role.ADMIN.name())) {
                rolesArr.add(Role.ADMIN);
            }
            if (role.equals(Role.USER.name())) {
                rolesArr.add(Role.USER);
            }
            if (role.equals(Role.UPLOADER.name())) {
                rolesArr.add(Role.UPLOADER);
            }
        }
        User user = new User(name, surname, username, password, c, rolesArr);
        userService.save(user);
        return new ModelAndView("add_user", "success_message", "User successfully added");
    }
}
