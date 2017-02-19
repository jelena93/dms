/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.service;

import java.util.List;
import org.nst.dms.domain.Company;
import org.nst.dms.domain.User;

/**
 *
 * @author Jelena
 */
public interface UserService {
    User save(User user);
    User findOne(String username);
    List<User> findByCompanyId(Long companyId);
}