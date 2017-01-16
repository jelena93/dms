var Script = function () {

    $().ready(function () {
        // validate the comment form when it is submitted
        $("#feedback_form").validate();

        // validate signup form on keyup and submit
        $("#register_form").validate({
            rules: {
                name: {
                    required: true
                },
                surname: {
                    required: true
                },
                username: {
                    required: true
                },
                password: {
                    required: true
                },
                pib: {
                    required: true
                },
                identificationNumber: {
                    required: true
                },
                headquarters: {
                    required: true
                },
                document: {
                    required: true
                },
                roles: "required"
            },
            messages: {
                name: {
                    required: "Please enter a name."
                },
                surname: {
                    required: "Please enter a surname."
                },
                username: {
                    required: "Please enter a username."
                },
                password: {
                    required: "Please provide a password."
                },
                roles: {
                    required: "Please select a role."
                },
                pib: {
                    required: "Please provide a pib."
                },
                identificationNumber: {
                    required: "Please provide an identification number."
                },
                headquarters: {
                    required: "Please provide headquarters."
                },
                document: {
                    required: "Please provide document."
                }
            }
        });

        //code to hide topic selection, disable for demo
        var newsletter = $("#newsletter");
        // newsletter topics are optional, hide at first
        var inital = newsletter.is(":checked");
        var topics = $("#newsletter_topics")[inital ? "removeClass" : "addClass"]("gray");
        var topicInputs = topics.find("input").attr("disabled", !inital);
        // show when newsletter is checked
        newsletter.click(function () {
            topics[this.checked ? "removeClass" : "addClass"]("gray");
            topicInputs.attr("disabled", !this.checked);
        });
    });


}();