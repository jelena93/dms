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
import org.nst.dms.domain.Company;
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
public class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;
    private Company company;

    @Before
    public void setupData() {
        company = new Company("Soko Štark d.o.o. Beograd", "100002799", "07026447", "Bulevar Peka Dapčevića 29, Voždovac, Beograd", null);
    }

    @After
    public void tearDown() { }

    @Test
    public void search() {
        List<Company> companies = companyRepository.search(this.company.getName());
        for (Company company : companies) {
            assertEquals(this.company.getId(), company.getId());
            assertEquals(this.company.getName(), company.getName());
            assertEquals(this.company.getPib(), company.getPib());
            assertEquals(this.company.getIdentificationNumber(), company.getIdentificationNumber());
            assertEquals(this.company.getHeadquarters(), company.getHeadquarters());
            assertEquals(this.company.getProcesses(), company.getProcesses());
        }
    }
}
