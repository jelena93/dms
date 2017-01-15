<%-- 
    Document   : add_process
    Created on : Jan 2, 2017, 4:13:50 PM
    Author     : ana
--%>

<form class="form-signin" method="POST" action="save_company">
    <h2 class="form-signin-heading">Add New Company</h2>
    <br>
    <input type="text" name = "name" class="form-control" placeholder="Company name"  required autofocus>
    <br>
    <input type="number" name = "pib" class="form-control" placeholder="PIB"  required>
    <br>
    <input type="number" name = "identificationNumber" class="form-control" placeholder="Identification Number"  required>
    <br>
    <input type="text" name = "headquarters" class="form-control" placeholder="Headquarters"  required>
    <br>
    <button class="btn btn-lg btn-primary btn-block" type="submit">Add Company</button>
</form>