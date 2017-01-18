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
import org.nst.dms.domain.Document;
import org.nst.dms.service.CompanyService;
import org.nst.dms.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author Hachiko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class DocumentContollerTest {

    @Autowired
    private DocumentController documentController;
    @Autowired
    private DocumentService documentService;

    private Document document;

    @Test
    public void testAddGetRequest() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.documentController).build();
        mockMvc.perform(get("/add")).andExpect(status().isOk())
        .andExpect(redirectedUrl("add_document"));
    }
    
    @Test
    public void testAdd() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.documentController).build();
        mockMvc.perform(post("/add").param("inputOutput", "input")
        .param("docType", "1").param("processId", "1"))
        .andExpect(status().isOk())
        .andExpect(redirectedUrl("add_document"));
    }
    
    @Test
    public void testFindOne() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.documentController).build();
        mockMvc.perform(get("/company/{id}")).andExpect(status().isOk())
        .andExpect(redirectedUrl("company"));
    }

    @Configuration
    static class DocumentControllerTestConfiguration {

        @Bean
        public DocumentService documentService() {
            return Mockito.mock(DocumentService.class);
        }

        @Bean
        public DocumentController documentController() {
            return new DocumentController();
        }
    }
}
