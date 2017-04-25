var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
var action_url_search_documents_api, action_url_display_document, action_url_download_document, total_pages;
$(document).ready(function () {
    $('#pagination-demo').twbsPagination({
        totalPages: "3",
        visiblePages: "3",
        onPageClick: function (event, page) {
            $('#page-content').text('Page ' + page);
        }
    });

//    $('#visible-pages-example').twbsPagination({
//        totalPages: 35,
//        visiblePages: 10
//    });

//    $('.sync-pagination').twbsPagination({
//        totalPages: 20,
//        onPageClick: function (evt, page) {
//            $('#sync-example-page-content').text('Page ' + page);
//        }
//    });
});
function search(value) {
    $.ajax({
        type: "GET",
        url: action_url_search_documents_api,
        data: {query: value},
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        dataType: 'json',
        success: function (data) {
            var documents = "";
            for (var i = 0; i < data.length; i++) {
                documents += '<ul class="list-group">' +
                        '<li class="list-group-item clearfix">' +
                        '<a class="btn btn-default pull-right" href="' + action_url_download_document + '/' + data[i].id + '" title="Download">' +
                        '<span class="icon_folder_download"></span> Download file</a>' +
                        '<a class="btn btn-default pull-right" href="' + action_url_display_document + '/' + data[i].id + '" target="_blank" title="View file">' +
                        '<span class="icon_folder-open"></span> View file</a>' +
                        '<h3 class="list-group-item-heading">' + data[i].fileName + '</h3>';
                for (var j = 0; j < data[i].descriptors.length; j++) {
                    documents += '<p class="list-group-item-text">' +
                            '<strong>' + data[i].descriptors[j].descriptorKey + ': </strong>' +
                            data[i].descriptors[j].valueAsString + '</p>';
                }
                documents += ' </li> </ul>';
            }
            $("#documents").html(documents);
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
function hideMessage() {
    $("#message-box-container").hide();
}