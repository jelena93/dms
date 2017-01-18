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
import org.nst.dms.domain.Process;
import org.nst.dms.service.ProcessService;
import org.nst.dms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author Hachiko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ProcessContollerTest {

    @Autowired
    private ProcessController processController;
    @Autowired
    private ProcessService processService;

    private Process process;

    @Before
    public void setup() throws Exception {
        process = new Process("Proces 1", null, true);
        List<Process> processes = new ArrayList<>();
        processes.add(process);
        Mockito.when(processService.search("Proces 1")).thenReturn(processes);
    }

    @Test
    public void testAddGetRequest() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.processController).build();
        mockMvc.perform(get("/add")).andExpect(status().isOk()).andExpect(redirectedUrl("add_process"));
    }
    
    @Test
    public void testAdd() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.processController).build();
        mockMvc.perform(post("/add").param("name", "Proces 1").param("primitive", "true"))
                .andExpect(status().isOk()).andExpect(redirectedUrl("add_process"));
    }
    
    @Test
    public void testSearch() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.processController).build();
        mockMvc.perform(post("/search")).andExpect(status().isOk()).andExpect(redirectedUrl("search_processes"));
    }

    @After
    public void verify() {
        Mockito.verify(this.processService, VerificationModeFactory.times(1)).search("Proces 1");
        Mockito.reset();
    }

    @Configuration
    static class ProcessControllerTestConfiguration {

        @Bean
        public ProcessService processService() {
            return Mockito.mock(ProcessService.class);
        }

        @Bean
        public ProcessController processController() {
            return new ProcessController();
        }
    }
}
