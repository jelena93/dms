var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
function search(name) {
    $.ajax({
        type: "GET",
        url: "/dms/api/companies/search",
        data: {name: name},
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        dataType: 'json',
        success: function (data) {
            console.log(data)
            $("#table-companies tbody").html('');
            for (var i = 0; i < data.length; i++) {
                $('#table-companies tbody').
                        append('<tr><td>' + data[i].id + '</td><td>' + data[i].name + '</td><td>' +
                                data[i].pib + '</td><td>'
                                + data[i].identificationNumber + '</td><td>' +
                                data[i].headquarters
                                + '</td><td> <div class="btn-group"><a class="btn btn-success" onclick="setCompany('
                                + data[i].id + ')"><i class="icon_check_alt2"></i></a></div></td></tr>');
            }
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}
function setCompany(id) {
    $("#company").val(id);
}