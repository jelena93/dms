<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>            
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="row">
    <div class="col-lg-12">
        ${poruka}
        <section class="panel">
            <header class="panel-heading"> ${title}</header>
            <div class="panel-body">
                <div class="form">
                    <form class="form-validate form-horizontal " id="register_form" onsubmit="return onSubmitForm()" method="POST" action="${pageContext.request.contextPath}/${action_url_add_process}">
                        <div class="form-group ">
                            <label for="name" class="control-label col-lg-2">Name <span class="required">*</span></label>
                            <div class="col-lg-10">
                                <input class=" form-control" id="name" name="name" type="text" />
                            </div>
                        </div>
                        <div class="form-group ">
                            <label for="password" class="control-label col-lg-2">Primitive </label>
                            <div class="col-lg-10">
                                <input  type="checkbox" style="width: 20px" class="checkbox form-control" id="primitive" name="primitive" value="true"/>
                            </div>
                        </div>
                        <input type="hidden"  name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <%@include file="process.jsp" %>
                        <div class="form-group">
                            <div class="col-lg-offset-10 col-lg-2">
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