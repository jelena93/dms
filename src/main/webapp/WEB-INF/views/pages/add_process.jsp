<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>            
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<tiles:importAttribute name="action_url_add_process"/>
<!-- Modal -->
<div class="modal fade" id="modal" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Are you sure?</h4>
            </div>
            <div class="modal-body">
                <p>Setting process to primitive will delete all child nodes of this process, are you sure?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-dismiss="modal" onclick="edit()">Yes</button>
                <button type="button" class="btn btn-default" data-dismiss="modal" onclick="cancelEdit()">No</button>
            </div>
        </div>
    </div>
</div>
<!--Model end-->
<div class="row">
    <div class="col-lg-12">
        <section class="panel">
            <header class="panel-heading"> ${title} for company: ${company.name}</header>
            <div class="panel-body">
                <div class="col-lg-4">
                    <tiles:insertAttribute name="process-tree" />
                    <button class="btn btn-primary" type="button" id="btn-add-process" onclick="addProcess()" style="display: none;">Add process</button>
                    <button class="btn btn-primary" type="button" id="btn-add-activity" onclick="addActivity()" style="display: none;" disabled>Add activity</button>
                </div>
                <div class="col-lg-8">
                    <tiles:insertAttribute name="process" />
                </div>
            </div>
        </section>
    </div>
</div>
<script src="<c:url value="/resources/js/processes-tree.js" />"></script>
<script>getProcessesForAddProcess();</script>
