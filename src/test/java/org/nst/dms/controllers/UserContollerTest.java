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
import org.nst.dms.domain.Role;
import org.nst.dms.domain.User;
import org.nst.dms.services.CompanyService;
import org.nst.dms.services.UserService;
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
public class UserContollerTest {

    @Autowired
    private UserController userController;
    @Autowired
    private UserService userService;

    private User user;

    @Before
    public void setup() throws Exception {
        Company company = new Company("Soko Štark d.o.o. Beograd", "100002799", "07026447", "Bulevar Peka Dapčevića 29, Voždovac, Beograd", null);
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        user = new User("Ana", "Licina", "hachiko93", "hachiko93", company, roles);
        Mockito.when(userService.findOne("hachiko93")).thenReturn(user);
    }

    @Test
    public void testAddGetRequest() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.userController).build();
        mockMvc.perform(get("/add")).andExpect(status().isOk()).andExpect(redirectedUrl("add_user"));
    }
    
    @Test
    public void testAdd() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(this.userController).build();
        mockMvc.perform(post("/add").param("username", "hachiko93").param("password", "hachiko93").param("name", "Ana")
        .param("surname", "Licina").param("company", "1")).andExpect(status().isOk()).andExpect(redirectedUrl("add_user"));
    }

    @After
    public void verify() {
        Mockito.verify(this.userService, VerificationModeFactory.times(1)).findOne("hachiko93");
        Mockito.reset();
    }

    @Configuration
    static class UserControllerTestConfiguration {

        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }

        @Bean
        public UserController userController() {
            return new UserController();
        }
    }
}
