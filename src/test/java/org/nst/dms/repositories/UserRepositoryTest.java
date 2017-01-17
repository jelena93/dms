/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.repositories;

import java.util.ArrayList;
import static junit.framework.Assert.assertEquals;

import java.util.List;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nst.dms.config.AppConfig;
import org.nst.dms.domain.Company;
import org.nst.dms.domain.Role;
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
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User user;

    @Before
    public void setupData() {
        Company company = new Company("Soko Štark d.o.o. Beograd", "100002799", "07026447", "Bulevar Peka Dapčevića 29, Voždovac, Beograd", null);
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        user = new User("Ana", "Licina", "hachiko93", "hachiko93", company, roles);
    }

    @After
    public void tearDown() { }

    @Test
    public void login() {
        User user = userRepository.login(this.user.getUsername(), this.user.getPassword());
        assertEquals(this.user.getName(), user.getName());
        assertEquals(this.user.getSurname(), user.getSurname());
        assertEquals(this.user.getUsername(), user.getUsername());
        assertEquals(this.user.getPassword(), user.getPassword());
        assertEquals(this.user.getCompany(), user.getCompany());
        assertEquals(this.user.getRoles(), user.getRoles());
    }
}
