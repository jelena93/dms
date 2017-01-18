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
import org.nst.dms.repositories.CompanyRepository;
import org.nst.dms.service.CompanyService;
import org.nst.dms.service.impl.CompanyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Hachiko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CompanyServiceTest {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyRepository companyRepository;
    @Before
    public void setup() {
        Company company = new Company("Soko Štark d.o.o. Beograd", "100002799", "07026447", "Bulevar Peka Dapčevića 29, Voždovac, Beograd", null);
        List<Company> companies = new ArrayList<>();
        companies.add(company);
        
        Mockito.when(companyRepository.search("Soko Štark d.o.o. Beograd")).thenReturn(companies);
    }

    @After
    public void verify() {
        Mockito.verify(companyRepository, VerificationModeFactory.times(1)).search(Mockito.anyString());
        Mockito.reset(companyRepository);
    }

    @Test()
    public void testSearch(){
        List<Company> companies = companyService.search("Soko Štark d.o.o. Beograd");
        for (Company company : companies) {
            assertEquals("Soko Štark d.o.o. Beograd", company.getName());
            assertEquals("100002799", company.getPib());
            assertEquals("07026447", company.getIdentificationNumber());
            assertEquals("Bulevar Peka Dapčevića 29, Voždovac, Beograd", company.getHeadquarters());
        }
    }
    
    @Configuration
    static class CompanyServiceTestContextConfiguration {

        @Bean
        public CompanyService companyService() {
            return new CompanyServiceImpl();
        }

        @Bean
        public CompanyRepository companyRepository() {
            return Mockito.mock(CompanyRepository.class);
        }
    }
}
