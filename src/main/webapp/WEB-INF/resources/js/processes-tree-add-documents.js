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
            $("#register_form").show();
        } else {
            $("#register_form").hide();
            data.instance.deselect_node(data.node, true);
        }
    });
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
