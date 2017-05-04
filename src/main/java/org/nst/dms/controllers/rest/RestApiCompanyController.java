/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.nst.dms.domain.Company;
import org.nst.dms.dto.CompanyDto;
import org.nst.dms.dto.MessageDto;
import org.nst.dms.elasticsearch.indexing.CompanyIndexer;
import org.nst.dms.elasticsearch.services.ElasticSearchService;
import org.nst.dms.elasticsearch.util.ElasticSearchUtil;
import org.nst.dms.services.CompanyService;
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
@RequestMapping("/api/companies")
public class RestApiCompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyIndexer companyIndexer;
    @Autowired
    private ElasticSearchService elasticSearchService;

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ResponseEntity<String> editCompany(Long id, String name, String pib, String identificationNumber, String headquarters) throws Exception {
        Company company = companyService.findOne(id);
        if (company == null) {
            return new ResponseEntity<>("There is no company with id: " + id, HttpStatus.NOT_FOUND);
        }
        company.setName(name);
        company.setPib(pib);
        company.setIdentificationNumber(identificationNumber);
        company.setHeadquarters(headquarters);
        company = companyService.save(company);
        companyIndexer.updateCompany(company);
        return new ResponseEntity<>("Company successfully edited", HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> search(String query, int page) throws IOException {
        Map<String, Object> map = new HashMap<>();
        List<CompanyDto> companies = new ArrayList<>();
        SearchResponse searchResponse = elasticSearchService.searchCompanies(query, ElasticSearchUtil.QUERY_SIZE_LIMIT, page);
        ObjectMapper mapper = new ObjectMapper();
        for (SearchHit hit : searchResponse.getHits()) {
            companies.add(mapper.readValue(hit.getSourceAsString(), CompanyDto.class));
        }
        map.put("companies", companies);
        map.put("total", searchResponse.getHits().getTotalHits());
        return new ResponseEntity<>(map, HttpStatus.OK);
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
