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
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.nst.dms.controllers.exceptions.CustomException;
import org.nst.dms.domain.Activity;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.Document;
import org.nst.dms.domain.DocumentType;
import org.nst.dms.domain.User;
import org.nst.dms.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.services.DocumentTypeService;
import org.springframework.web.multipart.MultipartFile;
import org.nst.dms.services.ActivityService;
import org.nst.dms.services.DocumentService;
import org.nst.dms.services.UserService;
import org.nst.elasticsearch.services.DocumentElasticSearchService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
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
    private DocumentService documentService;
    @Autowired
    private DocumentElasticSearchService documentElasticSearchService;
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
    public ModelAndView save(Authentication authentication, String inputOutput, MultipartFile file, long docType, HttpServletRequest request, long activityID, Long existingDocumentID) {
        Activity activity = activityService.find(activityID);
        DocumentType documentType = documentTypeService.find(docType);
        List<Descriptor> descriptors = documentType.getDescriptors();
        List<Descriptor> newDescriptors = new ArrayList<>();
        for (Descriptor descriptor : descriptors) {
            if (descriptor.getValue() == null) {
                String key = descriptor.getDescriptorKey();
                String value = request.getParameter(key).trim();
                descriptor.setValue(value);
                Descriptor newDescriptor = new Descriptor(key, descriptor.getValue(), docType, descriptor.getDescriptorType());
                newDescriptors.add(newDescriptor);
            }
        }
        Document document = new Document();
        boolean found = false;
        if(existingDocumentID != null) {
            List<Document> documents;
            if(inputOutput.equals("input")) documents = activity.getInputList();
            else documents = activity.getInputList();
            for (Document doc : documents) {
                if(Objects.equals(existingDocumentID, doc.getId())) {
                    document = doc;
                    found = true;
                    break;
                }
            }
        }
        document.setFileName(file.getOriginalFilename());
        document.setFileType(file.getContentType());
        try {
            document.setFileContent(file.getBytes());
        } catch (IOException ex) {
            ModelAndView mv = new ModelAndView("add_document");
            mv.addObject("message", new MessageDto(MessageDto.MESSAGE_TYPE_ERROR, ex.getMessage()));
            return mv;
        }
        document.setDescriptors(newDescriptors);
        if (!found) {
            if (inputOutput.equals("input"))activity.getInputList().add(document);
            else activity.getOutputList().add(document);
        }
        activity = activityService.save(activity);
        if (inputOutput.equals("input")) {
            documentElasticSearchService.save(activity.getInputList().get(activity.getInputList().size() - 1));
        } else {
            documentElasticSearchService.save(activity.getOutputList().get(activity.getOutputList().size() - 1));
        }
        for (Document d : documentElasticSearchService.findAll()) {
            System.out.println("doc " + d);
        }
        
        ModelAndView mv = new ModelAndView("add_document");
        List<DocumentType> documentTypes = documentTypeService.findAll();
        mv.addObject("documentTypes", documentTypes);
        mv.addObject("action_type_processes_search", "add_document");
        UserDto userDto = (UserDto) authentication.getPrincipal();
        User loggedUser = userService.findOne(userDto.getUsername());
        mv.addObject("company", loggedUser.getCompany());
        if(found) mv.addObject("message", new MessageDto(MessageDto.MESSAGE_TYPE_SUCCESS, "Document successfully edited"));
        else mv.addObject("message", new MessageDto(MessageDto.MESSAGE_TYPE_SUCCESS, "Document successfully added"));
        return mv;
    }

    @RequestMapping(path = "/document/display/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> showFile(@PathVariable("id") long id) {
        Document document = documentService.findOne(id);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.valueOf(document.getFileType()));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + document.getFileName());
        header.setContentLength(document.getFileContent().length);
        return new ResponseEntity<>(document.getFileContent(), header, HttpStatus.OK);
    }
    
    @RequestMapping(path = "/document/{id}", method = RequestMethod.GET)
    public ModelAndView showDocument(@PathVariable("id") long id) {
        Document document = documentService.findOne(id);
        if(document==null) throw new CustomException("There is no document with id "+id, "400");
        return new ModelAndView("document_info", "document", document);
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
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
