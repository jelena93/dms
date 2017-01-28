/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import org.nst.dms.controllers.exceptions.CustomException;
import org.nst.dms.domain.Action;
import org.nst.dms.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.domain.Process;
import org.nst.dms.service.ActionService;

/**
 *
 * @author Hachiko
 */
@Controller
@RequestMapping("/actions")
public class ActionController {

    @Autowired
    private ActionService actionService;
    @Autowired
    private ProcessService processService;

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public ModelAndView addAction() { return new ModelAndView("add_action"); }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ModelAndView save(String name, Long parent) {
        if (parent == null) {
            throw new CustomException("Action has to have a parent", "500");
        }
        Process processParent = processService.find(parent);
        Action action = new Action(name, processParent);
        actionService.save(action);
        return new ModelAndView("add_action", "success_message", "Action successfully added");
    }
}
