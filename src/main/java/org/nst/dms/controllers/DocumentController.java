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
import org.nst.dms.controllers.exceptions.CustomException;
import org.nst.dms.domain.Action;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.Document;
import org.nst.dms.domain.DocumentType;
import org.nst.dms.service.ActionService;
import org.nst.dms.service.DescriptorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.service.DocumentTypeService;
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
    private ActionService actionService;
    @Autowired
    private DescriptorService descriptorService;

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public ModelAndView save() {
        //@TODO? moze li ovo bolje nekako? Radili smo na neki dr nacin?
        ModelAndView mv = new ModelAndView("add_document");
        List<DocumentType> documentTypes = documentTypeService.findAll();
        mv.addObject("documentTypes", documentTypes);
        mv.addObject("action_type_processes_search", "add_document");
        return mv;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ModelAndView save(String inputOutput, MultipartFile file, long docType, HttpServletRequest request, long parent) {
        Action action = actionService.find(parent);
        String url = saveFile(file);
        DocumentType documentType = documentTypeService.find(docType);
        List<Descriptor> descriptors = documentType.getDescriptors();
        List<Descriptor> newDescriptors = new ArrayList<>();
        List<Descriptor> existingDescriptors = descriptorService.getDescriptorValuesForDocumentType(docType);
        int numberOfIdenticalDescriptors = 0;
        for (Descriptor descriptor : descriptors) {
            String key = descriptor.getKey();
            String value = request.getParameter(key);
            Descriptor newDescriptor = new Descriptor(key, value, docType);
            newDescriptors.add(newDescriptor);
            //@TODO napisati equals metodu koja proverava key-value a ne id i radice bolje?
            for (Descriptor existingDescriptor : existingDescriptors) {
                if(newDescriptor.getKey().equals(existingDescriptor.getKey()) && newDescriptor.getValue().equals(existingDescriptor.getValue())) numberOfIdenticalDescriptors++;
            }
        }
        //@TODO neki dijalog da li ste sigurni da zelite da pregazite fajl il nesto tako?
        if(numberOfIdenticalDescriptors == descriptors.size()) throw new CustomException("Document already exists", "500");
        Document document = new Document(url);
        document.setDescriptors(newDescriptors);
        if(inputOutput.equals("input")) action.getInputList().add(document);
        else action.getOutputList().add(document);
        documentTypeService.save(documentType);
        actionService.save(action);
        return new ModelAndView("add_document", "success_message", "Document successfully added");
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
}
