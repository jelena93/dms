/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers.rest;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.tika.Tika;
import org.elasticsearch.index.query.BoolQueryBuilder;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.DocumentType;
import org.nst.elasticsearch.domain.DescriptorElasticSearch;
import org.nst.elasticsearch.domain.DocumentElasticSearch;
import org.nst.dms.dto.MessageDto;
import org.nst.dms.repositories.DocumentRepository;
import org.nst.dms.services.ActivityService;
import org.nst.dms.services.DescriptorService;
import org.nst.dms.services.DocumentTypeService;
import org.nst.elasticsearch.services.DocumentElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private ActivityService activityService;
    @Autowired
    private DescriptorService descriptorService;
    @Autowired
    private DocumentTypeService documentTypeService;
    @Autowired
    private DocumentElasticSearchService documentElasticSearchService;
    @Autowired
    private Tika tika;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private DocumentRepository documentRepository;

    @RequestMapping(path = "/validation", method = RequestMethod.POST)
    public ResponseEntity<MessageDto> validation(HttpServletRequest request, MultipartHttpServletRequest req, long docType, long activityID, String inputOutput) throws Exception {
        MultipartFile file = req.getFile("file");
        DocumentElasticSearch document = checkIfDocumentNameExists(file.getOriginalFilename());
        if (document != null) {
            return new ResponseEntity<>(new MessageDto(MessageDto.MESSAGE_TYPE_QUESTION,
                    "Document with the same name already exists. Do you want to rewrite it?", document.getId(), MessageDto.MESSAGE_ACTION_EDIT), HttpStatus.OK);
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
                DescriptorElasticSearch newDescriptor = new DescriptorElasticSearch(descriptor.getId(), docType, key, descriptor.getDescriptorType(),
                        descriptor.getValueAsString());
                document = checkIfDescriptorsExists(newDescriptor);
                if (document != null) {
                    numberOfExistingDescripotrs++;
                }
            }
        }
        if (numberOfDefaultDescripotrs == numberOfExistingDescripotrs) {
            return new ResponseEntity<>(new MessageDto(MessageDto.MESSAGE_TYPE_QUESTION,
                    "Document with same descriptors already exists. Do you want to rewrite it?", document.getId(), MessageDto.MESSAGE_ACTION_EDIT), HttpStatus.OK);
        }
        document = checkIfDocumentContentExists(file);
        if (document != null) {
            return new ResponseEntity<>(new MessageDto(MessageDto.MESSAGE_TYPE_QUESTION, "Document: " + document.getFileName()
                    + " with same content already exists. Do you want to save it anyway?", MessageDto.MESSAGE_ACTION_ADD), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageDto(MessageDto.MESSAGE_TYPE_SUCCESS, "ok"), HttpStatus.OK);
    }

    private DocumentElasticSearch checkIfDocumentNameExists(String fileName) {
        List<DocumentElasticSearch> documents = documentElasticSearchService.findByFileName(fileName);
        if (!documents.isEmpty()) {
            return documents.get(0);
        }
        return null;
    }

    private DocumentElasticSearch checkIfDocumentContentExists(MultipartFile file) throws IOException {
        List<DocumentElasticSearch> documents = documentElasticSearchService.findAll();
        for (DocumentElasticSearch document : documents) {
            if (tika.detect(file.getInputStream()).equals(tika.detect(document.getFileContent()))) {
                return document;
            }
        }
        return null;
    }

    private DocumentElasticSearch checkIfDescriptorsExists(DescriptorElasticSearch descriptor) {
        BoolQueryBuilder builder = boolQuery();
        builder.must(nestedQuery("descriptors", matchQuery("descriptors.descriptorKey", descriptor.getDescriptorKey())))
                .must(nestedQuery("descriptors", termQuery("descriptors.documentType", descriptor.getDocumentType())))
                .must(nestedQuery("descriptors", matchQuery("descriptors.value", descriptor.getValue())));
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(builder)
                .build();
        List<DocumentElasticSearch> documents = elasticsearchTemplate.queryForList(searchQuery, DocumentElasticSearch.class);
        System.out.println(descriptor.getDescriptorKey() + ", " + descriptor.getValue() + " Document List : " + documents);
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
