<%-- 
    Document   : login
    Created on : Jan 2, 2017, 3:29:41 PM
    Author     : ana
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SignIn</title>
    <body>
        <link href="resources/css/bootstrap.min.css" rel="stylesheet">
        <link href="resources/css/scripts/signin.css" rel="stylesheet">
    </head>
    <div class="container">
        <form class="form-signin" method="POST" action="login">
            <h2 class="form-signin-heading">Please log in</h2>
            <input type="text" name = "username" id="inputEmail" class="form-control" placeholder="Username"  required autofocus>
            <br>
            <input type="password" name = "password" id="inputPassword" class="form-control" placeholder="Password" required>
            <div class="checkbox">
                <label>
                    <input type="checkbox" value="remember-me"> Remember me
                </label>
                <label>
                      <a class = "forgot-pass" href="#">Forgot password</a> 
                </label>
            </div>
            <button class="btn btn-lg btn-primary btn-block" type="submit">Log in</button>
        </form>
    </div> 
</body>
</html>
