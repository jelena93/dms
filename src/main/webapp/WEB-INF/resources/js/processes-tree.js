var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
var selectedNode = null;
var canEdit = false;
var modeEdit = "edit";
var modeAddProcess = "addProcess";
var modeAddActivity = "addActivity";
var mode = modeEdit;
var isSure = false;
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
            $('#id').val(selectedNode);
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
            var params = {};
            params.id = selectedNode.id;
            params.name = $("#name").val();
            if (params.name === "") {
                $("#register_form").submit();
                return;
            }
            var url = "";
            if (selectedNode.activity) {
                url = "/dms/api/activity/edit";
            } else {
                url = "/dms/api/process/edit";
                params.primitive = $("#primitive").prop('checked');
            }
            console.log("Params: " + params.id + " " + params.name + " " + params.primitive);
            if (!isSure && !selectedNode.activity && !selectedNode.primitive && params.primitive) {
                showPopUp("Setting process to primitive will delete all child nodes of this process, are you sure?");
            } else if (!isSure && !selectedNode.activity && selectedNode.primitive && !params.primitive) {
                showPopUp("Setting process to non primitive will delete all documents from activities, are you sure?");
            } else {
                canEdit = false;
                edit(url, params);
            }
        }
    } else if (mode === modeAddProcess) {
        $("#isActivity").val(false);
        $("#register_form").submit();
    } else if (mode === modeAddActivity) {
        $("#isActivity").val(true);
        $("#register_form").submit();
    }
}
function showPopUp(text) {
    $("#process-text-warning").text(text);
    $("#modal").modal({
        show: true,
        backdrop: 'static',
        keyboard: false
    });
}
function sendEditRequest() {
    isSure = true;
    $('#modal').modal('hide');
    checkData();
}
function cancelEdit() {
    isSure = false;
    $('#modal').modal('hide');
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
            isSure = false;
            disableForm();
            $('#processes').jstree(true).refresh();
            selectedNode.name = params["name"];
            if (!selectedNode.activity) {
                selectedNode.primitive = params["primitive"];
                if (selectedNode.primitive) {
                    $("#btn-add-activity").prop("disabled", false);
                    $("#btn-add-process").prop("disabled", true);
                } else {
                    $("#btn-add-activity").prop("disabled", true);
                    $("#btn-add-process").prop("disabled", false);
                }
            }
        },
        error: function (e) {
            console.log(e)
//            showMessage(e, "alert-danger");
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
    $("#btn-edit").text("Edit");
    hideErrorForName();
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
    hideErrorForName();
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
function hideErrorForName() {
    if ($("label[for='name']").hasClass("error")) {
        $("label[for='name']").eq(1).hide();
        $("label[for='name']").eq(1).removeClass("error");
        $("#name").removeClass("error");
    }
}
$('#register_form').on('keypress', function (e) {
    return e.which !== 13;
});
