var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
var selectedProcessId = null;
$(function () {
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
        }});

    $('#processes').on('activate_node.jstree', function (e, data) {
//        if (data.instance.is_leaf(data.node) && data.node.parent !== "#") {
        if (data.node.original.primitive) {
            data.instance.deselect_node(data.node, true);
        } else {
            selectedProcessId = data.node.id;
        }
    });
});

function searchProcesses(name) {
    $.ajax({
        type: "GET",
        url: "/dms/api/processes/search",
        data: {name: name},
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        dataType: 'json',
        success: function (data) {
            console.log(data);
            $('#processes').jstree(true).settings.core.data = data;
            $('#processes').jstree(true).refresh();
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });

}
function onSubmitForm() {
    if (selectedProcessId !== null) {
        $("#parent").val(selectedProcessId);
    }
    return true;
}