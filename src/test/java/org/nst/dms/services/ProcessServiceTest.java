/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.services;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.mockito.Mockito;
import org.nst.dms.repositories.ProcessRepository;
import org.nst.dms.domain.Process;
import org.nst.dms.services.impl.ProcessServiceImpl;

/**
 *
 * @author Hachiko
 */
//@TODO Prepraviti process testove zbog izbacivanja search-a
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ProcessServiceTest {
    @Autowired
    private ProcessService processService;
    @Autowired
    private ProcessRepository processRepository;
    @Before
    public void setup() {
        Process process = new Process("Proces 1", null, true);
        List<Process> processes = new ArrayList<>();
        processes.add(process);
    }

    @After
    public void verify() { }

    @Configuration
    static class ProcessServiceTestContextConfiguration {

        @Bean
        public ProcessService processService() {
            return new ProcessServiceImpl();
        }

        @Bean
        public ProcessRepository processRepository() {
            return Mockito.mock(ProcessRepository.class);
        }
    }
}
