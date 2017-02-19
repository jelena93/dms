/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers.rest;

import org.nst.dms.domain.Activity;
import org.nst.dms.dto.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.nst.dms.service.ActivityService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author Hachiko
 */
@RestController
@RequestMapping("/api/activities")
public class RestApiActivityController {

    @Autowired
    private ActivityService activityService;

    @RequestMapping(path = "/activity/{id}", method = RequestMethod.GET)
    public ResponseEntity<Activity> showActivity(@PathVariable("id") long id) throws Exception {
        Activity activity = activityService.find(id);
        if (activity == null) throw new Exception("There is no activity with id " + id); 
        return new ResponseEntity<>(activity, HttpStatus.OK);
    }

    @RequestMapping(path = "/edit", method = RequestMethod.POST)
    public ResponseEntity<String> editActivity(Long id, String name) {
        Activity activity = activityService.find(id);
        activity.setName(name);
        activityService.save(activity);
        return new ResponseEntity<>("Activity successfully edited", HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageDto> handleError(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return new ResponseEntity<>(new MessageDto(MessageDto.MESSAGE_TYPE_ERROR, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}
