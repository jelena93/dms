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
        getProcessInfo(data.node.id);
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

function getProcessInfo(id) {
    $.ajax({
        type: "GET",
        url: "/dms/api/process/" + id,
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        dataType: 'json',
        success: function (data) {
            clearInfo();
            $('#profile').show();
            $('#process_info').html(data.name);
            $('#process_id').html(data.id);
            $('#process_name').html(data.name);
            $('#process_parent').html(data.parent === null ? "/" : data.parent.name);
            $('#process_primitive').html(data.primitive === true ? "YES" : "NO");
            if (!data.primitive) return;
            if (data.inputList.length > 0) {
                $('#input_header').show();
                $('#input_list').html("");
                for (var i = 0; i < data.inputList.length; i++) {
                    var documentNameList = data.inputList[i].url.split("/");
                    var documentName = data.inputList[i].url.split("/")[documentNameList.length - 1];
                    $('#input_list').append('<p><span>Document URL: </span> <a href="' + documentNameList + ' download">' + documentName + '</a></p>');
                }
            }
            if (data.outputList.length > 0) {
                $('#output_header').show();
                $('#output_list').html("");
                for (var i = 0; i < data.outputList.length; i++) {
                    var documentNameList = data.outputList[i].url.split("/");
                    var documentName = data.outputList[i].url.split("/")[documentNameList.length - 1];
                    $('#output_list').append('<p><span>Document URL: </span> <a href="' + documentNameList + ' download">' + documentName + '</a></p>');
                }
            }
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}

function clearInfo() {
    $('#process_info').html("");
    $('#process_id').html("");
    $('#process_name').html("");
    $('#process_parent').html("");
    $('#process_primitive').html("");
    $('#input_header').hide();
    $('#output_header').hide();
    $('#input_list').html("");
    $('#output_list').html("");
}