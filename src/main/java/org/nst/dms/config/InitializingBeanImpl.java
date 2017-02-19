/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.config;

import java.util.ArrayList;
import java.util.List;
import org.nst.dms.domain.Activity;
import org.nst.dms.domain.Company;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.DescriptorType;
import org.nst.dms.domain.DocumentType;
import org.nst.dms.domain.Role;
import org.nst.dms.domain.User;
import org.nst.dms.domain.Process;
import org.nst.dms.service.CompanyService;
import org.nst.dms.service.DocumentTypeService;
import org.nst.dms.service.ProcessService;
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
    @Autowired
    ProcessService processService;
    @Autowired
    boolean isTest;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (isTest) {
            Company company = new Company("Soko Stark d.o.o Beograd", "011111111", "01111111", "Vozdovac, Beograd");
            companyService.save(company);

            Process process = new Process("Proces1", null, false);
            company.getProcesses().add(process);
            processService.save(process);
            process = new Process("Dete", process, true);
            process.getActivityList().add(new Activity("Aktivnost 1"));
            processService.save(process);
            company.getProcesses().add(process);
            companyService.save(company);
            process = new Process("Random process", null, true);
            processService.save(process);

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
            DescriptorType descriptorType = new DescriptorType(Integer.class);
            Descriptor descriptor = new Descriptor("broj racuna", documentType.getId(), descriptorType);
            documentType.getDescriptors().add(descriptor);
            documentType = documentTypeService.save(documentType);
            descriptorType = documentType.getDescriptors().get(0).getDescriptorType();
            documentType = new DocumentType("Porudzbenica");
            documentType = documentTypeService.save(documentType);
            descriptor = new Descriptor("broj porudzbenice", documentType.getId(), descriptorType);
            documentType.getDescriptors().add(descriptor);
            documentTypeService.save(documentType);
        }
    }
}
