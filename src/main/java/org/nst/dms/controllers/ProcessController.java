/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import java.util.List;
import org.nst.dms.config.security.SecurityUser;
import org.nst.dms.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.domain.Process;
import org.nst.dms.exceptions.CustomException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Hachiko
 */
@Controller
@RequestMapping("/processes")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public ModelAndView addProcess(Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        user.getBreadcrumbs().clear();
        user.getBreadcrumbs().add("Processes");
        user.getBreadcrumbs().add("Add process");
        return new ModelAndView("add_process","action_type_processes_search","add_parent");
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ModelAndView save(String name, @RequestParam(name = "parent", required = false) Long parent,
            @RequestParam(name = "primitive", required = false) boolean primitive) {
        Process process;
        if (parent != null) {
            Process parentProcess = processService.find(parent);
            if (parentProcess.isPrimitive()) {
                throw new CustomException("Can't add process to a primitive process", "500");
            }
            process = new Process(name, parentProcess, primitive);
        } else {
            process = new Process(name, null, primitive);
        }
        processService.save(process);
        return new ModelAndView("add_process", "success_message", "Process successfully added");
    }

    @RequestMapping(path = "/search", method = RequestMethod.POST)
    public ModelAndView findAll(Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        user.getBreadcrumbs().clear();
        user.getBreadcrumbs().add("Processes");
        user.getBreadcrumbs().add("Search process");
        List<Process> processes = processService.findAll();
        return new ModelAndView("search_processes", "processes", processes);
    }
}
