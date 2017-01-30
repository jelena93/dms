/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers.rest;

import java.util.ArrayList;
import org.nst.dms.service.CompanyService;
import java.util.List;
import org.nst.dms.controllers.exceptions.CustomException;
import org.nst.dms.domain.Activity;
import org.nst.dms.domain.Company;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.DocumentType;
import org.nst.dms.domain.Process;
import org.nst.dms.domain.dto.TreeDto;
import org.nst.dms.service.DocumentTypeService;
import org.nst.dms.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.service.ActivityService;

/**
 *
 * @author Hachiko
 */
@RestController
public class RestApiController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private DocumentTypeService documentTypeService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private ActivityService activityService;

    @RequestMapping(value = "/api/companies/search", method = RequestMethod.GET)
    public ResponseEntity<List<Company>> search(@Param("name") String name) {
        List<Company> companies;
        if (name == null || name.isEmpty()) {
            companies = companyService.findAll();
        } else {
            companies = companyService.search(name);
        }
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/document-type", method = RequestMethod.GET)
    public ResponseEntity<List<Descriptor>> getDocumentTypeDescriptors(Long id) {
        DocumentType documentType = documentTypeService.find(id);
        return new ResponseEntity<>(documentType.getDescriptors(), HttpStatus.OK);
    }

    @RequestMapping(value = "/api/processes", method = RequestMethod.GET)
    public ResponseEntity<List<TreeDto>> getProcesses() {
        List<Process> processes = processService.findAll();
        List<TreeDto> data = new ArrayList<>();
        for (Process process : processes) {
            TreeDto p;
            String icon;
            icon = TreeDto.PROCESS_ICON;
            if (process.getParent() == null) p = new TreeDto(process.getId()+"", "#", process.getName(), icon, process.isPrimitive());
            else p = new TreeDto(process.getId()+"", process.getParent().getId() + "", process.getName(), icon, process.isPrimitive());
            data.add(p);
            if(process.isPrimitive() && process.getActivityList() != null) {
                icon = TreeDto.ACTIVITY_ICON;
                for (Activity activity : process.getActivityList()) {
                    p = new TreeDto("a"+activity.getId(), process.getId()+"", activity.getName(), icon);
                    data.add(p);
                }
            }
            System.out.println("@@"+process.isPrimitive()+" "+process.getActivityList());
        }
        for (TreeDto treeDto : data) {
            System.out.println(treeDto);
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @RequestMapping(path = "/api/process/{id}", method = RequestMethod.GET)
    public ResponseEntity<Process> showProcess(@PathVariable("id") long id) {
        Process process = processService.find(id);
        if (process == null) {
            throw new CustomException("There is no process with id " + id, "404");
        }
        return new ResponseEntity<>(process, HttpStatus.OK);
    }
    
    @RequestMapping(path = "/api/activity/edit", method = RequestMethod.POST)
    public ResponseEntity<String> editActivity(Long id, String name) {
        Activity activity = activityService.find(id);
        activity.setName(name);
        activityService.save(activity);
        return new ResponseEntity<>("Activity successfully edited", HttpStatus.OK);
    }
    
    @RequestMapping(path = "/api/process/edit", method = RequestMethod.GET)
    public ResponseEntity<String> editProcess(Long id, String name, boolean primitive) {
        System.out.println("id: " + id + "name: " + name + "primitive: " + primitive );
        Process process = processService.find(id);
        process.setName(name);
        process.setPrimitive(primitive);
        processService.save(process);
        return new ResponseEntity<>("Process successfully edited", HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleError(Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
