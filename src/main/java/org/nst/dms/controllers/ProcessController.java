/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import java.util.List;
import org.nst.dms.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.domain.Process;

/**
 *
 * @author Hachiko
 */
@Controller
public class ProcessController {

    @Autowired
    private ProcessService processService;

    //ovako? gde se definise? svi get process zahtevi ovde?
    @RequestMapping(path = "/add_process", method = RequestMethod.GET)
    public ModelAndView addProcess() {
        return new ModelAndView("add_process");
    }

    @RequestMapping(path = "/save_process", method = RequestMethod.POST)
    public ModelAndView save(Process process) {
//        @TODO obrada greske ako je process null
        Process p = processService.save(process);
        return new ModelAndView("menu", "p", p);
    }
    
    @RequestMapping(path = "/find_all_processes", method = RequestMethod.POST)
    public ModelAndView findAll() {
//        @TODO obrada greske ako je null
        List<Process> processes = processService.findAll();
        return new ModelAndView("menu", "processes", processes);
    }
    
    @RequestMapping(path = "/search_processes", method = RequestMethod.POST)
    public ModelAndView search(String name) {
//        @TODO obrada greske ako je null
        List<Process> processes = processService.search(name);
        return new ModelAndView("menu", "processes", processes);
    }
}
