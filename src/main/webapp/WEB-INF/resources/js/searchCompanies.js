var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
function search(name) {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/dms/api/companies/search",
        data: {name: name},
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        dataType: 'json',
        success: function (data) {
            var companies = JSON.parse(JSON.stringify(data.clanovi));
            $("#table-companies tbody").remove();
            for (var i = 0; i < companies.length; i++) {
                $('#table-companies').
                        append('<tr><td>' + companies[i].id + '</td><td>' + companies[i].name + '</td><td>' +
                                companies[i].pib + '</td><td>'
                                + companies[i].identificationNumber + '</td><td>' +
                                companies[i].headquarters 
                                + '</td><td> <div class="btn-group" <a class="btn btn-success"'
                                +'<i class="icon_check_alt2"></i></a></div></td></tr>');
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