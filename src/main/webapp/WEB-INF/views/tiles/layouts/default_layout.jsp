<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="shortcut icon" href="<c:url value="/resources/img/favicon.png" />">
        <!-- Bootstrap CSS -->    
        <link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
        <!-- bootstrap theme -->
        <link href="<c:url value="/resources/css/bootstrap-theme.css" />" rel="stylesheet">
        <!--external css-->
        <!-- font icon -->
        <link href="<c:url value="/resources/css/elegant-icons-style.css" />" rel="stylesheet" />
        <link href="<c:url value="/resources/css/font-awesome.css" />" rel="stylesheet" />
        <!-- Custom styles -->
        <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/css/style-responsive.css" />" rel="stylesheet">
        <tiles:importAttribute name="title" scope="request"/>
        <tiles:importAttribute name="site_name" scope="request" />
        <tiles:importAttribute name="action_logout_url" scope="request" />
        <tiles:importAttribute name="action_url_add_company" scope="request" ignore="true" />
        <tiles:importAttribute name="action_url_add_process" scope="request" ignore="true"/>
        <sec:authentication var="user" property="principal" scope="request"/>
        <title>${title}</title>
    </head>

    <body>
        <!-- container section start -->
        <section id="container" class="">
            <header class="header dark-bg">
                <tiles:insertAttribute name="header" />
            </header>
            <aside>
                <tiles:insertAttribute name="left_side_menu" />
            </aside>

            <!--main content start-->
            <section id="main-content">
                <section class="wrapper">            
                    <tiles:insertAttribute name="breadcrumbs" />
                    <tiles:insertAttribute name="site_content" />
                </section>
            </section>
            <!--main content end-->
        </section>
        <!-- container section start -->

        <!-- javascripts -->
        <script src="<c:url value="/resources/js/jquery.js" />"></script>
        <!-- bootstrap -->
        <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
        <!-- nice scroll -->
        <script src="<c:url value="/resources/js/jquery.scrollTo.min.js" />"></script>
        <script src="<c:url value="/resources/js/jquery.nicescroll.js" />"></script>
        <!--custome script for all page-->
        <script src="<c:url value="/resources/js/scripts.js" />"></script>

    </body>
</html>
