$(function () {
    $('input.typeahead').on('typeahead:selected', function (evt, item) {
//        window.location.href = "/" + item.substring(item.indexOf("-") + 1);
    });
    $('input.typeahead-companies').typeahead({
            hint: true,
            highlight: true,
            minLength: 1
        },
        {
            limit: 12,
            async: true,
            source: function (query, processSync, processAsync) {
                return $.ajax({
                    url: "/api/companies/search",
                    type: 'GET',
                    data: {name: query},
                    dataType: 'json',
                    success: function (data) {
                        return processAsync(data);
                    }
                });
            }
        }
    );
});