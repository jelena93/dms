/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers.rest;

import java.io.IOException;
import java.util.ArrayList;
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
import org.nst.dms.elasticsearch.indexing.ElasticClient;
import org.nst.dms.elasticsearch.services.ElasticSearchService;
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
//                DescriptorElasticSearch newDescriptor = new DescriptorElasticSearch(descriptor.getId(), docType, key, descriptor.getDescriptorType(),
//                        descriptor.getValueAsString());
//                document = findByDescriptors(user.getCompany().getId(), newDescriptor);
//                if (document != null) {
//                    numberOfExistingDescripotrs++;
//                    documentID = document.getId();
//                }
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
    public ResponseEntity<List<Document>> search(Authentication authentication, String query) throws IOException, TikaException, TikaException {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        User user = userService.findOne(userDto.getUsername());
        List<Document> documents = find(user.getCompany().getId(), query);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    private List<Document> findByName(String fileName) {
        List<Document> documents = documentService.findByFileName(fileName);
        return documents;
    }

    private Document checkIfDocumentContentExists(Long companyID, MultipartFile file) throws IOException, TikaException {
//        List<Document> documents = documentService.findAll();
//        for (Document document : documents) {
//            if (document.getCompanyID().equals(companyID)
//                    && tika.parseToString(file.getInputStream())
//                            .equals(tika.parseToString(new ByteArrayInputStream(document.getFileContent())))) {
//                return document;
//            }
//        }
        return null;
    }

    private List<Document> searchContent(Long companyID, String value) throws IOException, TikaException {
//        List<Document> allDocuments = documentElasticSearchService.findByCompanyID(companyID);
//        List<Document> documents = new ArrayList<>();
//        for (Document document : allDocuments) {
//            if (tika.parseToString(new ByteArrayInputStream(document.getFileContent()))
//                    .toLowerCase().contains(value.toLowerCase())) {
//                documents.add(document);
//            }
//        }
//        return documents;
        return new ArrayList<>();
    }

//    private Document findByDescriptors(Long companyID, DescriptorElasticSearch descriptor) {
//        BoolQueryBuilder builder = boolQuery();
//        builder.must(nestedQuery("descriptors", matchQuery("descriptors.descriptorKey", descriptor.getDescriptorKey())))
//                .must(nestedQuery("descriptors", termQuery("descriptors.documentType", descriptor.getDocumentType())))
//                .must(nestedQuery("descriptors", matchQuery("descriptors.value", descriptor.getValue())))
//                .must(termQuery("companyID", companyID));
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(builder)
//                .build();
//        List<DocumentElasticSearch> documents = elasticsearchTemplate.queryForList(searchQuery, DocumentElasticSearch.class);
//        if (!documents.isEmpty()) {
//            return documents.get(0);
//        }
//        return null;
//    }

//    private List<Document> findByDescriptorKeyOrValue(Long companyID, String descriptorKey, String descriptorValue) {
//        BoolQueryBuilder builder = boolQuery();
//        builder.must(termQuery("companyID", companyID));
//        if (descriptorKey != null) {
//            builder.must(nestedQuery("descriptors", matchQuery("descriptors.descriptorKey", descriptorKey)));
//        }
//        if (descriptorValue != null) {
//            builder.must(nestedQuery("descriptors", matchQuery("descriptors.value", descriptorValue)));
//        }
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(builder)
//                .build();
//        List<Document> documents = elasticsearchTemplate.queryForList(searchQuery, DocumentElasticSearch.class);
//        return documents;
//    }

    private List<Document> find(long companyID, String query) {
        List<Document> documents = new ArrayList<>();
        return documents;
    }

    private List<Document> searchDocumentsForCompany(long companyID) {
        elasticSearchService.searchDocumentsForCompany(companyID + "", 10, 1);
        List<Document> documents = new ArrayList<>();
        return documents;
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
