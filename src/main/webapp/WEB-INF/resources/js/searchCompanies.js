var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
var action_type_companies_search;
var action_url_show_company;
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
            console.log(data);
            $("#table-companies tbody").html('');
            for (var i = 0; i < data.length; i++) {
                var link = "";
                if (action_type_companies_search === "set") {
                    link = '<div class="btn-group"><a class="btn btn-success" onclick="setCompany('
                            + data[i].id + ')"><i class="icon_check_alt2"></i></a></div>';
                }
                if (action_type_companies_search === "show") {
                    link = '<div class="btn-group"><a class="btn btn-success" href="' + action_url_show_company + '/' + data[i].id + '"><i class="icon_check_alt2"></i></a></div>';
                }
                $('#table-companies tbody').
                        append('<tr><td>' + data[i].id + '</td><td>' + data[i].name + '</td><td>' +
                                data[i].pib + '</td><td>'
                                + data[i].identificationNumber + '</td><td>' +
                                data[i].headquarters
                                + '</td><td>' + link + ' </td></tr>');
            }
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}
function setCompany(id) {
    $("#company").val(id);
    $("#company").attr("readonly", "readonly");
    $("#btn-remove").show();
}
function removeCompany() {
    $("#company").val('');
    $("#company").removeAttr("readonly");
    $("#btn-remove").hide();
}