/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.service;

import org.nst.dms.domain.User;

/**
 *
 * @author Jelena
 */
public interface UserService {

    User login(String username, String password);
    User save(User user);
    User findOne(String username);
}