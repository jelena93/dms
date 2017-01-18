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
import static junit.framework.Assert.assertEquals;
import org.junit.Test;
import org.nst.dms.repositories.ProcessRepository;
import org.nst.dms.service.ProcessService;
import org.nst.dms.domain.Process;
import org.nst.dms.service.impl.ProcessServiceImpl;

/**
 *
 * @author Hachiko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ProcessServiceTest {
    @Autowired
    private ProcessService processService;
    @Autowired
    private ProcessRepository processRepository;
    @Before
    public void setup() {
        Process process = new Process("Proces 1", null, true, null, null);
        List<Process> processes = new ArrayList<>();
        processes.add(process);
        
        Mockito.when(processRepository.search("Proces 1")).thenReturn(processes);
    }

    @After
    public void verify() { }

    @Test()
    public void testSearch() {
        List<Process> processes = processService.search("Proces 1");
        for (Process process : processes) {
            assertEquals("Proces 1", process.getName());
//            assertEquals("100002799", process.getPib());
//            assertEquals("07026447", process.getIdentificationNumber());
//            assertEquals("Bulevar Peka Dapčevića 29, Voždovac, Beograd", process.getHeadquarters());
        }
    }

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
