<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="profile" >
    <section class="panel">
        <div class="bio-graph-heading">
            ${company.name}, ${company.headquarters}
        </div>
        <div class="panel-body bio-graph-info">
            <h1>Company details</h1>
            <div class="row">
                <div class="bio-row">
                    <p><strong>Id: </strong> ${company.id}</p>
                </div>
                <div class="bio-row">
                    <p><strong>Name: </strong> ${company.name}</p>
                </div>
                <div class="bio-row">
                    <p><strong>Identification number </strong> ${company.identificationNumber}</p>
                </div>
                <div class="bio-row">
                    <p><strong>Pib: </strong> ${company.pib}</p>
                </div>
                <div class="bio-row">
                    <p><strong>Headquarters: </strong> ${company.headquarters}</p>
                </div>
            </div>
        </div>
    </section>
    <section>
        <div class="row">                                              
        </div>
    </section>
</div>
<!--collapse start-->
<div class="panel-group m-bot20" id="accordion">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4 class="panel-title">
                <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#collapse">
                    Users
                </a>
            </h4>
        </div>
        <div id="collapse" class="panel-collapse collapse">
            <div class="panel-body">
                <c:choose>
                <c:when test="${users.isEmpty()}" ><h4 style="text-align: center;">There are no users for this company</h4></c:when>
                <c:otherwise>
                    <table class="table">
                        <thead>
                            <tr>
                                <th><i class="icon_profile"></i>Username</th>
                                <th><i class="icon_profile"></i>Name</th>
                                <th><i class="icon_profile"></i>Surname</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="u" items="${users}">
                                <tr>
                                    <td>${u.username}</td>
                                    <td>${u.name}</td>
                                    <td>${u.surname}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
<!--collapse end-->