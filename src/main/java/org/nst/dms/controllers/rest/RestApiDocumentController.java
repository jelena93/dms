/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers.rest;

import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.nst.dms.domain.Activity;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.Document;
import org.nst.dms.domain.DocumentType;
import org.nst.dms.dto.MessageDto;
import org.nst.dms.service.ActivityService;
import org.nst.dms.service.DescriptorService;
import org.nst.dms.service.DocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
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
@RequestMapping("/api/documents")
public class RestApiDocumentController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private DescriptorService descriptorService;
    @Autowired
    private DocumentTypeService documentTypeService;
    
    @RequestMapping(path = "/validation", method = RequestMethod.POST)
    public ResponseEntity<MessageDto> checkIfDocumentExists(HttpServletRequest request, long docType, long activityID, String inputOutput) throws Exception {
        DocumentType documentType = documentTypeService.find(docType);
        List<Descriptor> descriptors = documentType.getDescriptors();
        List<Descriptor> existingDescriptors = descriptorService.findByDocumentType(docType);
        int numberOfIdenticalDescriptors = 0;
        Long existingDocumentID = null;
        for (Descriptor descriptor : descriptors) {
            String key = descriptor.getDescriptorKey();
            String value = request.getParameter(key).trim();
            descriptor.setValue(value);
            if(descriptor.getValue() == null) throw new Exception("Descriptor value is not correct");
            Descriptor newDescriptor = new Descriptor(key, descriptor.getValue(), docType, descriptor.getDescriptorType());
            Long id = checkIfFileAlreadyAdded(existingDescriptors, newDescriptor, activityID, inputOutput);
            if(id == null) continue;
            else if(existingDocumentID == null) existingDocumentID = id;
            else if(!Objects.equals(id, existingDocumentID)) continue;
            numberOfIdenticalDescriptors += 1;
        }
        if(numberOfIdenticalDescriptors == descriptors.size() && existingDocumentID != null) return new ResponseEntity<>(new MessageDto(MessageDto.MESSAGE_TYPE_QUESTION, existingDocumentID+""), HttpStatus.OK);
        return new ResponseEntity<>(new MessageDto(MessageDto.MESSAGE_TYPE_SUCCESS, "ok"), HttpStatus.OK);
    }
    
     private Long checkIfFileAlreadyAdded(List<Descriptor> existingDescriptors, Descriptor newDescriptor, long activityID, String inputOutput) {
        for (Descriptor existingDescriptor : existingDescriptors) {
            if(existingDescriptor.equals(newDescriptor)) {
                Activity activity = activityService.find(activityID);
                if(inputOutput.equals("input")) for (Document document : activity.getInputList()) if(document.getDescriptors().contains(existingDescriptor)) return document.getId();
                else if (inputOutput.equals("output")) for (Document d : activity.getOutputList()) if(d.getDescriptors().contains(existingDescriptor)) return document.getId();
            }
        }
        return null;
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
