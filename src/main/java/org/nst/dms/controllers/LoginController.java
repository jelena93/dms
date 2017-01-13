/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import org.nst.dms.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.service.UserService;

/**
 *
 * @author Jelena
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/add_document", method = RequestMethod.GET)
    public ModelAndView login() {
        return new ModelAndView("add_document");
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ModelAndView login(String username, String password) {
        User user = userService.login(username, password);
        return new ModelAndView("menu", "user", user);
    }
}
