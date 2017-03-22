/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 *
 * @author Jelena
 */
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(CustomException.class)
    public ModelAndView handleCustomException(CustomException ex) {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("error", ex);
        return mv;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleUrlNotFound(NoHandlerFoundException ex) {
        ModelAndView mv = new ModelAndView("error");
        CustomException customException = new CustomException("Requested page: " + ex.getHttpMethod() + " " + ex.getRequestURL() + " doesn't exist", "404");
        mv.addObject("error", customException);
        return mv;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleMissingParameter(MissingServletRequestParameterException ex) {
        ModelAndView mv = new ModelAndView("error");
        CustomException customException = new CustomException("Missing request parameter" + ex.getParameterName() + " type " + ex.getParameterType(), "400");
        mv.addObject("error", customException);
        return mv;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        ModelAndView mv = new ModelAndView("error");
        CustomException customException = new CustomException(ex.getMessage(), "500");
        mv.addObject("error", customException);
        ex.printStackTrace();
        return mv;
    }

    @ExceptionHandler(NullPointerException.class)
    public ModelAndView handleNullPointerException(NullPointerException ex) {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("error", new CustomException("Nullpointer exception " + ex, "500"));
        ex.printStackTrace();
        return mv;
    }
}
