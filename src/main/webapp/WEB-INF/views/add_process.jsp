<%-- 
    Document   : add_process
    Created on : Jan 2, 2017, 4:13:50 PM
    Author     : ana
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Process</title>
        <jsp:include page = "menu.jsp" />
        <link href="resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="resources/css/signin.css" rel="stylesheet">
        <script src="resources/js/jquery.min.js"></script>
        <script src="resources/js/bootstrap.min.js"></script>
    </head>
    <body>
        <form class="form-signin" method="POST" action="/index.html">
            <h2 class="form-signin-heading">Add New Process</h2>
            <br>
            <input type="text" name = "processName" class="form-control" placeholder="Process name"  required autofocus>
            <br>
            <div class="dropdown">
                <button class="btn btn-default btn-lg dropdown-toggle form-control" type="button" data-toggle="dropdown">Choose parent...
                    <span class="caret"></span></button>
                <ul class="dropdown-menu">
                    <!--dinamicki-->
                    <li><a href="#">About Us</a></li>
                </ul>
            </div>
            <div class="checkbox primitive">
                <label>
                    <input type="checkbox" value="primitive"> Primitive
                </label>
            </div>
            <br>
            <br>
            <br>
            <button class="btn btn-lg btn-primary btn-block" type="submit">Add Process</button>
        </form>
    </body>
</html>