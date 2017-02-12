<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>           
<div class="form" id="form-document" style="display: none;">
    <form class="form-validate form-horizontal " id="register_form" method="POST" onsubmit="return onSubmitForm()" enctype="multipart/form-data">
        <div class="form-group">
            <label class="control-label col-lg-2" for="inputSuccess">Input or output</label>
            <div class="col-lg-10">
                <div class="radio">
                    <label>
                        <input type="radio" name="inputOutput" id="optionsRadios1" value="input" checked>
                        Input
                    </label>
                </div>
                <div class="radio">
                    <label>
                        <input type="radio" name="inputOutput" id="optionsRadios2" value="output">
                        Output
                    </label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label for="docType" class="control-label col-lg-2">Document type </label>
            <div class="col-lg-10">
                <select class="form-control" name="docType" id="docType" onchange="showDescriptors()">
                    <c:forEach var="docType" items="${documentTypes}">
                        <option value="${docType.id}">${docType.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-group">
            <div class="col-lg-offset-2 col-lg-10">
                <section class="panel">
                    <header class="panel-heading"> Descriptors</header>
                    <div class="panel-body" id="descriptors">
                        <c:forEach var="desc" items="${documentTypes[0].descriptors}">
                            <div class="form-group">
                                <label for="${desc.id}" class="control-label col-lg-4">${desc.descriptorKey} <span class="required">*</span></label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" name="${desc.descriptorKey}" id="${desc.id}" placeholder="Enter ${desc.descriptorKey}" required>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </section>
            </div>
        </div>
        <div class="form-group">
            <label for="file" class="control-label col-lg-2">Document <span class="required">*</span></label>
            <input type="file" id="file" name="file">
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="activityID" id="activityID"/>
        <div class="form-group">
            <div class="col-lg-offset-2 col-lg-10">
                <button class="btn btn-primary" type="submit">${title}</button>
            </div>
        </div>
    </form>
</div>