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
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/add_user", method = RequestMethod.GET)
    public ModelAndView login() {
        return new ModelAndView("add_user");
    }

    @RequestMapping(path = "/save_user", method = RequestMethod.POST)
    public ModelAndView save(User user) {
        User u = userService.save(user);
        return new ModelAndView("menu", "u", u);
    }
}
