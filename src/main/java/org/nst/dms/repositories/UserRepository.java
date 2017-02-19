/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.repositories;

import java.util.List;
import org.nst.dms.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jelena
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByCompanyId(Long companyId);
}
