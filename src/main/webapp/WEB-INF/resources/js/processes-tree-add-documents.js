var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
var selectedNode = null;
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
            $("#btn-add-document").show();
        } else {
            $("#activity-info").hide();
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
        error: function (e) {
            showMessage(e, "alert-danger");
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
    $("#activity-info").show();
}
function showFormAddDocument() {
    $("#activity-info").hide();
    $("#form-document").show();
}
function onSubmitForm() {
    if (selectedNode !== null) {
        $("#activityID").val(selectedNode.id);
        return true;
    }
    return false;
}
function showMessage(data, messageType) {
    $("#message-box").removeClass("alert-success");
    $("#message-box").removeClass("alert-danger");
    $("#message-box").addClass(messageType);
    $("#message-text").html(data);
    $("#message-box-container").show();
}
$('#register_form').on('keypress', function (e) {
    return e.which !== 13;
});
