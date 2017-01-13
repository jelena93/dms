/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import java.util.List;
import org.nst.dms.domain.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.service.CompanyService;

/**
 *
 * @author Hachiko
 */
@Controller
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    //ovako? gde se definise? svi get process zahtevi ovde?
    @RequestMapping(path = "/add_company", method = RequestMethod.GET)
    public ModelAndView addProcess() {
        return new ModelAndView("add_company");
    }

    @RequestMapping(path = "/save_company", method = RequestMethod.POST)
    public ModelAndView save(Company company) {
//        @TODO obrada greske ako je process null
        Company c = companyService.save(company);
        return new ModelAndView("menu", "c", c);
    }
    
    @RequestMapping(path = "/find_all_companies", method = RequestMethod.POST)
    public ModelAndView findAll() {
//        @TODO obrada greske ako je null
        List<Company> companies = companyService.findAll();
        return new ModelAndView("menu", "companies", companies);
    }
    
    @RequestMapping(path = "/search_companies", method = RequestMethod.POST)
    public ModelAndView search(String name) {
//        @TODO obrada greske ako je null
        List<Company> companies = companyService.search(name);
        return new ModelAndView("menu", "companies", companies);
    }
}
