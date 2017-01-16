var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
function showDescriptors() {
    var docType = document.getElementById("docType").value;
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/dms/api/document-type/name",
        data: {name: docType},
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        dataType: 'json',
        success: function (data) {
//            var companies = JSON.parse(JSON.stringify(data.clanovi));
            
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}