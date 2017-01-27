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
        Company company = new Company("Soko Stark d.o.o Beograd", "011111111", "01111111", "Vozdovac, Beograd");
        companyService.save(company);
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        User user = new User("Pera", "Peric", "admin", "admin", null, roles);
        userService.save(user);
        roles.add(Role.USER);
        roles.add(Role.UPLOADER);
        user = new User("Zika", "Zikic", "asd", "asd", company, roles);
        userService.save(user);
        DocumentType documentType = new DocumentType("Racun");
        documentType = documentTypeService.save(documentType);
        Descriptor descriptor = new Descriptor("broj racuna", null, documentType.getId());
        documentType.getDescriptors().add(descriptor);
        documentTypeService.save(documentType);
        documentType = new DocumentType("Porudzbenica");
        documentType = documentTypeService.save(documentType);
        descriptor = new Descriptor("broj porudzbenice", null, documentType.getId());
        documentType.getDescriptors().add(descriptor);
        documentTypeService.save(documentType);
    }
}
