/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers.rest;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.tika.Tika;
import org.elasticsearch.index.query.BoolQueryBuilder;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.Document;
import org.nst.dms.domain.DocumentType;
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
    public ResponseEntity<MessageDto> checkIfDocumentExists(HttpServletRequest request, MultipartHttpServletRequest req, long docType, long activityID, String inputOutput) throws Exception {
        MultipartFile file = req.getFile("file");
        System.out.println("naziiiiv" + checkIfDocumentNameExists(file.getOriginalFilename()));

        System.out.println("sadrzaj" + checkIfDocumentContentExists(file));

        DocumentType documentType = documentTypeService.find(docType);

        for (Descriptor descriptor : documentType.getDescriptors()) {
            if (descriptor.getValue() == null) {
                String key = descriptor.getDescriptorKey();
                String value = request.getParameter(key).trim();
                descriptor.setValue(value);
                if (descriptor.getValue() == null) {
                    throw new Exception("Value for descriptor " + descriptor.getDescriptorKey()
                            + "  is not correct. Expecting descriptor of type " + descriptor.getDescriptorType().getStringMessageByParamClass() + ".");
                }
                Descriptor newDescriptor = new Descriptor(key, descriptor.getValue(), docType, descriptor.getDescriptorType());
                test(newDescriptor);
            }
        }
        return new ResponseEntity<>(new MessageDto(MessageDto.MESSAGE_TYPE_QUESTION, ""), HttpStatus.OK);
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

    private boolean isTheSameDate(Descriptor existingDescriptor, Descriptor newDescriptor) {
        Date d1 = (Date) existingDescriptor.getValue();
        Date d2 = (Date) newDescriptor.getValue();
        System.out.println("@@@@" + d1 + "--" + d2);
        return d1.equals(d2);
    }

    private boolean checkIfDocumentNameExists(String fileName) {
        return !documentElasticSearchService.findByFileName(fileName).isEmpty();
    }

    private void test(Descriptor descriptor) {
        BoolQueryBuilder builder = boolQuery();
        builder.must(nestedQuery("descriptors", matchQuery("descriptors.descriptorKey", descriptor.getDescriptorKey())))
                .must(nestedQuery("descriptors", termQuery("descriptors.documentType", descriptor.getDocumentType())));
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(builder)
                .build();
        List<Document> documents = elasticsearchTemplate.queryForList(searchQuery, Document.class);
        System.out.println(descriptor.getDescriptorKey() + ", " + descriptor.getValue() + " Document List : " + documents);
    }

    private boolean checkIfDocumentContentExists(MultipartFile file) throws IOException {
        List<Document> documents = documentElasticSearchService.findAll();
        for (Document document : documents) {
            return tika.detect(file.getInputStream()).equals(tika.detect(document.getFileContent()));
        }
        return false;
    }
}
