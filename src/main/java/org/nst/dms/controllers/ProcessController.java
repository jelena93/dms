/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import java.util.Arrays;
import org.nst.dms.dto.MessageDto;
import org.nst.dms.dto.UserDto;
import org.nst.dms.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.domain.Process;
import org.nst.dms.controllers.exceptions.CustomException;
import org.nst.dms.domain.Activity;
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

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public ModelAndView addProcess(Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        userDto.setBreadcrumbs(Arrays.asList("Processes", "Add process"));
        ModelAndView mv = new ModelAndView("add_process");
        mv.addObject("company", userDto.getCompany());
        return mv;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ModelAndView save(Authentication authentication, String name, @RequestParam(name = "parent", required = false) Long parent,
            @RequestParam(name = "primitive", required = false) boolean primitive, boolean isActivity) {
        Process process = null;
        String successMessage = "Process successfully added";
        if (parent == null && isActivity) {
            throw new CustomException("Activity has to have a parent", "500");
        }
        if (isActivity) {
            process = processService.find(parent);
            if (!process.isPrimitive()) {
                throw new CustomException("Can't add activity to a non primitive process", "500");
            }
            Activity activity = new Activity(name);
            process.getActivityList().add(activity);
            successMessage = "Activity successfully added";
        } else {
            if (parent != null) {
                Process parentProcess = processService.find(parent);
                if (parentProcess.isPrimitive()) {
                    throw new CustomException("Can't add process to a primitive process", "500");
                }
                process = new Process(name, parentProcess, primitive);
            } else {
                process = new Process(name, null, primitive);
            }
        }
        processService.save(process);
        UserDto userDto = (UserDto) authentication.getPrincipal();
        userDto.setBreadcrumbs(Arrays.asList("Processes", "Add process"));
        return new ModelAndView("add_process", "message", new MessageDto(MessageDto.MESSAGE_TYPE_SUCCESS, successMessage));
    }

}
