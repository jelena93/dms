/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import java.util.List;
import org.nst.dms.config.security.SecurityUser;
import org.nst.dms.domain.Document;
import org.nst.dms.domain.DocumentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.service.DocumentService;
import org.nst.dms.service.DocumentTypeService;
import org.springframework.security.core.Authentication;

/**
 *
 * @author Hachiko
 */
@Controller
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentTypeService documentTypeService;

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public ModelAndView save(Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        user.getBreadcrumbs().clear();
        user.getBreadcrumbs().add("Documents");
        user.getBreadcrumbs().add("Add document");
        ModelAndView mv = new ModelAndView("add_document");
        List<DocumentType> documentTypes = documentTypeService.findAll();
        mv.addObject("documentTypes", documentTypes);
        return mv;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ModelAndView save(Authentication authentication, String name, String[]descriptors) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        user.getBreadcrumbs().clear();
        user.getBreadcrumbs().add("Documents");
        user.getBreadcrumbs().add("Add document");
//        @TODO obrada greske ako je process null
//        Document d = documentService.save(document);
        return new ModelAndView("add_document", "poruka", "Dodat dokument");
    }
}
