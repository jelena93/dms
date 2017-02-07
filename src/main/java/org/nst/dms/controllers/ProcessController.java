/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import org.nst.dms.config.security.SecurityUser;
import org.nst.dms.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.domain.Process;
import org.nst.dms.domain.User;
import org.nst.dms.controllers.exceptions.CustomException;
import org.nst.dms.domain.Company;
import org.nst.dms.service.CompanyService;
import org.nst.dms.service.UserService;
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
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyService;

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public ModelAndView addProcess(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        securityUser.getBreadcrumbs().clear();
        securityUser.getBreadcrumbs().add("Processes");
        securityUser.getBreadcrumbs().add("Add process");
        ModelAndView mv = new ModelAndView("add_process");
        User user = userService.findOne(securityUser.getUsername());
        mv.addObject("company", user.getCompany());
        return mv;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ModelAndView save(Authentication authentication, String name, @RequestParam(name = "parent", required = false) Long parent,
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
        
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        Company company = userService.findOne(user.getUsername()).getCompany();
        company.getProcesses().add(process);
        companyService.save(company);
        return new ModelAndView("add_process", "success_message", "Process successfully added");
    }
}
