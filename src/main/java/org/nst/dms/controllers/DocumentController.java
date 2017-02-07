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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.nst.dms.controllers.exceptions.CustomException;
import org.nst.dms.domain.Activity;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.Document;
import org.nst.dms.domain.DocumentType;
import org.nst.dms.service.DescriptorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.service.DocumentTypeService;
import org.springframework.web.multipart.MultipartFile;
import org.nst.dms.service.ActivityService;

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
    private ActivityService activityService;
    @Autowired
    private DescriptorService descriptorService;
    @Autowired
    ServletContext context;

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public ModelAndView save() {
        ModelAndView mv = new ModelAndView("add_document");
        List<DocumentType> documentTypes = documentTypeService.findAll();
        mv.addObject("documentTypes", documentTypes);
        mv.addObject("action_type_processes_search", "add_document");
        return mv;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ModelAndView save(String inputOutput, MultipartFile file, long docType, HttpServletRequest request, long activityID) {
        Activity activity = activityService.find(activityID);
        String url = saveFile(file);
        DocumentType documentType = documentTypeService.find(docType);
        List<Descriptor> descriptors = documentType.getDescriptors();
        List<Descriptor> newDescriptors = new ArrayList<>();
        List<Descriptor> existingDescriptors = descriptorService.getDescriptorValuesForDocumentType(docType);
        int numberOfIdenticalDescriptors = 0;
        for (Descriptor descriptor : descriptors) {
            String key = descriptor.getDescriptorKey();
            String value = request.getParameter(key);
            descriptor.setValue(value);
            if(descriptor.getValue() == null) throw new CustomException("Descriptor value is not correct", "500");
            Descriptor newDescriptor = new Descriptor(key, descriptor.getValue(), docType, descriptor.getDescriptorType());
            newDescriptors.add(newDescriptor);
            numberOfIdenticalDescriptors += checkIfFileAlreadyAdded(existingDescriptors, newDescriptor);
        }
        if(numberOfIdenticalDescriptors == descriptors.size()) {
//            @TODO neki jOptionpane
            throw new CustomException("Document already exists", "500");
        }
        Document document = new Document(url);
        document.setDescriptors(newDescriptors);
        if(inputOutput.equals("input")) activity.getInputList().add(document);
        else activity.getOutputList().add(document);
//        documentTypeService.save(documentType);
        activityService.save(activity);
        return new ModelAndView("add_document", "success_message", "Document successfully added");
    }

    private String saveFile(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String relativeWebPath = "/resources/avatars";
            String absoluteFilePath = context.getRealPath(relativeWebPath);
            File uploadedFile = new File(absoluteFilePath, file.getOriginalFilename());
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile))) {
                stream.write(bytes);
            }
            return absoluteFilePath + File.separator + file.getOriginalFilename();
        } catch (IOException e) {
            throw new CustomException("Failed to upload " + file.getOriginalFilename() + "\n" + e.getMessage(), "500");
        }
    }

    private int checkIfFileAlreadyAdded(List<Descriptor> existingDescriptors, Descriptor newDescriptor) {
        for (Descriptor existingDescriptor : existingDescriptors) {
            if(existingDescriptor.equals(newDescriptor)) return 1;
        }
        return 0;
    }
}
