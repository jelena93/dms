<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>            
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<tiles:importAttribute name="action_url_add_process"/>
<div class="row">
    <div class="col-lg-12">
        <section class="panel">
            <header class="panel-heading"> ${title} for ${company.name}</header>
            <div class="panel-body">
                <div class="col-lg-5">
                    <tiles:insertAttribute name="process-tree" />
                    <button class="btn btn-primary" type="button" id="btn-add-process" onclick="addProcess()" disabled>Add process</button>
                    <button class="btn btn-primary" type="button" id="btn-add-activity" onclick="addActivity()" disabled>Add activity</button>
                </div>
                <div class="col-lg-7">
                    <tiles:insertAttribute name="process" />
                </div>
            </div>
        </section>
    </div>
</div>
<script src="<c:url value="/resources/js/processes-tree.js" />"></script>
<script>getProcessesForAddProcess();</script>
