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
import org.nst.dms.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.service.DocumentService;
import org.nst.dms.service.DocumentTypeService;
import org.nst.dms.service.ProcessService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private ProcessService processService;

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
    public ModelAndView save(String inputOutput, MultipartFile file, long docType, HttpServletRequest request, long processId) {
        String url = saveFile(file);
        Process process = processService.find(processId);
        if (process.isPrimitive()) {
            DocumentType documentType = documentTypeService.find(docType);
            List<Descriptor> descriptors = documentType.getDescriptors();
            List<Descriptor> newDescriptors = new ArrayList<>();
            for (Descriptor descriptor : descriptors) {
                String key = descriptor.getKey();
                String value = request.getParameter(key);
                Descriptor newDescriptor = new Descriptor(key, value);
                newDescriptors.add(newDescriptor);
            }
            descriptors.addAll(newDescriptors);
            Document document = new Document(documentType, url);
            if (inputOutput.equals("input")) {
                process.getInputList().add(document);
            } else {
                process.getOutputList().add(document);
            }
            documentTypeService.save(documentType);
            processService.save(process);
        }
        return new ModelAndView("add_document", "success_message", "Document successfully added");
    }

    private String saveFile(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            // Creating the directory to store file
            String rootPath = System.getProperty("catalina.home");
            File dir = new File(rootPath + File.separator + "dms-documents");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // Create the file on server
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
}
