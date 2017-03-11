/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.config.security;

import org.nst.dms.dto.UserDto;
import org.nst.dms.domain.User;
import org.nst.dms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findOne(username);
        if (user == null) {
            throw new UsernameNotFoundException("UserName " + username + " not found");
        }
        UserDto securityUser = new UserDto(user.getUsername(), user.getPassword(), user.getName(),
                user.getSurname(), user.getRoles(), user.getRoles().get(0));
        return securityUser;
    }
}
