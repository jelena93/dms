var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
var selectedNode = null;
var checked = false;
var isSure = false;
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
        if (data.node.original.activity) {
            selectedNode = data.node.original;
            getActivityInfo(selectedNode.id);
        } else {
            $("#activity-info").hide();
            $("#form-document").hide();
            $("#btn-add-document").hide();
            data.instance.deselect_node(data.node, true);
        }
    });
}
function getActivityInfo(id) {
    $.ajax({
        type: "GET",
        url: "../api/activity/" + id,
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
        var inputList = "<tr><td><a target='_blank' href='document/" + activity.inputList[i].id + "'>" + activity.inputList[i].fileName + "</a></td><td>" +
                "<a class='btn btn-default' href='document/download/" + activity.inputList[i].id +
                "' title='Download'><span class='icon_cloud-download'></span></a></td></tr>";
        $('#table-inputList tbody').append(inputList);
    }

    for (var i = 0; i < activity.outputList.length; i++) {
        var outputList = "<tr><td><a target='_blank' href='document/" + activity.outputList[i].id + "'>" + activity.outputList[i].fileName
                + "</a></td><td>" + "<a class='btn btn-default' href='/document/download/" + activity.outputList[i].id +
                "' title='Download'><span class='icon_cloud-download'></span></a></td></tr>";
        $('#table-outputList tbody').append(outputList);
    }
    $("#form-document").hide();
    $("#activity-info").show();
    $("#btn-add-document").show();
}
function showFormAddDocument() {
    $("#activity-info").hide();
    $("#form-document").show();
}
function onSubmitForm() {
    if ($("#file").val() === "") {
        return false;
    }
    if (checked || isSure) {
        return true;
    }
    checkIfDocumentExists();
    return false;
}
function checkIfDocumentExists() {
    if (selectedNode !== null) {
        $("#activityID").val(selectedNode.id);
        var docType = $("#docType").val();
        var params = {};
        params["docType"] = docType;
        params["activityID"] = selectedNode.id;
        params["inputOutput"] = $("input[name='inputOutput']:checked").val();
        console.log("@@@@" + params["inputOutput"]);
        var descriptors = $(".descriptors");
        for (var i = 0; i < descriptors.length; i++) {
            params[descriptors[i].name] = descriptors[i].value;
            if (descriptors[i].value === "") {
                return;
            }
        }
        documentValidation(params);
    }

}
function documentValidation(params) {
    $.ajax({
        type: "POST",
        url: "/dms/api/documents/validation",
        data: params,
        dataType: 'json',
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        success: function (data) {
            if (data.messageType === "question") {
                $("#existingDocumentID").val(data.messageText);
                showPopUp("Document already exists. Are you sure you want to rewrite the file?");
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
    $('#modal').modal('hide');
}
$('#register_form').on('keypress', function (e) {
    return e.which !== 13;
});
