var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
var action_url_document_type_api;
function showDescriptors() {
    var docType = document.getElementById("docType").value;
    $.ajax({
        type: "GET",
        url: action_url_document_type_api,
        data: {id: docType},
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        dataType: 'json',
        success: function (data) {
            var descriptors = "";
            for (var i = 0; i < data.length; i++) {
                var placeholder = "Enter " + data[i].descriptorKey;
                if (data[i].value === null) {
                    if (data[i].descriptorType.paramClass === "java.util.Date") {
                        placeholder += " in format " + data[i].date_FORMAT;
                    }
                    descriptors += '<div class="form-group"> <label for="' + data[i].id
                            + '" class="control-label col-lg-4">' + data[i].descriptorKey
                            + '<span class="required">*</span></label><div class="col-lg-8"> ' +
                            '<input type="text" class="form-control descriptors" name="' + data[i].descriptorKey + '" id="' + data[i].id + '" placeholder="' + placeholder + '" required></div></div>';
                }
            }
            $('#descriptors').html(descriptors);
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