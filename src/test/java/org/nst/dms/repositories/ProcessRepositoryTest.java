/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.repositories;

import static junit.framework.Assert.assertEquals;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nst.dms.config.AppConfig;
import org.nst.dms.config.WebConfig;
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
@ContextConfiguration(classes = {AppConfig.class, WebConfig.class})
@Transactional
public class ProcessRepositoryTest {

    @Autowired
    private ProcessRepository processRepository;
    private Process process;

    @BeforeClass
    public static void setUpClass() throws Exception {
        try {
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.naming.java.javaURLContextFactory");
            System.setProperty(Context.URL_PKG_PREFIXES,
                    "org.apache.naming");
            InitialContext initCtx = new InitialContext();

            initCtx.createSubcontext("java:");
            initCtx.createSubcontext("java:/comp");
            initCtx.createSubcontext("java:/comp/env");
            initCtx.createSubcontext("java:/comp/env/jdbc");

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/dms");

            initCtx.bind("java:/comp/env/jdbc/dms", ds);
        } catch (NamingException ex) {
            Logger.getLogger(ProcessRepositoryTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void search() {
        List<Process> processes = processRepository.search(this.process.getName());
        for (Process process : processes) {
            assertEquals(this.process.getName(), process.getName());
            assertEquals(this.process.getParent(), process.getParent());
            assertEquals(this.process.isPrimitive(), process.isPrimitive());
            assertEquals(this.process.getActionList(), process.getActionList());
        }
    }
}
