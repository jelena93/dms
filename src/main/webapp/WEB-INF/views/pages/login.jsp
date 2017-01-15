<%-- 
    Document   : login
    Created on : Jan 2, 2017, 3:29:41 PM
    Author     : ana
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Karate klub | Prijava</title>
        <!-- CSS -->
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet" />
        <link href="<c:url value="/resources/css/font-awesome.min.css" />" rel="stylesheet" />
        <link href="<c:url value="/resources/css/login-form-elements.css" />" rel="stylesheet" />
        <link href="<c:url value="/resources/css/login.css" />" rel="stylesheet" />
        <link rel="shortcut icon" href="<c:url value="/resources/ico/karate.png" />" />
        <script src='<c:url value="/resources/js/jquery-1.11.1.min.js" />'></script>
    </head>
    <body>
        <div class="top-content">
            <div class="inner-bg">
                <div class="container">
                    <div class="row">
                        <div class="col-sm-6 col-sm-offset-3 form-box">
                            <div class="form-top">
                                <div class="form-top-left">
                                    <c:choose>
                                        <c:when test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
                                            <font color="red">
                                            Error:
                                            <br/>
                                            ${SPRING_SECURITY_LAST_EXCEPTION.message}
                                            </font>
                                        </c:when>
                                        <c:otherwise>
                                            <h3>Login</h3>
                                            <p>Enter your username and password:</p>                                        
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="form-top-right">
                                    <i class="fa fa-lock"></i>
                                </div>
                            </div>
                            <div class="form-bottom">
                                <form role="form" action="login" method="post" class="login-form">
                                    <div class="form-group">
                                        <label class="sr-only" for="form-username">Username:</label>
                                        <input type="text" name="username" placeholder="Username" class="form-username form-control" id="form-username">
                                    </div>
                                    <div class="form-group">
                                        <label class="sr-only" for="form-password">Password:</label>
                                        <input type="password" name="password" placeholder="Password" class="form-password form-control" id="form-password">
                                    </div>
                                    <input type="hidden"  name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="btn">Login</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>        
        <script src='<c:url value="/resources/js/bootstrap.min.js" />'></script>
        <script src='<c:url value="/resources/js/jquery.backstretch.min.js" />'></script>
        <script src='<c:url value="/resources/js/scripts.js" />'></script>

    </body>
</html>
