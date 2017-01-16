/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.config;

import java.util.ArrayList;
import java.util.List;
import org.nst.dms.domain.Company;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.DocumentType;
import org.nst.dms.domain.Role;
import org.nst.dms.domain.User;
import org.nst.dms.repositories.DocumentTypeRepository;
import org.nst.dms.service.CompanyService;
import org.nst.dms.service.DocumentTypeService;
import org.nst.dms.service.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jelena
 */
public class InitializingBeanImpl implements InitializingBean {

    @Autowired
    UserService userService;
    @Autowired 
    CompanyService companyService;
    @Autowired
    DocumentTypeService documentTypeService;
    
    @Override
    public void afterPropertiesSet() throws Exception {
//        List<Role> roles = new ArrayList<>();
//        roles.add(Role.ADMIN);
//        User user = new User("Pera", "Peric", "admin", "admin", null, roles);
//        userService.save(user);
//        roles.add(Role.USER);
//        roles.add(Role.UPLOADER);
//        user = new User("Zika", "Zikic", "asd", "asd", null, roles);
//        userService.save(user);
//        
//        Company company = new Company("Soko Stark d.o.o", "011111111", "01111111", "Vozdovac, Beograd");
//        companyService.save(company);
//        
//        List<Descriptor> descriptors = new ArrayList<>();
//        descriptors.add(new Descriptor("broj racuna", "11111111111"));
//        DocumentType documentType = new DocumentType("Racun", descriptors);
//        documentTypeService.save(documentType);
//        descriptors.clear();
//        descriptors.add(new Descriptor("broj porudzbenice", "11111111111"));
//        documentType = new DocumentType("Porudzbenica", descriptors);
//        documentTypeService.save(documentType);
    }
}
