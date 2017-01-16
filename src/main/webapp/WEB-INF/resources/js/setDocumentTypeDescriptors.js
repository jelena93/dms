var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
function showDescriptors() {
    var docType = document.getElementById("docType").value;
    $.ajax({
        type: "GET",
        url: "/dms/api/document-type",
        data: {id: docType},
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        dataType: 'json',
        success: function (data) {
            console.log(data)
            for (var i = 0; i < data.length; i++) {
                var descriptors = '<div class="form-group"> <label for="' + data[i].id
                        + '" class="control-label col-lg-2">' + data[i].key
                        + '<span class="required">*</span></label><div class="col-lg-10"> ' +
                        '<input type="text" class="form-control" name="' + data[i].key + '" id="' + data[i].id + '" placeholder="Enter '
                        + data[i].key + '"></div></div>';
            }
            $('#descriptors').html(descriptors);
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}