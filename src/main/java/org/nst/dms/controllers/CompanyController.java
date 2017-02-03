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
import org.nst.dms.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.service.CompanyService;
import org.nst.dms.service.UserService;
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
    @Autowired
    private UserService userService;

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public String addProcess() { return "add_company"; }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ModelAndView save(String name, String pib, String identificationNumber, String headquarters) {
        Company c = new Company(name, pib, identificationNumber, headquarters);
        companyService.save(c);
        return new ModelAndView("add_company", "success_message", "Company successfully added");
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public ModelAndView findAll() {
        List<Company> companies = companyService.findAll();
        return new ModelAndView("search_companies", "companies", companies);
    }

    @RequestMapping(path = "/company/{id}", method = RequestMethod.GET)
    public ModelAndView showCompany(@PathVariable("id") long id) {
        Company company = companyService.findOne(id);
        if (company == null) throw new CustomException("There is no company with id " + id, "404");
        List<User> usersOfCompany = userService.findUsersOfCompany(company);
        System.out.println("@@@@@@@@@@@ " + usersOfCompany);
        ModelAndView mv = new ModelAndView("company");
        mv.addObject("company", company);
        mv.addObject("users", usersOfCompany);
        return mv;
    }
}
