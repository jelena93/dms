/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.repositories;

import java.util.List;
import org.nst.dms.domain.Company;
import org.nst.dms.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jelena
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.username=?1 AND u.password=?2")
    User login(String username, String password);
    @Query("SELECT u FROM User u WHERE u.company=?1")
    List<User> findUsersOfCompany(Company company);
}
