/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers.rest;

import java.util.ArrayList;
import org.nst.dms.service.CompanyService;
import java.util.List;
import org.nst.dms.domain.Company;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.DocumentType;
import org.nst.dms.domain.Process;
import org.nst.dms.domain.dto.ProcessDto;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author Hachiko
 */
@RestController
public class SearchController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private DocumentTypeService documentTypeService;
    @Autowired
    private ProcessService processService;

    @RequestMapping(value = "/api/companies/search", method = RequestMethod.GET)
    public ResponseEntity<List<Company>> search(@Param("name")String name) {
        System.out.println("@@@@@@@@@@@" + name);
        List<Company> companies;
        if(name == null || name.isEmpty()) companies = companyService.findAll();
        else companies = companyService.search(name);
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/document-type", method = RequestMethod.GET)
    public ResponseEntity<List<Descriptor>> getDocumentTypeDescriptors(Long id) {
        DocumentType documentType = documentTypeService.find(id);
        return new ResponseEntity<>(documentType.getDescriptors(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/processes", method = RequestMethod.GET)
    public ResponseEntity<List<ProcessDto>> getProcesses() {
        List<Process> processes = processService.findAll();
        List<ProcessDto> data = new ArrayList<>();
        for (Process process : processes) {
            ProcessDto p;
            String icon;
            if (process.isPrimitive()) {
                icon = "glyphicon glyphicon-ok";
            } else {
                icon = "glyphicon glyphicon-folder-open";
            }
            if (process.getParent() == null) {
                p = new ProcessDto(process.getId(), "#", process.getName(), icon, process.isPrimitive());
            } else {
                p = new ProcessDto(process.getId(), process.getParent().getId() + "", process.getName(), icon, process.isPrimitive());
            }
            data.add(p);
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/processes/search", method = RequestMethod.GET)
    public ResponseEntity<List<ProcessDto>> searchProcesses(@Param("name") String name) {
        if (name == null || name.isEmpty()) {
            return getProcesses();
        }
        List<Process> processesAll = processService.findAll();
        List<Process> processesName = processService.search(name);
        List<ProcessDto> data = new ArrayList<>();
        for (Process process : processesAll) {
            ProcessDto p;
            String icon;
            if (process.isPrimitive()) {
                icon = "glyphicon glyphicon-ok";
            } else {
                icon = "glyphicon glyphicon-folder-open";
            }
            if (processesName.contains(process)) {
                p = new ProcessDto(process.getId(), "#", process.getName(), icon, process.isPrimitive());
                data.add(p);
            } else {
                if (processesName.contains(process.getParent())) {
                    p = new ProcessDto(process.getId(), process.getParent().getId() + "", process.getName(), icon, process.isPrimitive());
                    data.add(p);
                }
            }
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
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
