<%@page contentType="text/html" pageEncoding="UTF-8"%>
<section class="panel" id="activity-info" style="display: none;">
    <header class="panel-heading tab-bg-primary">
        <ul class="nav nav-tabs">
            <li class="active">
                <a data-toggle="tab" href="#inputList">Input documents</a>
            </li>
            <li class="">
                <a data-toggle="tab" href="#outputList">Output documents</a>
            </li>
        </ul>
    </header>
    <div class="panel-body">
        <div class="tab-content">
            <div id="inputList" class="tab-pane active">
                <table class="table" id="table-inputList">
                    <tbody></tbody>
                </table>
            </div>
            <div id="outputList" class="tab-pane">
                <table class="table" id="table-outputList">
                    <tbody></tbody>
                </table>
            </div>
        </div>
    </div>
</section>