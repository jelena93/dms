<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>            
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<tiles:importAttribute name="action_url_search_documents_api" />
<tiles:importAttribute name="action_url_download_document" />
<tiles:importAttribute name="action_url_display_document"/>
<div class="input-group">
    <div class="input-group-addon">
        <select class="" id="filter" name="filter">
            <option value="name">Search by Name</option>
            <option value="descriptorKey">Search by Descriptor key</option>
            <option value="descriptorValue">Search by Descriptor value</option>
            <option value="content">Search by Content</option>
        </select>
    </div>
    <input type="text" class="form-control" onkeyup="search(this.value)" placeholder="Search ..." aria-describedby="filter">
</div>
<script language=javascript>
    var action_url_search_documents_api = "${pageContext.request.contextPath}/${action_url_search_documents_api}";
    var action_url_display_document = "${pageContext.request.contextPath}/${action_url_display_document}";
    var action_url_download_document = "${pageContext.request.contextPath}/${action_url_download_document}";
</script>
<section class="panel">
    <div class="panel panel-default">
        <div class="panel-heading">Documents</div>
        <div class="panel-body" id="documents">
            <c:forEach var="doc" items="${documents}">
                <ul class="list-group">
                    <li class="list-group-item clearfix">
                        <a class='btn btn-default pull-right' href="${pageContext.request.contextPath}/${action_url_download_document}/${doc.id}" title='Download'>
                            <span class='icon_folder-download'></span> Download file</a>
                        <a class='btn btn-default pull-right' href="${pageContext.request.contextPath}/${action_url_display_document}/${doc.id}" target='_blank' title='View file'>
                            <span class='icon_folder-open'></span> View file</a>
                        <h3 class="list-group-item-heading">${doc.fileName}</h3>
                        <c:forEach var="desc" items="${doc.descriptors}">
                            <p class="list-group-item-text"><strong>${desc.descriptorKey}: </strong>${desc.value}</p>
                        </c:forEach>
                    </li> 
                </ul>
            </c:forEach>
        </div>
    </div>
</section>
<script src="<c:url value="/resources/js/searchDocuments.js" />"></script>
