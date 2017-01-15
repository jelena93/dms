/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.config;

import java.util.ArrayList;
import java.util.List;
import org.nst.dms.domain.Role;
import org.nst.dms.domain.User;
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

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        User user = new User("Pera", "Peric", "admin", "admin", null, roles);
        userService.save(user);
        roles.add(Role.USER);
        roles.add(Role.UPLOADER);
        user = new User("Zika", "Zikic", "asd", "asd", null, roles);
        userService.save(user);
    }
}
