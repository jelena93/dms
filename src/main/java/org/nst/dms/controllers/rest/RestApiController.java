/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers.rest;

import java.util.ArrayList;
import org.nst.dms.service.CompanyService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.nst.dms.controllers.exceptions.CustomException;
import org.nst.dms.dto.UserDto;
import org.nst.dms.domain.Activity;
import org.nst.dms.domain.Company;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.DocumentType;
import org.nst.dms.domain.Process;
import org.nst.dms.domain.User;
import org.nst.dms.dto.MessageDto;
import org.nst.dms.dto.TreeDto;
import org.nst.dms.service.DocumentTypeService;
import org.nst.dms.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.nst.dms.service.ActivityService;
import org.nst.dms.service.DescriptorService;
import org.nst.dms.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

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
    @Autowired
    private UserService userService;
    @Autowired
    private DescriptorService descriptorService;

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
    public ResponseEntity<List<TreeDto>> getProcesses(Authentication authentication) {
        UserDto user = (UserDto) authentication.getPrincipal();
        User activeUser  = userService.findOne(user.getUsername());
        List<Process> processes = activeUser.getCompany().getProcesses();
        List<TreeDto> data = new ArrayList<>();
        for (Process process : processes) {
            TreeDto p;
            String icon;
            icon = TreeDto.PROCESS_ICON;
            if (process.getParent() == null) p = new TreeDto(process.getId(), "#", process.getName(), icon, process.isPrimitive());
            else p = new TreeDto(process.getId(), process.getParent().getId() + "", process.getName(), icon, process.isPrimitive());
            data.add(p);
            if(process.isPrimitive() && process.getActivityList() != null) {
                icon = TreeDto.ACTIVITY_ICON;
                for (Activity activity : process.getActivityList()) {
                    p = new TreeDto(activity.getId(), process.getId()+"", activity.getName(), icon);
                    data.add(p);
                }
            }
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(path = "/api/process/{id}", method = RequestMethod.GET)
    public ResponseEntity<Process> showProcess(@PathVariable("id") long id) throws Exception {
        Process process = processService.find(id);
        if (process == null) {
            throw new Exception("There is no process with id " + id);
        }
        return new ResponseEntity<>(process, HttpStatus.OK);
    }

    @RequestMapping(path = "/api/activity/{id}", method = RequestMethod.GET)
    public ResponseEntity<Activity> showActivity(@PathVariable("id") long id) throws Exception {
        Activity activity = activityService.find(id);
        if (activity == null) {
            throw new Exception("There is no activity with id " + id);
        }
        return new ResponseEntity<>(activity, HttpStatus.OK);
    }

    @RequestMapping(path = "/api/activity/edit", method = RequestMethod.POST)
    public ResponseEntity<String> editActivity(Long id, String name) {
        Activity activity = activityService.find(id);
        activity.setName(name);
        activityService.save(activity);
        return new ResponseEntity<>("Activity successfully edited", HttpStatus.OK);
    }

    @RequestMapping(path = "/api/process/edit", method = RequestMethod.POST)
    public ResponseEntity<String> editProcess(Authentication authentication, Long id, String name, boolean primitive) {
        Process process = processService.find(id);
        if(process == null) return new ResponseEntity<>("Process is null", HttpStatus.OK);
        process.setName(name);
        if (process.isPrimitive() != primitive && primitive) deleteProcessFromCompany(authentication, process);
        if (process.isPrimitive() != primitive && !primitive) process.getActivityList().clear();
        process.setPrimitive(primitive);
        processService.save(process);
        return new ResponseEntity<>("Process successfully edited", HttpStatus.OK);
    }
    
    @RequestMapping(path = "/api/documents/validation", method = RequestMethod.POST)
    public ResponseEntity<String> checkIfDocumentExists(HttpServletRequest request, long docType) throws Exception {
        DocumentType documentType = documentTypeService.find(docType);
        List<Descriptor> descriptors = documentType.getDescriptors();
        List<Descriptor> newDescriptors = new ArrayList<>();
        List<Descriptor> existingDescriptors = descriptorService.getDescriptorValuesForDocumentType(docType);
        int numberOfIdenticalDescriptors = 0;
        for (Descriptor descriptor : descriptors) {
            String key = descriptor.getDescriptorKey();
            String value = request.getParameter(key).trim();
            descriptor.setValue(value);
            if(descriptor.getValue() == null) throw new Exception("Descriptor value is not correct");
            Descriptor newDescriptor = new Descriptor(key, descriptor.getValue(), docType, descriptor.getDescriptorType());
            newDescriptors.add(newDescriptor);
            numberOfIdenticalDescriptors += checkIfFileAlreadyAdded(existingDescriptors, newDescriptor);
        }
        if(numberOfIdenticalDescriptors == descriptors.size()) {
            throw new Exception("Document already exists. Are you sure you want to rewrite the file?");
        }
        return new ResponseEntity<>("Process successfully edited", HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleError(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return new ResponseEntity<Object>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    private void deleteProcessFromCompany(Authentication authentication, Process process) {
        UserDto user = (UserDto) authentication.getPrincipal();
        Company company = userService.findOne(user.getUsername()).getCompany();
        List<Process> processes = company.getProcesses();
        deleteChildren(process, processes, true);
        companyService.save(company);
    }

    private void deleteChildren(Process process, List<Process> processes, boolean root) {
        List<Process> children = getChildren(process, processes);
        for (Process child : children) {
            deleteChildren(child, processes, false);
        }
        if (!root) {
            processes.remove(process);
        }
    }

    private List<Process> getChildren(Process p, List<Process> lista) {
        List<Process> children = new ArrayList<>();
        for (Process process : lista) {
            if (p != null && p.equals(process.getParent())) {
                children.add(process);
            }
        }
        return children;
    }
    
    private int checkIfFileAlreadyAdded(List<Descriptor> existingDescriptors, Descriptor newDescriptor) {
        for (Descriptor existingDescriptor : existingDescriptors) {
            if(existingDescriptor.equals(newDescriptor)) return 1;
        }
        return 0;
    }
}
