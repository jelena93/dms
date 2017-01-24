/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers.exceptions;

import org.nst.dms.config.security.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Jelena
 */
@Controller
public class AccessDeniedController {

    @RequestMapping(path = "/403", method = RequestMethod.GET)
    public ModelAndView accessDenied(Authentication authentication) {
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        return new ModelAndView("error", "error", new CustomException("You don't have permission to access this page as "
                + user.getActiveRole().name(), "403"));
    }
}
