/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers.rest;

import java.util.List;
import org.nst.dms.domain.Company;
import org.nst.dms.dto.MessageDto;
import org.nst.dms.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.repository.query.Param;
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
 * @author Jelena
 */
@RestController
@RequestMapping("/api/companies")
public class RestApiCompanyController {

    @Autowired
    private CompanyService companyService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<List<Company>> search(@Param("name") String name) {
        List<Company> companies;
        if (name == null || name.isEmpty()) companies = companyService.findAll();
        else companies = companyService.findByNameContainingOrHeadquartersContaining(name);
        return new ResponseEntity<>(companies, HttpStatus.OK);
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
