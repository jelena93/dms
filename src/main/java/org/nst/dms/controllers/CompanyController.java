/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import org.nst.dms.dto.MessageDto;
import java.util.List;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.nst.dms.domain.Company;
import org.nst.dms.controllers.exceptions.CustomException;
import org.nst.dms.domain.User;
import org.nst.dms.dto.CompanyDto;
import org.nst.dms.elasticsearch.indexing.CompanyIndexer;
import org.nst.dms.elasticsearch.services.ElasticSearchService;
import org.nst.dms.elasticsearch.util.ElasticSearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.services.CompanyService;
import org.nst.dms.services.UserService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author Hachiko
 */
@Controller
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyIndexer companyIndexer;
    @Autowired
    private ElasticSearchService elasticSearchService;

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public String addCompany() {
        return "add_company";
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ModelAndView save(String name, String pib, String identificationNumber, String headquarters) throws Exception {
        Company c = new Company(name, pib, identificationNumber, headquarters);
        companyService.save(c);
        saveCompanyToElasticSearch(c);
        return new ModelAndView("add_company", "message", new MessageDto(MessageDto.MESSAGE_TYPE_SUCCESS, "Company successfully added"));
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public ModelAndView findAll() throws IOException {
        SearchResponse searchResponse = elasticSearchService.searchCompanies("", ElasticSearchUtil.QUERY_SIZE_LIMIT, 1);
        List< CompanyDto> companies = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (SearchHit hit : searchResponse.getHits()) {
            companies.add(mapper.readValue(hit.getSourceAsString(), CompanyDto.class));
        }
        ModelAndView mv = new ModelAndView("search_companies");
        mv.addObject("companies", companies);
        mv.addObject("total", searchResponse.getHits().getTotalHits());
        return mv;
    }

    @RequestMapping(path = "/company/{id}", method = RequestMethod.GET)
    public ModelAndView showCompany(@PathVariable("id") long id) {
        Company company = companyService.findOne(id);
        if (company == null) {
            throw new CustomException("There is no company with id " + id, "404");
        }
        List<User> usersOfCompany = userService.findByCompanyId(id);
        ModelAndView mv = new ModelAndView("company");
        mv.addObject("company", company);
        mv.addObject("users", usersOfCompany);
        return mv;
    }

    private void saveCompanyToElasticSearch(Company company) throws Exception {
        companyIndexer.indexCompany(company);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
