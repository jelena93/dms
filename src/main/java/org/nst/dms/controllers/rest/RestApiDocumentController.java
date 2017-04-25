/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.Document;
import org.nst.dms.domain.DocumentType;
import org.nst.dms.domain.User;
import org.nst.dms.dto.MessageDto;
import org.nst.dms.dto.UserDto;
import org.nst.dms.elasticsearch.services.ElasticSearchService;
import org.nst.dms.elasticsearch.util.ElasticSearchUtil;
import org.nst.dms.services.DocumentService;
import org.nst.dms.services.DocumentTypeService;
import org.nst.dms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author Jelena
 */
@RestController
@RequestMapping("/api/documents")
public class RestApiDocumentController {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentTypeService documentTypeService;
    @Autowired
    private UserService userService;
    @Autowired
    private Tika tika;
    @Autowired
    private ElasticSearchService elasticSearchService;

    @RequestMapping(path = "/validation", method = RequestMethod.POST)
    public ResponseEntity<MessageDto> validation(Authentication authentication, HttpServletRequest request, MultipartHttpServletRequest req, long docType, long activityID, String inputOutput) throws Exception {
        MultipartFile file = req.getFile("file");
        UserDto userDto = (UserDto) authentication.getPrincipal();
        User user = userService.findOne(userDto.getUsername());
        Document document;
        boolean sameName = false;
        Long documentID = null;
        List<Document> documents = findByName(file.getOriginalFilename());
        if (!documents.isEmpty()) {
            documentID = documents.get(0).getId();
            sameName = true;
        }
        DocumentType documentType = documentTypeService.find(docType);
        int numberOfExistingDescripotrs = 0;
        int numberOfDefaultDescripotrs = 0;
        for (Descriptor descriptor : documentType.getDescriptors()) {
            if (descriptor.getValue() == null) {
                String key = descriptor.getDescriptorKey();
                String value = request.getParameter(key).trim();
                descriptor.setValue(value);
                if (descriptor.getValue() == null) {
                    throw new Exception("Value for descriptor " + descriptor.getDescriptorKey()
                            + "  is not correct. Expecting descriptor of type "
                            + descriptor.getDescriptorType().getStringMessageByParamClass() + ".");
                }
                numberOfDefaultDescripotrs++;
                document = findByDescriptors(user.getCompany().getId(), key, docType, descriptor.getValueAsString(), 10, 1);
                if (document != null) {
                    numberOfExistingDescripotrs++;
                    documentID = document.getId();
                }
            }
        }
        if (numberOfDefaultDescripotrs == numberOfExistingDescripotrs) {
            if (sameName) {
                return new ResponseEntity<>(new MessageDto(MessageDto.MESSAGE_TYPE_QUESTION,
                        "Document with same name and descriptors already exists. Do you want to rewrite it?", documentID, MessageDto.MESSAGE_ACTION_EDIT), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageDto(MessageDto.MESSAGE_TYPE_QUESTION,
                        "Document with same descriptors already exists. Do you want to rewrite it?", documentID, MessageDto.MESSAGE_ACTION_EDIT), HttpStatus.OK);
            }
        }
        document = checkIfDocumentContentExists(user.getCompany().getId(), file);
        if (document != null) {
            return new ResponseEntity<>(new MessageDto(MessageDto.MESSAGE_TYPE_QUESTION, "Document: " + document.getFileName()
                    + " with same content already exists. Do you want to save it anyway?", MessageDto.MESSAGE_ACTION_ADD), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageDto(MessageDto.MESSAGE_TYPE_SUCCESS, "ok"), HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<List<Document>> search(Authentication authentication, String query, int page) throws IOException, TikaException, TikaException {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        User user = userService.findOne(userDto.getUsername());
        System.out.println("@@@ page " + page);
        List<Document> documents = searchDocumentsForCompany(user.getCompany().getId(), query, ElasticSearchUtil.SIZE_LIMIT, page);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    private List<Document> findByName(String fileName) {
        List<Document> documents = documentService.findByFileName(fileName);
        return documents;
    }

    private Document checkIfDocumentContentExists(Long companyID, MultipartFile file) throws IOException, TikaException {
        List<Document> documents = documentService.findAll();
        for (Document document : documents) {
            if (document.getCompanyID() == companyID
                    && tika.parseToString(file.getInputStream()).equals(tika.parseToString(new ByteArrayInputStream(document.getFileContent())))) {
                return document;
            }
        }
        return null;
    }

    private Document findByDescriptors(long companyID, String descriptorKey, long docType, String value, int limit, int page) throws IOException {
        List<Document> documents = elasticSearchService.findDocumentsForCompany(companyID, descriptorKey, docType, value, limit, page);
        if (!documents.isEmpty()) {
            return documents.get(0);
        }
        return null;
    }

    private List<Document> searchDocumentsForCompany(long companyID, String query, int limit, int page) throws IOException {
        return elasticSearchService.searchDocumentsForCompany(companyID, query, limit, page);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageDto> handleError(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return new ResponseEntity<>(new MessageDto(MessageDto.MESSAGE_TYPE_ERROR, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
