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
import org.nst.dms.domain.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;
import org.nst.dms.domain.Role;
import org.nst.dms.domain.User;
import org.nst.dms.repositories.UserRepository;
import org.nst.dms.services.impl.UserServiceImpl;

/**
 *
 * @author Hachiko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    private User user;

    @Before
    public void setup() {
        Company company = new Company("Soko Štark d.o.o. Beograd", "100002799", "07026447", "Bulevar Peka Dapčevića 29, Voždovac, Beograd", null);
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        user = new User("Ana", "Licina", "hachiko93", "hachiko93", company, roles);
        Mockito.when(userService.findOne("hachiko93")).thenReturn(user);
    }

    @After
    public void verify() {
        Mockito.verify(userRepository, VerificationModeFactory.times(1)).findOne(Mockito.anyString());
        Mockito.reset(userRepository);
    }

    @Test()
    public void testLogin(){
        User user = userService.findOne("hachiko93");
        assertEquals("Ana", user.getName());
        assertEquals("Licina", user.getSurname());
    }
    
    @Configuration
    static class UserServiceTestContextConfiguration {

        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }

        @Bean
        public UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }
    }
}
