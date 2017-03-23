var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
var selectedNode = null;
var checked = false;
var isSure = false;
var action_url_processes_api, action_url_show_activity_api, action_url_document_validation_api,
        action_url_show_document_info, action_url_download_document, action_url_display_document;
function getProcessesForAddDocument() {
    $('#processes').jstree({
        'core': {
            'data': {
                'url': action_url_processes_api,
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
        if (data.node.original.activity) {
            if (selectedNode !== null && selectedNode.id === data.node.original.id) {
                reset(data);
            } else {
                selectedNode = data.node.original;
                getActivityInfo(selectedNode.id);
            }
        } else {
            reset(data);
        }
    });
}
function getActivityInfo(id) {
    $.ajax({
        type: "GET",
        url: action_url_show_activity_api + "/" + id,
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        dataType: 'json',
        success: function (data) {
            displayActivityInfo(data);
        },
        error: function (request, status, error) {
            try {
                var message = jQuery.parseJSON(request.responseText);
                showMessage(message.messageText, message.messageType);
            } catch (e) {
                console.log(request);
            }
        }
    });
}
function displayActivityInfo(activity) {
    $("#table-inputList tbody").html('');
    $("#table-outputList tbody").html('');
    for (var i = 0; i < activity.inputList.length; i++) {
        var inputList = "<tr><td><a target='_blank' href='" + action_url_display_document + "/" + activity.inputList[i].id + "'>" + activity.inputList[i].fileName + "</a></td><td>" +
                "<a class='btn btn-default' href='" + action_url_download_document + "/" + activity.inputList[i].id +
                "' title='Download'><span class='icon_cloud-download'></span> Download file</a></td><td><div class='btn-group'><a class='btn btn-success' target='_blank' title='Show document' href='"
                + action_url_show_document_info + "/" + activity.inputList[i].id + "'><i class='icon_check_alt2'></i></a></div></td></tr>";
        $('#table-inputList tbody').append(inputList);
    }

    for (var i = 0; i < activity.outputList.length; i++) {
        var outputList = "<tr><td><a target='_blank' href='" + action_url_display_document + "/" + activity.outputList[i].id + "'>" + activity.outputList[i].fileName
                + "</a></td><td>" + "<a class='btn btn-default' href='" + action_url_download_document + "/" + activity.outputList[i].id +
                "' title='Download'><span class='icon_cloud-download'></span> Download file</a></td><td><div class='btn-group'><a class='btn btn-success' target='_blank' title='Show document' href='"
                + action_url_show_document_info + "/" + activity.outputList[i].id + "'><i class='icon_check_alt2'></i></a></div></td></tr>";
        $('#table-outputList tbody').append(outputList);
    }
    $("#form-document").hide();
    $("#activity-info").show();
    $("#btn-add-document").show();
}
function showFormAddDocument() {
    $("#activity-info").hide();
    $("#btn-add-document").hide();
    $("#form-document").show();
}
function onSubmitForm() {
    if ($("#file").val() === "") {
        return false;
    }
    if (checked || isSure) {
        return true;
    }
    validateDocument();
    return false;
}
function validateDocument() {
    if (selectedNode !== null) {
        $("#activityID").val(selectedNode.id);
        var docType = $("#docType").val();
        var data = new FormData();
        data.append("file", $("#file").prop('files')[0]);
        data.append("docType", docType);
        data.append("activityID", selectedNode.id);
        data.append("inputOutput", $("input[name='inputOutput']:checked").val());
        var descriptors = $(".descriptors");
        var sendValidationRequest = true;
        for (var i = 0; i < descriptors.length; i++) {
            data.append([descriptors[i].name], descriptors[i].value);
            if (descriptors[i].value === "") {
                sendValidationRequest = false;
                break;
            }
        }
        if (sendValidationRequest) {
            documentValidation(data);
        }
    }

}
function documentValidation(params) {
    $.ajax({
        type: "POST",
        url: action_url_document_validation_api,
        data: params,
        processData: false,
        enctype: 'multipart/form-data',
        contentType: false,
        dataType: 'json',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
            console.log(data)
            if (data.messageType === "question") {
                if (data.messageAction === "edit") {
                    $("#existingDocumentID").val(data.messageData);
                } else {
                    $("#existingDocumentID").val(null);
                }
                showPopUp(data.messageText);
            } else if (data.messageType === "alert-success") {
                checked = true;
                $("#register_form").submit();
            } else {
                console.log(data);
            }
        },
        error: function (request, status, error) {
            try {
                var message = jQuery.parseJSON(request.responseText);
                showMessage(message.messageText, message.messageType);
            } catch (e) {
                console.log(request);
            }
        }
    });
}

function reset(data) {
    $("#activity-info").hide();
    $("#form-document").hide();
    $("#btn-add-document").hide();
    selectedNode = null;
    data.instance.deselect_node(data.node, true);
}
function showMessage(data, messageType) {
    $("#message-box").removeClass("alert-success");
    $("#message-box").removeClass("alert-danger");
    $("#message-box").addClass(messageType);
    $("#message-text").html(data);
    $("#message-box-container").show();
}
function showPopUp(text) {
    $("#modal-question-text").text(text);
    $("#modal").modal({
        show: true,
        backdrop: 'static',
        keyboard: false
    });
}
function sendRequest() {
    isSure = true;
    $('#modal').modal('hide');
    $("#register_form").submit();
}
function closeModal() {
    isSure = false;
    $("#existingDocumentID").val(null);
    $('#modal').modal('hide');
}