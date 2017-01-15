<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>            
<div class="row">
    <div class="col-lg-12">
        <section class="panel">
            <header class="panel-heading"> ${title}</header>
            <div class="panel-body">
                <div class="form">
                    <form class="form-validate form-horizontal " id="register_form" method="get" action="${pageContext.request.contextPath}/${action_url_add_user}">
                        <div class="form-group ">
                            <label for="fullname" class="control-label col-lg-2">Name <span class="required">*</span></label>
                            <div class="col-lg-10">
                                <input class=" form-control" id="fullname" name="name" type="text" />
                            </div>
                        </div>
                        <div class="form-group ">
                            <label for="address" class="control-label col-lg-2">Surname <span class="required">*</span></label>
                            <div class="col-lg-10">
                                <input class=" form-control" id="address" name="surname" type="text" />
                            </div>
                        </div>
                        <div class="form-group ">
                            <label for="username" class="control-label col-lg-2">Username <span class="required">*</span></label>
                            <div class="col-lg-10">
                                <input class="form-control " id="username" name="username" type="text" />
                            </div>
                        </div>
                        <div class="form-group ">
                            <label for="password" class="control-label col-lg-2">Password <span class="required">*</span></label>
                            <div class="col-lg-10">
                                <input class="form-control " id="password" name="password" type="password" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="password" class="control-label col-lg-2">Roles <span class="required">*</span></label>
                            <div class="col-lg-10">
                                <select multiple class="form-control" name="roles" id="roles">
                                    <c:forEach var="role" items="${roles}">
                                        <option value="${role}">${role}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group ">
                            <label for="username" class="control-label col-lg-2">Company</label>
                            <div class="col-lg-10">
                                <input class="form-control" name="company" id="company"type="text" onkeyup="search(this.value)" />
                                <br/>
                                <section class="panel">
                                    <table class="table table-striped table-advance table-hover" id="table-companies">
                                        <tbody>
                                            <tr>
                                                <th><i class="icon_profile"></i> Name</th>
                                                <th><i class="icon_calendar"></i> Pib</th>
                                                <th><i class="icon_mail_alt"></i> Identification number</th>
                                                <th><i class="icon_pin_alt"></i> Headquarters</th>
                                                <th><i class="icon_cogs"></i> Action</th>
                                            </tr>
                                            <c:forEach var="c" items="${companies}">
                                                <tr>
                                                    <td>${c.name}</td>
                                                    <td>${c.pib}</td>
                                                    <td>${c.identificationNumber}</td>
                                                    <td>${c.headquarters}</td>
                                                    <td>
                                                        <div class="btn-group">
                                                            <a class="btn btn-success"><i class="icon_check_alt2"></i></a>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </section>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-lg-offset-2 col-lg-10">
                                <button class="btn btn-primary" type="submit">Add user</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </section>
    </div>
</div>
</section>
</div>
</div>
<!-- jquery validate js -->
<script src="<c:url value="/resources/js/jquery.validate.min.js" />"></script>
<script src="<c:url value="/resources/js/typeahead.js" />"></script>
<script src="<c:url value="/resources/js/searchCompanies.js" />"></script>
<!-- custom form validation script for this page-->
<script src="<c:url value="/resources/js/form-validation-script.js" />"></script>