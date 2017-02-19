/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.service.impl;

import java.util.List;
import org.nst.dms.domain.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.nst.dms.repositories.CompanyRepository;
import org.nst.dms.service.CompanyService;

/**
 *
 * @author Hachiko
 */
@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyRepository companyRepository;
    @Override
    public List<Company> findByNameContainingOrHeadquartersContaining(String name) {
        return companyRepository.findByNameContainingOrHeadquartersContaining(name, name);
    }
    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }
    @Override
    public Company save(Company company) {
        return companyRepository.save(company);
    }
    @Override
    public Company findOne(long companyId) {
        return companyRepository.findOne(companyId);
    }
}
