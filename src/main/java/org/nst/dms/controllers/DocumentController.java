/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.nst.dms.config.security.SecurityUser;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.Document;
import org.nst.dms.domain.DocumentType;
import org.nst.dms.domain.Process;
import org.nst.dms.controllers.exceptions.CustomException;
import org.nst.dms.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.service.DocumentTypeService;
import org.nst.dms.service.ProcessService;
import org.nst.dms.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Hachiko
 */
@Controller
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentTypeService documentTypeService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private UserService userService;

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public ModelAndView save(Authentication authentication) {
        ModelAndView mv = new ModelAndView("add_document");
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        setBreadcrumbs(securityUser);
        User user = userService.findOne(securityUser.getUsername());
        mv.addObject("company", user.getCompany());
        mv.addObject("documentTypes", getDocs());
        return mv;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ModelAndView save(String inputOutput, MultipartFile file, long docType, HttpServletRequest request, long processId) {
        Process process = processService.find(processId);
        if (!process.isPrimitive()) {
            throw new CustomException("Can't add document to a non primitive process", "500");
        }
        String url = saveFile(file);
        DocumentType documentType = documentTypeService.find(docType);
        List<Descriptor> descriptors = documentType.getDescriptors();
        List<Descriptor> newDescriptors = new ArrayList<>();
        for (Descriptor descriptor : descriptors) {
            String key = descriptor.getKey();
            String value = request.getParameter(key);
            Descriptor newDescriptor = new Descriptor(key, value, docType);
            newDescriptors.add(newDescriptor);
        }
//            descriptors.addAll(newDescriptors);
        Document document = new Document(url);
        document.setDescriptors(newDescriptors);
        if (inputOutput.equals("input")) {
            process.getInputList().add(document);
        } else {
            process.getOutputList().add(document);
        }
        documentTypeService.save(documentType);
        processService.save(process);
        ModelAndView mv = new ModelAndView("add_document");
        mv.addObject("documentTypes", getDocs());
        mv.addObject("success_message", "Document successfully added");
        return mv;
    }

    private String saveFile(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String rootPath = System.getProperty("catalina.home");
            File dir = new File(rootPath + File.separator + "dms-documents");
            if (!dir.exists()) {
                dir.mkdir();
            }
            String url = dir.getAbsolutePath() + File.separator + file.getOriginalFilename();
            File serverFile = new File(url);
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
                stream.write(bytes);
            }
            return url;
        } catch (IOException e) {
            throw new CustomException("Failed to upload " + file.getOriginalFilename() + "\n" + e.getMessage(), "500");
        }
    }

    private void setBreadcrumbs(SecurityUser user) {
        user.getBreadcrumbs().clear();
        user.getBreadcrumbs().add("Documents");
        user.getBreadcrumbs().add("Add document");
    }

    private List<DocumentType> getDocs() {
        List<DocumentType> documentTypesAll = documentTypeService.findAll();
        List<DocumentType> documentTypes = new ArrayList<>();
        for (DocumentType documentType : documentTypesAll) {
            for (Descriptor descriptor : documentType.getDescriptors()) {
                if (descriptor.getKey() == null) {
                    DocumentType d = new DocumentType(documentType.getName());
                    d.setId(documentType.getId());
                    d.getDescriptors().add(descriptor);
                }
            }
        }
        return documentTypes;
    }
}
