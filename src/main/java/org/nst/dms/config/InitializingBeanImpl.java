/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.nst.dms.domain.Activity;
import org.nst.dms.domain.Company;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.DescriptorType;
import org.nst.dms.domain.Document;
import org.nst.dms.domain.DocumentType;
import org.nst.dms.domain.Role;
import org.nst.dms.domain.User;
import org.nst.dms.domain.Process;
import org.nst.dms.services.CompanyService;
import org.nst.dms.services.DocumentService;
import org.nst.dms.services.DocumentTypeService;
import org.nst.dms.services.ProcessService;
import org.nst.dms.services.UserService;
import org.nst.dms.elasticsearch.indexing.DocumentIndexer;
import org.nst.dms.elasticsearch.indexing.ElasticClient;
import org.nst.dms.services.DescriptorTypeService;
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
    DescriptorTypeService descriptorTypeService;
    @Autowired
    DocumentTypeService documentTypeService;
    @Autowired
    ProcessService processService;
    @Autowired
    DocumentService documentService;
    @Autowired
    boolean insertValuesInDB;
    @Autowired
    boolean createAndAddInIndex;
    @Autowired
    ElasticClient elasticClient;
    @Autowired
    DocumentIndexer documentIndexer;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (insertValuesInDB) {
            // zadata kompanija
            DescriptorType descriptorTypeInteger = new DescriptorType(Integer.class);
            DescriptorType descriptorTypeDouble = new DescriptorType(Double.class);
            DescriptorType descriptorTypeDate = new DescriptorType(Date.class);

            descriptorTypeInteger = descriptorTypeService.save(descriptorTypeInteger);
            descriptorTypeDouble = descriptorTypeService.save(descriptorTypeDouble);
            descriptorTypeDate = descriptorTypeService.save(descriptorTypeDate);
            Company company = new Company("Silab d.o.o", "011111111", "01111111", "Vozdovac, Beograd");
            companyService.save(company);

            Process test = new Process("test", null, true);
            Activity testAct = new Activity("Test act");
            DocumentType documentType = new DocumentType("test doc type");
            documentType = documentTypeService.save(documentType);
            documentType.getDescriptors().add(new Descriptor("Broj test", documentType.getId(), descriptorTypeInteger));
            documentType.getDescriptors().add(new Descriptor("Datum test", documentType.getId(), descriptorTypeDate));
            documentType = documentTypeService.save(documentType);
            testAct.getInputListDocumentTypes().add(documentType);
            testAct.getOutputListDocumentTypes().add(documentType);
            test.getActivityList().add(testAct);
            company.getProcesses().add(test);

            //zadati procesi i aktivnosti
            Process prodaja = new Process("Prodaja", null, false);
            Process nabavka = new Process("Nabavka", null, false);
            Process skladistenje = new Process("Skladistenje i oprema", null, false);
            Process finansije = new Process("Regulisanje finansija", null, false);

            company.getProcesses().add(prodaja);
            company.getProcesses().add(nabavka);
            company.getProcesses().add(skladistenje);
            company.getProcesses().add(finansije);

            prodaja = processService.save(prodaja);
            nabavka = processService.save(nabavka);
            skladistenje = processService.save(skladistenje);
            finansije = processService.save(finansije);

            Process katalog = new Process("Formiranje i slanje kataloga", prodaja, true);
            katalog.getActivityList().add(new Activity("Formiranje kataloga"));
            katalog.getActivityList().add(new Activity("Slanje kataloga"));

            Process narudzbenica = new Process("Prijem narudzbenice", prodaja, false);
            Process otprema = new Process("Formiranje naloga za otpremu", prodaja, false);

            Process profaktura = new Process("Formiranje profakture", prodaja, true);
            profaktura.getActivityList().add(new Activity("Provera raspolozivosti"));
            profaktura.getActivityList().add(new Activity("Formiranje profakture"));
            profaktura.getActivityList().add(new Activity("Slanje profakture"));

            processService.save(prodaja);
            company.getProcesses().add(katalog);
            company.getProcesses().add(narudzbenica);
            company.getProcesses().add(otprema);
            company.getProcesses().add(profaktura);

            Process katalogP = new Process("Prijem kataloga", nabavka, false);
            Process narucivanje = new Process("Narucivanje", nabavka, false);

            Process profakturaP = new Process("Prijem profakture", nabavka, true);
            profakturaP.getActivityList().add(new Activity("Prijem profakture"));
            profakturaP.getActivityList().add(new Activity("Formiranje naloga za placanje"));

            processService.save(nabavka);
            company.getProcesses().add(katalogP);
            company.getProcesses().add(narucivanje);
            company.getProcesses().add(profakturaP);

            Process zalihe = new Process("Izvestavanje o stanju na zalihama", skladistenje, true);
            zalihe.getActivityList().add(new Activity("Formiranje izvestaja o stanju na zalihama"));
            zalihe.getActivityList().add(new Activity("Formiranje naloga za nabavku"));

            Process otpremanje = new Process("Otpremanje robe", skladistenje, true);
            otpremanje.getActivityList().add(new Activity("Azuriranje stanja gotovih proizvoda i formiranje otpremnice"));
            otpremanje.getActivityList().add(new Activity("Isporucivanje robe"));

            Process prijem = new Process("Projem robe", skladistenje, true);
            prijem.getActivityList().add(new Activity("Prijem fakture"));
            prijem.getActivityList().add(new Activity("Prijem otpremnice"));
            prijem.getActivityList().add(new Activity("Azuriranje stanja gotovih proizvoda i formiranje prijemnice"));
            prijem.getActivityList().add(new Activity("Overa otpremnice dobavljaca"));

            Process nalogZaNabavku = new Process("Formiranje naloga za nabavku", skladistenje, false);
            processService.save(skladistenje);
            company.getProcesses().add(zalihe);
            company.getProcesses().add(otpremanje);
            company.getProcesses().add(prijem);
            company.getProcesses().add(nalogZaNabavku);

            Process obrada = new Process("Obrada izvoda stanja na racunu", finansije, true);
            obrada.getActivityList().add(new Activity("Evidentiranje izvoda stanja sa racuna"));
            obrada.getActivityList().add(new Activity("Evidentiranje uplate"));
            obrada.getActivityList().add(new Activity("Evidentiranje isplate"));
            obrada.getActivityList().add(new Activity("Obrada isplate"));

            Process uplata = new Process("Uplate profakture dobavljaca", finansije, true);
            uplata.getActivityList().add(new Activity("Slanje naloga za prenos"));
            uplata.getActivityList().add(new Activity("Formiranje naloga za prenos"));
            uplata.getActivityList().add(new Activity("Evidentiranje overenih naloga za prenos"));

            processService.save(finansije);
            company.getProcesses().add(obrada);
            company.getProcesses().add(uplata);

            company.getProcesses().add(prodaja);
            company.getProcesses().add(nabavka);
            company.getProcesses().add(skladistenje);
            company.getProcesses().add(finansije);
            companyService.save(company);

            //zadata dokumenta
            documentType = new DocumentType("Nalog za placanje");
            documentType = documentTypeService.save(documentType);

            Descriptor descriptor = new Descriptor("Broj naloga", documentType.getId(), descriptorTypeInteger);
            documentType.getDescriptors().add(descriptor);
            descriptor = new Descriptor("Suma", documentType.getId(), descriptorTypeDouble);
            documentType.getDescriptors().add(descriptor);
            descriptor = new Descriptor("Datum", documentType.getId(), descriptorTypeDate);
            documentType.getDescriptors().add(descriptor);
            documentTypeService.save(documentType);

            documentType = new DocumentType("Otpremnica dobavljaca");
            documentType = documentTypeService.save(documentType);
            descriptor = new Descriptor("Broj otpremnice", documentType.getId(), descriptorTypeInteger);
            documentType.getDescriptors().add(descriptor);
            descriptor = new Descriptor("Datum", documentType.getId(), descriptorTypeDate);
            documentType.getDescriptors().add(descriptor);
            documentTypeService.save(documentType);

            //users
            User sinisa = new User("Sinisa", "Vlajic", "admin", "admin", null, new ArrayList<>(Arrays.asList(new Role[]{Role.ADMIN})));
            userService.save(sinisa);

            User milos = new User("Milos", "Milic", "milos", "milic", company, new ArrayList<>(Arrays.asList(new Role[]{Role.USER})));
            userService.save(milos);

            User voja = new User("Voja", "Stanojevic", "voja", "voja", company, new ArrayList<>(Arrays.asList(new Role[]{Role.UPLOADER})));
            userService.save(voja);

            User dule = new User("DUULE", "SAVIC!", "dules", "dules", company, new ArrayList<>(Arrays.asList(new Role[]{Role.ADMIN, Role.USER, Role.UPLOADER})));
            userService.save(dule);

        }
        if (createAndAddInIndex) {
            documentIndexer.deleteDocumentIndexes();
            documentIndexer.createDocumentIndexIfNotExists();
        }
    }
}
