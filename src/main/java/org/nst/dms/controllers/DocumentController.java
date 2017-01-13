/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import org.nst.dms.domain.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.service.DocumentService;

/**
 *
 * @author Hachiko
 */
@Controller
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @RequestMapping(path = "/save_document", method = RequestMethod.POST)
    public ModelAndView save(Document document) {
//        @TODO obrada greske ako je process null
        Document d = documentService.save(document);
        return new ModelAndView("menu", "d", d);
    }
}
