/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.nst.dms.domain.Company;
import org.nst.dms.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author Hachiko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CompanyContollerTest {

    @Autowired
    private CompanyController companyController;
    @Autowired
    private CompanyService companyService;

    private Company company;

    @Before
    public void setup() throws Exception {
        company = new Company("Soko Štark d.o.o. Beograd", "100002799", "07026447", "Bulevar Peka Dapčevića 29, Voždovac, Beograd", null);
        List<Company> companies = new ArrayList<>();
        companies.add(company);
        Mockito.when(this.companyService.search("Soko Stark d.o.o. Beograd")).thenReturn(companies);
    }

    @Test
    public void testAdd() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.companyController).build();
        mockMvc.perform(post("/add").param("name", "Soko Stark d.o.o. Beograd")
        .param("pib", "100002799").param("identificationNumber", "07026447").param("headquarters", "Bulevar Peka Dapčevića 29, Voždovac, Beograd"))
        .andExpect(status().isOk())
        .andExpect(redirectedUrl("add_company"));
    }
    
    @Test
    public void testAddGetRequest() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.companyController).build();
        mockMvc.perform(get("/add")).andExpect(status().isOk())
        .andExpect(redirectedUrl("search_companies"));
    }
    
    @Test
    public void testFindOne() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.companyController).build();
        mockMvc.perform(get("/company/{id}")).andExpect(status().isOk())
        .andExpect(redirectedUrl("company"));
    }

    @After
    public void verify() {
        Mockito.verify(this.companyService, VerificationModeFactory.times(1)).search("Soko Stark d.o.o. Beograd");
        Mockito.reset();
    }

    @Configuration
    static class CompanyControllerTestConfiguration {

        @Bean
        public CompanyService companyService() {
            return Mockito.mock(CompanyService.class);
        }

        @Bean
        public CompanyController companyController() {
            return new CompanyController();
        }
    }
}
