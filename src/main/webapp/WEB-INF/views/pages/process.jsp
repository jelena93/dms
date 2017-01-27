<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>           
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<tiles:importAttribute name="label_name"/>
<tiles:importAttribute name="process_message"/>
<tiles:importAttribute name="input_name"/>

<div class="form-group ">
    <label for="processId" class="control-label col-lg-2">${label_name} </label>
    <div class="col-lg-5">
        <input class="form-control" id="processId" name="${input_name}" type="text" onkeyup="searchProcesses(this.value)" placeholder="Search processes"/>
        <br/>
        <div class="well">
            <h5>${process_message}</h5>
            <div id="processes">
            </div>
        </div>
    </div>
    <div id="profile" class="col-lg-5" style="display: none">
        <section class="panel">
            <div id = "process_info" class="bio-graph-heading" >
            </div>
            <div class="panel-body bio-graph-info">
                <h1>Process details</h1>
                <div class="row">
                    <div class="bio-row">
                        <p><span>Id: </span> <div id="process_id"></div> </p>
                    </div>
                    <div class="bio-row">
                        <p><span>Name: </span> <div id="process_name"></div></p>
                    </div>
                    <div class="bio-row">
                        <p><span>Parent: </span> <div id="process_parent"></div></p>
                    </div>
                    <div class="bio-row">
                        <p><span>Primitive: </span> <div id="process_primitive"></div></p>
                    </div>
                    <div class="bio-row">
                        <h1 id="input_header">Input Documents</h1>
                        <div id="input_list"></div>
                    </div>
                    <div class="bio-row">
                        <h1 id="output_header">Output Documents</h1>
                        <div id="output_list"></div>
                    </div>
                </div>
            </div>
        </section>
        <section>
            <div class="row">                                              
            </div>
        </section>
    </div>
</div>