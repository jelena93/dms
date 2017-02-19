var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
var action_url_show_company;
var action_url_search_companies_api;
var selectedCompanyId = null;
function search(name) {
    $.ajax({
        type: "GET",
        url: action_url_search_companies_api,
        data: {name: name},
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        dataType: 'json',
        success: function (data) {
            $("#table-companies tbody").html('');
            for (var i = 0; i < data.length; i++) {
                var link = '<div class="btn-group"><a class="btn btn-success" href="' + action_url_show_company + '/' + data[i].id + '"><i class="icon_check_alt2"></i></a></div>';
                $('#table-companies tbody').
                        append('<tr><td>' + data[i].id + '</td><td>' + data[i].name + '</td><td>' +
                                data[i].pib + '</td><td>'
                                + data[i].identificationNumber + '</td><td>' +
                                data[i].headquarters
                                + '</td><td>' + link + ' </td></tr>');
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
