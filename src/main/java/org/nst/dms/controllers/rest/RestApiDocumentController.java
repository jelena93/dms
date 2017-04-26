/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.Document;
import org.nst.dms.domain.DocumentType;
import org.nst.dms.domain.User;
import org.nst.dms.dto.DocumentDto;
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
        DocumentDto document;
        boolean sameName = false;
        Long documentID = null;
        List<DocumentDto> documents = findByName(user.getCompany().getId(), file.getOriginalFilename(), 1, 1);
        System.out.println("@@@@@@ documents sameName " + documents);
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
                document = findByDescriptors(user.getCompany().getId(), key, docType, descriptor.convertValueToString(), 10, 1);
                if (document != null) {
                    numberOfExistingDescripotrs++;
                    documentID = document.getId();
                }
            }
        }
        if (sameName) {
            return new ResponseEntity<>(new MessageDto(MessageDto.MESSAGE_TYPE_QUESTION,
                    "Document with same name already exists. Do you want to rewrite it?", documentID, MessageDto.MESSAGE_ACTION_EDIT), HttpStatus.OK);
        }
        if (numberOfDefaultDescripotrs == numberOfExistingDescripotrs) {
            return new ResponseEntity<>(new MessageDto(MessageDto.MESSAGE_TYPE_QUESTION,
                    "Document with same descriptors already exists. Do you want to rewrite it?", documentID, MessageDto.MESSAGE_ACTION_EDIT), HttpStatus.OK);
        }
        document = checkIfDocumentContentExists(user.getCompany().getId(), file, 1, 1);
        System.out.println("@@@@@@ documents content " + document);

        if (document != null) {
            return new ResponseEntity<>(new MessageDto(MessageDto.MESSAGE_TYPE_QUESTION, "Document: " + document.getFileName()
                    + " with same content already exists. Do you want to save it anyway?", MessageDto.MESSAGE_ACTION_ADD), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageDto(MessageDto.MESSAGE_TYPE_SUCCESS, "ok"), HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> search(Authentication authentication, String query, int page) throws IOException, TikaException, TikaException {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        User user = userService.findOne(userDto.getUsername());
        Map<String, Object> map = new HashMap<>();
        List<DocumentDto> documents = new ArrayList<>();
        SearchResponse searchResponse = elasticSearchService.searchDocumentsForCompany(user.getCompany().getId(), query, ElasticSearchUtil.QUERY_SIZE_LIMIT, page);
        ObjectMapper mapper = new ObjectMapper();
        for (SearchHit hit : searchResponse.getHits()) {
            documents.add(mapper.readValue(hit.getSourceAsString(), DocumentDto.class));
        }
        map.put("documents", documents);
        map.put("total", searchResponse.getHits().getTotalHits());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    private List<DocumentDto> findByName(long companyID, String fileName, int limit, int page) throws IOException {
        List<DocumentDto> documents = elasticSearchService.findDocumentsForCompanyByFileName(companyID, fileName, limit, page);
        return documents;
    }

    private DocumentDto checkIfDocumentContentExists(Long companyID, MultipartFile file, int limit, int page) throws IOException, TikaException {
        String parsedContent = tika.parseToString(file.getInputStream());
        List<DocumentDto> documents = elasticSearchService.findDocumentsForCompanyByContent(companyID, parsedContent, limit, page);
        if (!documents.isEmpty()) {
            return documents.get(0);
        }
        return null;
    }

    private DocumentDto findByDescriptors(long companyID, String descriptorKey, long docType, String value, int limit, int page) throws IOException {
        List<DocumentDto> documents = elasticSearchService.findDocumentsForCompanyByDescriptors(companyID, descriptorKey, docType, value, limit, page);
        if (!documents.isEmpty()) {
            return documents.get(0);
        }
        return null;
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
