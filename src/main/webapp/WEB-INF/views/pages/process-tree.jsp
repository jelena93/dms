<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<tiles:importAttribute name="process_message"/>
<div class="well">
    <h5>${process_message}</h5>
    <div id="processes">
    </div>
</div>