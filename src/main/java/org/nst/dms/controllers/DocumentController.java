/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import org.nst.dms.dto.MessageDto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.nst.dms.controllers.exceptions.CustomException;
import org.nst.dms.domain.Activity;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.Document;
import org.nst.dms.domain.DocumentType;
import org.nst.dms.domain.User;
import org.nst.dms.dto.UserDto;
import org.nst.dms.service.DescriptorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.service.DocumentTypeService;
import org.springframework.web.multipart.MultipartFile;
import org.nst.dms.service.ActivityService;
import org.nst.dms.service.DocumentService;
import org.nst.dms.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

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
    private DocumentService documentService;
    @Autowired
    private UserService userService;
    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public ModelAndView save(Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        ModelAndView mv = new ModelAndView("add_document");
        List<DocumentType> documentTypes = documentTypeService.findAll();
        User loggedUser = userService.findOne(userDto.getUsername());
        mv.addObject("documentTypes", documentTypes);
        mv.addObject("action_type_processes_search", "add_document");
        mv.addObject("company", loggedUser.getCompany());
        return mv;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ModelAndView save(Authentication authentication, String inputOutput, MultipartFile file, long docType, HttpServletRequest request, long activityID) {
        Activity activity = activityService.find(activityID);
        DocumentType documentType = documentTypeService.find(docType);
        List<Descriptor> descriptors = documentType.getDescriptors();
        List<Descriptor> newDescriptors = new ArrayList<>();
        List<Descriptor> existingDescriptors = descriptorService.getDescriptorValuesForDocumentType(docType);
        int numberOfIdenticalDescriptors = 0;
        for (Descriptor descriptor : descriptors) {
            String key = descriptor.getDescriptorKey();
            String value = request.getParameter(key).trim();
            descriptor.setValue(value);
            if(descriptor.getValue() == null) return new ModelAndView("add_document", "message", new MessageDto(MessageDto.MESSAGE_TYPE_ERROR, "Descriptor value is not correct"));
            Descriptor newDescriptor = new Descriptor(key, descriptor.getValue(), docType, descriptor.getDescriptorType());
            newDescriptors.add(newDescriptor);
            numberOfIdenticalDescriptors += checkIfFileAlreadyAdded(existingDescriptors, newDescriptor);
        }
        if(numberOfIdenticalDescriptors == descriptors.size()) {
//            @TODO neki jOptionpane: Document already exists. Are you sure you want to rewrite the file?
            throw new CustomException("Document already exists", "500");
        }
        Document document = new Document();
        document.setFileName(file.getOriginalFilename());
        document.setFileType(file.getContentType());
        try {
            document.setFileContent(file.getBytes());
        } catch (IOException ex) {
            throw new CustomException(ex.getMessage(), "500");
        }
        document.setDescriptors(newDescriptors);
        if(inputOutput.equals("input")) activity.getInputList().add(document);
        else activity.getOutputList().add(document);
        activityService.save(activity);
        
        ModelAndView mv = new ModelAndView("add_document");
        List<DocumentType> documentTypes = documentTypeService.findAll();
        mv.addObject("documentTypes", documentTypes);
        mv.addObject("action_type_processes_search", "add_document");
        UserDto userDto = (UserDto) authentication.getPrincipal();
        User loggedUser = userService.findOne(userDto.getUsername());
        mv.addObject("company", loggedUser.getCompany());
        mv.addObject("message", new MessageDto(MessageDto.MESSAGE_TYPE_SUCCESS, "Document successfully added"));
        return mv;
    }

    private int checkIfFileAlreadyAdded(List<Descriptor> existingDescriptors, Descriptor newDescriptor) {
        for (Descriptor existingDescriptor : existingDescriptors) {
            if(existingDescriptor.equals(newDescriptor)) return 1;
        }
        return 0;
    }
    
    @RequestMapping(path = "/document/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> showFile(@PathVariable("id") long id) {
        Document document = documentService.findOne(id);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.valueOf(document.getFileType()));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + document.getFileName());
        header.setContentLength(document.getFileContent().length);
        return new ResponseEntity<>(document.getFileContent(), header, HttpStatus.OK);
    }
    @RequestMapping(path = "/document/download/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") long id) {
        Document document = documentService.findOne(id);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.valueOf(document.getFileType()));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document.getFileName());
        header.setContentLength(document.getFileContent().length);
        return new ResponseEntity<>(document.getFileContent(), header, HttpStatus.OK);
    }
}
