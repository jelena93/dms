/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import java.util.ArrayList;
import java.util.List;
import org.nst.dms.domain.Company;
import org.nst.dms.domain.Role;
import org.nst.dms.domain.User;
import org.nst.dms.controllers.exceptions.CustomException;
import org.nst.dms.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.service.UserService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestParam;

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
    public ModelAndView getAddUser() {
        List<Company> companies = companyService.findAll();
        ModelAndView mv = new ModelAndView("add_user");
        mv.addObject("roles", getRoles());
        mv.addObject("companies", companies);
        return mv;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ModelAndView save(String username, String password, String name, String surname, @RequestParam(name = "company", required = false) Long company, String[] roles) {
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
        Company c;
        if (rolesArr.size() == 1 && rolesArr.contains(Role.ADMIN)) {
            c = null;
        } else if (company == null) {
            throw new CustomException("Can't find company with id " + company, "500");
        } else {
            c = companyService.findOne(company);
        }
        User user = new User(name, surname, username, password, c, rolesArr);
        userService.save(user);
        List<Company> companies = companyService.findAll();
        ModelAndView mv = new ModelAndView("add_user");
        mv.addObject("roles", getRoles());
        mv.addObject("companies", companies);
        mv.addObject("success_message", "User successfully added");
        return mv;
    }

    private List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        roles.add(Role.USER);
        roles.add(Role.UPLOADER);
        return roles;
    }
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
