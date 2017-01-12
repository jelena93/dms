/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.service.impl;

import org.nst.dms.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.nst.dms.repositories.UserRepository;
import org.nst.dms.service.UserService;

/**
 *
 * @author Jelena
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User login(String username, String password) {
        return userRepository.login(username, password);
    }

}