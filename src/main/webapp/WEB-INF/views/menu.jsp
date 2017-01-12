<%-- 
    Document   : menu
    Created on : Jan 2, 2017, 3:52:45 PM
    Author     : ana
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="resources/css/dashboard.css" rel="stylesheet">
    </head>
    <body>
        <nav class="navbar navbar-inverse navbar-fixed-top">
            <div class="container-fluid">
                <div id="navbar" class="navbar-collapse collapse">
                    <ul class="nav navbar-nav navbar-right">
                        <!--bilo bi dobro da se dinamicki ubacuje kao u gwt-u-->
                        <!--vidi se za uploader-a-->
                        <li><a href="#">Add document</a></li>
                        <!--vidi se za user-a i admina (admin dodaje company koja je prakticno proces)-->
                        <li><a href="#">Add new process</a></li>
                        <!--vidi se za admina-->
                        <li><a href="#">Add user to company</a></li>
                        <!--vidi se za sve koji imaju vise od jedne role-->
                        <li><a href="#">Roles</a></li>
                        <li><a href="#">Logout</a></li>
                    </ul>
                    <form class="navbar-form navbar-right">
                        <input type="text" class="form-control" placeholder="Search processes...">
                    </form>
                </div>
            </div>
        </nav>
    </body>
</html>
