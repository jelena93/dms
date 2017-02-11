<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>            
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<tiles:importAttribute name="action_url_add_document"/>
<div class="row">
    <div class="col-lg-12">
        <section class="panel">
            <header class="panel-heading"> ${title} for company: ${company.name}</header>
            <div class="panel-body">
                <div class="col-lg-4">
                    <tiles:insertAttribute name="process-tree" />
                    <button class="btn btn-primary" type="button" id="btn-add-document" onclick="showFormAddDocument()" style="display: none;">Add document</button>
                </div>
                <div class="col-lg-8">
                    <tiles:insertAttribute name="activity_info" />
                    <tiles:insertAttribute name="document" />
                </div>
            </div>
        </section>
    </div>
</div>
<script src="<c:url value="/resources/js/processes-tree-add-documents.js" />"></script>
<script src="<c:url value="/resources/js/setDocumentTypeDescriptors.js" />"></script>
<script>getProcessesForAddDocument();</script>