var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
function search(name) {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/companies/search",
        data: {query: kriterijumPretrage},
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        dataType: 'json',
        success: function (data) {
            var clanovi = JSON.parse(JSON.stringify(data.clanovi));
            $("#table-companies tbody").remove();
            for (var i = 0; i < clanovi.length; i++) {
                $('#clanovi').
                        append('<tr><td>' + clanovi[i].brojClana + '</td><td>' +
                                clanovi[i].ime + '</td><td>'
                                + clanovi[i].prezime + '</td><td>' +
                                clanovi[i].email + '</td><td>' +
                                clanovi[i].kontakt + '</td><td>' + clanovi[i].adresa
                                + '</td><td><a href="/karateklub/trener/clan/' +
                                clanovi[i].brojClana + '">Detalji</a></td></tr>');
            }
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}