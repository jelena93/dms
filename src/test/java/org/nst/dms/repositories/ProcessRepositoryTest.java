/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.repositories;

import static junit.framework.Assert.assertEquals;

import java.util.List;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nst.dms.config.AppConfig;
import org.nst.dms.domain.Process;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Hachiko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
public class ProcessRepositoryTest {

    @Autowired
    private ProcessRepository processRepository;
    private Process process;

    @Before
    public void setupData() {
        process = new Process("Nabavka cokoladnih bananica", null, true, null, null);
    }

    @After
    public void tearDown() { }

    @Test
    public void search() {
        List<Process> processes = processRepository.search(this.process.getName());
        for (Process process : processes) {
            assertEquals(this.process.getId(), process.getId());
            assertEquals(this.process.getName(), process.getName());
            assertEquals(this.process.getParent(), process.getParent());
            assertEquals(this.process.isPrimitive(), process.isPrimitive());
            assertEquals(this.process.getInputList(), process.getInputList());
            assertEquals(this.process.getOutputList(), process.getOutputList());
        }
    }
}
