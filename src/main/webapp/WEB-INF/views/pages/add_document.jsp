<%-- 
    Document   : add_process
    Created on : Jan 2, 2017, 4:13:50 PM
    Author     : ana
--%>
<form class="form-signin" method="POST" action="${pageContext.request.contextPath}/${action_url_add_document}">
    <h2 class="form-signin-heading">${title}</h2>
    <br>
    <div class="dropdown">
        <button class="btn btn-default btn-lg dropdown-toggle form-control" type="button" data-toggle="dropdown">Choose type...
            <span class="caret"></span></button>
        <ul class="dropdown-menu">
            <!--dinamicki-->
            <li><a href="#">About Us</a></li>
        </ul>
    </div>
    <br>
    <br>
    <br>
    <button class="btn btn-lg btn-primary btn-block" type="submit">Add Document</button>
</form>