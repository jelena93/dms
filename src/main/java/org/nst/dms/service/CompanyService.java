/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.service;

import java.util.List;
import org.nst.dms.domain.Company;

/**
 *
 * @author Hachiko
 */
public interface CompanyService {
    List<Company> findByNameContainingOrHeadquartersContaining(String name);
    List<Company> findAll();
    Company save(Company company);
    Company findOne(long companyId);
}
