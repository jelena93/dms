<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="_csrf" content="${_csrf.token}"/>
        <meta name="_csrf_header" content="${_csrf.headerName}"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <tiles:insertAttribute name="head" />
        <tiles:importAttribute name="title" scope="request"/>
        <sec:authentication var="user" property="principal" scope="request"/>
        <tiles:importAttribute name="action_url_add_process"/>
        <tiles:importAttribute name="action_url_add_document"/>
        <tiles:importAttribute name="action_url_add_activity"/>
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
                    <tiles:insertAttribute name="message" />
                    <tiles:insertAttribute name="site_content" />
                </section>
            </section>
            <!--main content end-->
        </section>
        <!-- container section start -->
    </body>
</html>
