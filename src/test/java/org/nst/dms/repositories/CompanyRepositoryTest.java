/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.repositories;

import static junit.framework.Assert.assertEquals;

import java.util.List;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nst.dms.config.AppConfig;
import org.nst.dms.config.WebConfig;
import org.nst.dms.domain.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;
    private Company company;
    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @After
    public void tearDown() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
//        try {
//            System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
//                    "org.apache.naming.java.javaURLContextFactory");
//            System.setProperty(Context.URL_PKG_PREFIXES,
//                    "org.apache.naming");
////
//            ic.createSubcontext("java:");
//            ic.createSubcontext("java:/comp");
//            Context ic = new InitialContext();
//            ic.createSubcontext("java:/comp/env");
//            ic.createSubcontext("java:/comp/env/jdbc");
////
////            // Construct DataSource
////            Context envCtx = (Context) ic.lookup("java:comp/env");
////
////            DataSource ds = (DataSource) envCtx.lookup("jdbc/dms");
//            Context envCtx = (Context) ic.lookup("java:comp/env");
//            DataSource ds = (DataSource) envCtx.lookup("jdbc/dms");
//            ic.bind("java:comp/env/jdbc/dms", ds);
//        } catch (NamingException ex) {
//            //Logger.getLogger(MyDAOTest.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }

    @Test
    public void search() {
        List<Company> companies = companyRepository.findByNameContainingOrHeadquartersContaining(this.company.getName(), this.company.getHeadquarters());
        for (Company company : companies) {
            assertEquals(this.company.getName(), company.getName());
            assertEquals(this.company.getPib(), company.getPib());
            assertEquals(this.company.getIdentificationNumber(), company.getIdentificationNumber());
            assertEquals(this.company.getHeadquarters(), company.getHeadquarters());
            assertEquals(this.company.getProcesses(), company.getProcesses());
        }
    }
}
