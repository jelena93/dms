<%-- 
    Document   : add_process
    Created on : Jan 2, 2017, 4:13:50 PM
    Author     : ana
--%>

<form class="form-signin" method="POST" action="${pageContext.request.contextPath}/${action_url_add_process}">
    <h2 class="form-signin-heading">${title}/h2>
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
