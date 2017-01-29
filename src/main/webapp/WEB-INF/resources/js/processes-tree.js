var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
var selectedNode = null;
var actionEdit = false;
function getProcessesForAddProcess() {
    $('#processes').jstree({
        'core': {
            'data': {
                'url': '/dms/api/processes',
                'data': function (node) {
                    return {'id': node.id};
                }
            },
            "multiple": false,
            "themes": {
                "variant": "large"
            },
            "plugins": ["wholerow"]
        }}).on('activate_node.jstree', function (e, data) {
        if (selectedNode !== null && selectedNode.id === data.node.id) {
            data.instance.deselect_node(data.node, true);
            selectedNode = null;
            $('#info').hide();
            $("#btn-add-activity").prop("disabled", true);
            $("#btn-add-process").prop("disabled", true);
            return;
        }
        selectedNode = data.node.original;
        console.log(selectedNode);
        if (data.node.original.primitive) {
            getInfo(data.node.id, "/dms/api/process/" + selectedNode.id, false);
            $("#btn-add-activity").prop("disabled", false);
            $("#btn-add-process").prop("disabled", true);
        } else if (data.node.original.activity) {
            getInfo(data.node.id, "/dms/api/activity/" + selectedNode.id, true);
            $("#btn-add-activity").prop("disabled", true);
            $("#btn-add-process").prop("disabled", true);
            data.instance.deselect_node(data.node, true);
        } else {
            getInfo(data.node.id, "/dms/api/process/" + selectedNode.id, false);
            $("#btn-add-activity").prop("disabled", true);
            $("#btn-add-process").prop("disabled", false);
        }
//        if (data.node.original.primitive) {
//            data.instance.deselect_node(data.node, true);
//        } else {
//            if (selectedNode.id === data.node.id) {
//                data.instance.deselect_node(data.node, true);
//                selectedNode = null;
//            } else {
//                selectedNode = data.node;
//            }
//        }
    });
}
function getProcessesForAddDocument() {
    $('#processes').jstree({
        'core': {
            'data': {
                'url': '/dms/api/processes',
                'data': function (node) {
                    return {'id': node.id};
                }
            },
            "multiple": false,
            "themes": {
                "variant": "large"
            },
            "plugins": ["wholerow"]
        }}).on('activate_node.jstree', function (e, data) {
        getInfo(data.node.id);
        if (!data.node.original.primitive) {
            data.instance.deselect_node(data.node, true);
        } else {
            if (selectedNode.id === data.node.id) {
                data.instance.deselect_node(data.node, true);
                selectedNode = null;
            } else {
                selectedNode = data.node;
            }
        }
    });
}

function getInfo(id, url, isActivity) {
    $.ajax({
        type: "GET",
        url: url,
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        dataType: 'json',
        success: function (data) {
            $('#id').val(data.id);
            $('#name').val(data.name);
            actionEdit = false;
            if (isActivity) {
                $("#primitive").hide();
            } else {
                $("#primitive").show();
                $('#primitive').prop("checked", data.primitive);
            }
            $('#info').show();
        },
        error: function (e) {
            showMessage(e, "alert-danger");
        }
    });
}
function onSubmitForm() {
    if (selectedNode !== null) {
        $("#processId").val(selectedNode.id);
    } else {
        $("#processId").val(null);
    }
    return true;
}
function onSubmitFormAddDocument() {
    if (selectedNode !== null) {
        $("#processId").val(selectedNode.id);
    } else {
        $("#processId").val(null);
    }
    return true;
}
function enableForm() {
    if (!actionEdit) {
        $('#name').prop("disabled", false);
        $('#primitive').prop("disabled", false);
        actionEdit = true;
        $("#btn-edit").text("Save");
    } else {
        actionEdit = false;
        var params = Array();
        params["id"] = selectedNode.id;
        params["name"] = $("#name").val();
        var url = "";
        if (selectedNode.activity) {
            url = "/dms/api/activity/edit";
        } else {
            url = "/dms/api/process/edit";
            params["primitive"] = $("#primitive").prop('checked');
        }
        edit(url, params);
    }
}
function edit(url, params) {
    $.ajax({
        type: "POST",
        url: url,
        data: params,
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        dataType: 'json',
        success: function (data) {
            showMessage(data, "alert-success");
        },
        error: function (e) {
            showMessage(e, "alert-danger");

        }
    });
}
function addActivity() {

}
function addProcess() {}

function showMessage(data, messageType) {
    $("#message-box").removeClass("alert-success");
    $("#message-box").removeClass("alert-danger");
    $("#message-box").addClass(messageType);
    $("#message-text").html(data);
    $("#message-box-container").show();
}