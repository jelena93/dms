/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import java.util.List;
import org.nst.dms.config.security.SecurityUser;
import org.nst.dms.domain.Company;
import org.nst.dms.controllers.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.service.CompanyService;
import org.springframework.security.core.Authentication;
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

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public String addProcess(Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        user.getBreadcrumbs().clear();
        user.getBreadcrumbs().add("Companies");
        user.getBreadcrumbs().add("Add company");
        return "add_company";
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ModelAndView save(String name, String pib, String identificationNumber, String headquarters) {
        Company c = new Company(name, pib, identificationNumber, headquarters);
        companyService.save(c);
        return new ModelAndView("add_company", "success_message", "Company successfully added");
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public ModelAndView findAll(Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        user.getBreadcrumbs().clear();
        user.getBreadcrumbs().add("Companies");
        user.getBreadcrumbs().add("Search companies");
        List<Company> companies = companyService.findAll();
        return new ModelAndView("search_companies", "companies", companies);
    }

    @RequestMapping(path = "/company/{id}", method = RequestMethod.GET)
    public ModelAndView showCompany(Authentication authentication, @PathVariable("id") long id) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        user.getBreadcrumbs().clear();
        user.getBreadcrumbs().add("Companies");
        user.getBreadcrumbs().add("Company");
        Company company = companyService.findOne(id);
        if (company == null) {
            throw new CustomException("There is no company with id " + id, "404");
        }
        return new ModelAndView("company", "company", company);
    }

}
