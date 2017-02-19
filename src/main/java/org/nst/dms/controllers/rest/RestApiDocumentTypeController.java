/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers.rest;

import java.util.List;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.DocumentType;
import org.nst.dms.dto.MessageDto;
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
@RequestMapping("/api/document-type")
public class RestApiDocumentTypeController {

    @Autowired
    private DocumentTypeService documentTypeService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Descriptor>> getDocumentTypeDescriptors(Long id) {
        DocumentType documentType = documentTypeService.find(id);
        return new ResponseEntity<>(documentType.getDescriptors(), HttpStatus.OK);
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
