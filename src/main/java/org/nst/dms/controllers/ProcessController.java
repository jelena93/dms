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
import org.springframework.security.core.Authentication;

/**
 *
 * @author Hachiko
 */
@Controller
@RequestMapping("/processes")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    //ovako? gde se definise? svi get process zahtevi ovde?
    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public String addProcess(Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        user.getBreadcrumbs().clear();
        user.getBreadcrumbs().add("Processes");
        user.getBreadcrumbs().add("Add process");
        return "add_process";
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public String save(String name, int processId, boolean primitive) {
        
//        Process p = processService.save(process);
        return "";
    }

    @RequestMapping(path = "/search", method = RequestMethod.POST)
    public ModelAndView findAll(Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        user.getBreadcrumbs().clear();
        user.getBreadcrumbs().add("Processes");
        user.getBreadcrumbs().add("Search process");
//        @TODO obrada greske ako je null
        List<Process> processes = processService.findAll();
        return new ModelAndView("search_processes", "processes", processes);
    }

//    @RequestMapping(path = "/search", method = RequestMethod.POST)
//    public ModelAndView search(String name) {
////        @TODO obrada greske ako je null
//        List<Process> processes = processService.search(name);
//        return new ModelAndView("search_processes", "processes", processes);
//    }
}
