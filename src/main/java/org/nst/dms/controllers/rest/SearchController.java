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
    public ResponseEntity<JSONObject> search(String name) {
        List<Company> companies = companyService.search(name);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("companies", companies);
        String message;
        if (companies.isEmpty()) {
            message = "No companies found";
            jSONObject.put("message", message);
        }
        return new ResponseEntity<>(jSONObject, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/document-type", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getDocumentTypeDescriptors(Long id) {
        DocumentType documentType = documentTypeService.find(id);
        System.out.println("documentType" + documentType);
        JSONObject jSONObject = new JSONObject();
        String message;
        if (documentType == null) {
            message = "No document type with given name found";
            jSONObject.put("message", message);
        } else {
            jSONObject.put("descriptors", documentType.getDescriptors());
            if (documentType.getDescriptors().isEmpty()) {
                message = "No descriptors found";
                jSONObject.put("message", message);
            }
        }
        return new ResponseEntity<>(jSONObject, HttpStatus.OK);
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
