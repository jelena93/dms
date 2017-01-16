<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row">
    <div class="col-lg-12">
        <h3 class="page-header">${title}</h3>
        <ol class="breadcrumb">
            <li><a href="${pageContext.request.contextPath}">Home</a></li>
                <c:choose>
                    <c:when test="">
                    <li>Dashboard</li>	
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="b" items="${user.breadcrumbs}">
                        <li>${b}</li>	
                        </c:forEach>
                    </c:otherwise>
                </c:choose>    
        </ol>
    </div>
</div>