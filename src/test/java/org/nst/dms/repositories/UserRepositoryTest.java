/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.repositories;

import static junit.framework.Assert.assertEquals;



import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nst.dms.config.AppConfig;
import org.nst.dms.config.WebConfig;
import org.nst.dms.domain.User;
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
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeClass
    public static void setUpClass() throws Exception {
//        try {
//            System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
//                    "org.apache.naming.java.javaURLContextFactory");
//            System.setProperty(Context.URL_PKG_PREFIXES,
//                    "org.apache.naming");
//            Context initCtx = new InitialContext();
//        Context envCtx = (Context) initCtx.lookup("java:comp/env");
//        DataSource ds = (DataSource) envCtx.lookup("jdbc/dms");
//
//            initCtx.bind("java:comp/env/jdbc/dms", ds);
//        } catch (NamingException ex) {
//            Logger.getLogger(UserRepositoryTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @After
    public void tearDown() { }

    @Test
    public void login() {
        User user = userRepository.findOne(this.user.getUsername());
        assertEquals(this.user.getName(), user.getName());
        assertEquals(this.user.getSurname(), user.getSurname());
        assertEquals(this.user.getUsername(), user.getUsername());
        assertEquals(this.user.getPassword(), user.getPassword());
        assertEquals(this.user.getCompany(), user.getCompany());
        assertEquals(this.user.getRoles(), user.getRoles());
    }
}
