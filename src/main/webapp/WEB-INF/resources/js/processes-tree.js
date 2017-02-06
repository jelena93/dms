var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
var selectedNode = null;
var canEdit = false;
var modeEdit = "edit";
var modeAddProcess = "addProcess";
var modeAddActivity = "addActivity";
var mode = modeEdit;
function getProcessesForAddProcess() {
    $('#processes').bind('ready.jstree', function (e, data) {
        $("#btn-add-process").show();
        $("#btn-add-activity").show();
    }).jstree({
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
        if (selectedNode !== null && selectedNode.id === data.node.original.id) {
            data.instance.deselect_node(data.node, true);
            selectedNode = null;
            $('#info').hide();
            $("#btn-add-activity").prop("disabled", true);
            $("#btn-add-process").prop("disabled", false);
            return;
        }
        selectedNode = data.node.original;
        if (data.node.original.primitive) {
            getInfo("/dms/api/process/" + selectedNode.id, false);
            $("#btn-add-activity").prop("disabled", false);
            $("#btn-add-process").prop("disabled", true);
        } else if (data.node.original.activity) {
            getInfo("/dms/api/activity/" + selectedNode.id, true);
            $("#btn-add-activity").prop("disabled", true);
            $("#btn-add-process").prop("disabled", true);
        } else {
            getInfo("/dms/api/process/" + selectedNode.id, false);
            $("#btn-add-activity").prop("disabled", true);
            $("#btn-add-process").prop("disabled", false);
        }
    });
}
function getInfo(url, isActivity) {
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
            canEdit = false;
            if (isActivity) {
                $("#form-primitive").hide();
            } else {
                $("#form-primitive").show();
                $('#primitive').prop("checked", data.primitive);
            }
            disableForm();
            $("#message-box-container").hide();
            $('#info').show();
        },
        error: function (e) {
            showMessage(e, "alert-danger");
        }
    });
}
function checkData() {
    if (mode === modeEdit) {
        $("#btn-edit").prop("type", "button");
        if (!canEdit) {
            $('#name').prop("disabled", false);
            $('#primitive').prop("disabled", false);
            canEdit = true;
            $("#btn-edit").text("Save");
        } else {
            canEdit = false;
            var params = {};
            params.id = selectedNode.id;
            params.name = $("#name").val();
            var url = "";
            if (selectedNode.activity) {
                url = "/dms/api/activity/edit";
            } else {
                url = "/dms/api/process/edit";
                params.primitive = $("#primitive").prop('checked');
            }
            edit(url, params);
        }
    } else if (mode === modeAddProcess) {
        $("#isActivity").val(false);
        $("#register_form").submit();
    } else if (mode === modeAddActivity) {
        $("#isActivity").val(true);
        $("#register_form").submit();
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
        success: function (data) {
            showMessage(data, "alert-success");
            canEdit = false;
            disableForm();
            $('#processes').jstree(true).refresh();
        },
        error: function (e) {
            showMessage(e, "alert-danger");
        }
    });
}
function addActivity() {
    mode = modeAddActivity;
    showFormForAdding(true);
}
function addProcess() {
    mode = modeAddProcess;
    showFormForAdding(false);
}

function disableForm() {
    $("#name").prop("disabled", true);
    $("#primitive").prop("disabled", true);
    $("#btn-edit").text("Edit");
    mode = modeEdit;
}
function showMessage(data, messageType) {
    $("#message-box").removeClass("alert-success");
    $("#message-box").removeClass("alert-danger");
    $("#message-box").addClass(messageType);
    $("#message-text").html(data);
    $("#message-box-container").show();
}
function showFormForAdding(isActivity) {
    $("#name").prop("disabled", false);
    $("#name").val("");
    if (isActivity) {
        $("#form-primitive").hide();
    } else {
        $("#form-primitive").show();
        $("#primitive").prop("disabled", false);
        $('#primitive').prop("checked", false);
    }
    $("#btn-edit").text("Add");
    $('#info').show();
}
$('#register_form').on('keypress', function (e) {
    return e.which !== 13;
});
