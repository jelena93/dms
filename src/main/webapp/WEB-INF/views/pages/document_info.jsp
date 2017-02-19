<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<tiles:importAttribute name="action_url_download_document" ignore="true"/>
<tiles:importAttribute name="action_url_display_document" ignore="true"/>
<div id="profile" >
    <section class="panel">
        <div class="bio-graph-heading">
            <a target='_blank' href='${pageContext.request.contextPath}/${action_url_display_document}/${document.id}'>${document.fileName}</a>
        </div>
        <div class="panel-body bio-graph-info">
            <h1>Document details</h1>
            <div class="row">
                <div class="bio-row">
                    <p><strong>Id: </strong> ${document.id}</p>
                </div>
            </div>
            <h4>Document descriptors</h4>
            <div class="row">
                <c:forEach var="desc" items="${document.descriptors}">
                    <div class="bio-row">
                        <p><strong> ${desc.descriptorKey}: </strong> ${desc.value}</p>
                    </div>
                </c:forEach>
            </div>
            <a class='btn btn-default pull-right' href='${pageContext.request.contextPath}/${action_url_download_document}/${document.id}' title='Download'>
                <span class='icon_cloud-download'></span> Download file</a>
        </div>
    </section>
</div>