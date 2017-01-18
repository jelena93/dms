/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers.rest;

import org.nst.dms.service.CompanyService;
import java.util.List;
import org.json.JSONObject;
import org.nst.dms.domain.Company;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.DocumentType;
import org.nst.dms.service.DocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
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

    @RequestMapping(value = "/api/companies/search", method = RequestMethod.GET)
    public ResponseEntity<List<Company>> search(String name) {
        List<Company> companies;
        if(name.isEmpty()) companies = companyService.findAll();
        else companies = companyService.search(name);
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/document-type", method = RequestMethod.GET)
    public ResponseEntity<List<Descriptor>> getDocumentTypeDescriptors(Long id) {
        DocumentType documentType = documentTypeService.find(id);
        return new ResponseEntity<>(documentType.getDescriptors(), HttpStatus.OK);
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
