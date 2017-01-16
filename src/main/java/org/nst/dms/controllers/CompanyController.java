/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import java.util.List;
import org.nst.dms.config.security.SecurityUser;
import org.nst.dms.domain.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.service.CompanyService;
import org.springframework.security.core.Authentication;

/**
 *
 * @author Hachiko
 */
@Controller
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public String addProcess(Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        user.getBreadcrumbs().clear();
        user.getBreadcrumbs().add("Companies");
        user.getBreadcrumbs().add("Add company");
        return "add_company";
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public String save(String name, String pib, String identificationNumber, String headquarters) {
        Company c = new Company(name, pib, identificationNumber, headquarters);
        companyService.save(c);
        return "redirect:/companies/search";
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public ModelAndView findAll(Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        user.getBreadcrumbs().clear();
        user.getBreadcrumbs().add("Companies");
        user.getBreadcrumbs().add("Search companies");
//        @TODO obrada greske ako je null
        List<Company> companies = companyService.findAll();
        return new ModelAndView("search_companies", "companies", companies);
    }

//    @RequestMapping(path = "/search_companies", method = RequestMethod.POST)
//    public ModelAndView search(String name) {
////        @TODO obrada greske ako je null
//        List<Company> companies = companyService.search(name);
//        return new ModelAndView("menu", "companies", companies);
//    }
}
