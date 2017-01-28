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
import org.nst.dms.domain.Action;
import org.nst.dms.domain.Company;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.DocumentType;
import org.nst.dms.domain.Process;
import org.nst.dms.domain.dto.TreeDto;
import org.nst.dms.service.ActionService;
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
    private ActionService actionService;

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
            if (process.getParent() == null) p = new TreeDto(process.getId(), "#", process.getName(), icon, process.isPrimitive());
            else p = new TreeDto(process.getId(), process.getParent().getId() + "", process.getName(), icon, process.isPrimitive());
            data.add(p);
            if(process.isPrimitive() && process.getActionList() != null) {
                icon = TreeDto.ACTION_ICON;
                for (Action action : process.getActionList()) {
                    p = new TreeDto(action.getId(), action.getParent().getId()+"", action.getName(), icon);
                    data.add(p);
                }
            }
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    @RequestMapping(path = "/api/process/{id}", method = RequestMethod.GET)
    public ResponseEntity<Process> showCompany(@PathVariable("id") long id) {
        Process process = processService.find(id);
        if (process == null) {
            throw new CustomException("There is no process with id " + id, "404");
        }
        return new ResponseEntity<>(process, HttpStatus.OK);
    }
    
    @RequestMapping(path = "/api/action/edit", method = RequestMethod.POST)
    public ModelAndView edit(Long id, String name, Long parent) {
        if (parent == null) {
            throw new CustomException("Action has to have a parent", "500");
        }
        Action action = actionService.find(id);
        Process processParent = processService.find(parent);
        action.setName(name);
        action.setParent(processParent);
        actionService.save(action);
        return new ModelAndView("add_action", "success_message", "Action successfully edited");
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
