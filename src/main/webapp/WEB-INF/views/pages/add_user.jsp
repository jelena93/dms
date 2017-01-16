<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>           
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="row">
    <div class="col-lg-12">
                ${poruka}
        <section class="panel">
            <header class="panel-heading"> ${title}</header>
            <div class="panel-body">
                <div class="form">
                    <form class="form-validate form-horizontal " id="register_form" method="POST" action="${pageContext.request.contextPath}/${action_url_add_user}">
                        <div class="form-group ">
                            <label for="name" class="control-label col-lg-2">Name <span class="required">*</span></label>
                            <div class="col-lg-10">
                                <input class=" form-control" id="name" name="name" type="text" />
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
                            <label for="roles" class="control-label col-lg-2">Roles <span class="required">*</span></label>
                            <div class="col-lg-10">
                                <select multiple class="form-control" name="roles" id="roles">
                                    <c:forEach var="role" items="${roles}">
                                        <option value="${role}">${role}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group ">
                            <label for="company" class="control-label col-lg-2">Company</label>
                            <div class="col-lg-10">
                                <jsp:include page="search_companies.jsp"/>
                            </div>
                        </div>
                        <input type="hidden"  name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <div class="form-group">
                            <div class="col-lg-offset-2 col-lg-10">
                                <button class="btn btn-primary" type="submit">${title}</button>
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
<!-- custom form validation script for this page-->
<script src="<c:url value="/resources/js/form-validation-script.js" />"></script>