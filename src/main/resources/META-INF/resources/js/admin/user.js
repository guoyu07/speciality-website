jQuery(document).ready(function () {
    function reloadPage() {
        window.location.reload();
    }

    function disableInputAndAddSpin(input) {
        input.prop("disabled", "disabled");
        input.after("<i class='fa fa-spinner fa-spin'></i>");
    }

    function refreshNotification(email) {
        jQuery("#notification").text("用户 '" + email + "' 已修改于 " + new Date());
    }

    let userTable = jQuery(".user-table");

    jQuery("#create-user-form").validate({
        rules: {
            email: {
                remote: {
                    url: validateUserAPIURL,
                    data: {
                        email: function () {
                            return jQuery("#email").val();
                        }
                    }
                }
            }
        },
        messages: {
            email: {
                remote: "User already exists."
            }
        }
    });

    jQuery(".user-table i.fa-trash").click(function () {
        let parentTr = jQuery(this).parents("tr");
        BootstrapDialog.show({
            title: "警告",
            message: "你真的想删除用户 '" + parentTr.attr("data-nick") + "' 吗? 删除后该用户所有文章将归属 ID 为 1 的用户!",
            buttons: [{
                label: "确定",
                action: function () {
                    jQuery.ajax({
                        type: "DELETE",
                        url: userAPIURL + parentTr.attr("data-id")
                    }).always(function () {
                        window.location.reload();
                    });
                }
            }, {
                label: "取消",
                action: function (dialog) {
                    dialog.close();
                }
            }]
        });
    });

    jQuery(".user-table i.fa-pencil").click(function () {
        jQuery(this).hide();
    });

    jQuery(".user-table i.fa-pencil.edit-nick").click(function () {
        let nick = jQuery(this).siblings("span.nick");
        let value = nick.text();
        nick.html("<input class='form-control editing-nick'/>");
        let input = nick.children("input");
        input.val(value);
        input.attr("old-nick", value);
        input.focus();
    });

    userTable.on("blur", ".editing-nick", function () {
        let input = jQuery(this);
        let span = input.parents("span");

        function afterEdit() {
            span.text(nick);
            span.siblings("i.fa-pencil").show();
        }

        let nick = input.val();
        if (nick === input.attr("old-nick")) {
            afterEdit();
            return;
        }
        disableInputAndAddSpin(input);
        let tr = input.parents("tr");
        jQuery.ajax({
            type: "PATCH",
            url: userAPIURL + tr.attr("data-id"),
            data: {
                nick: nick
            }
        }).done(function () {
            afterEdit();
            refreshNotification(tr.attr("data-email"));
        }).fail(reloadPage);
    });

    jQuery(".user-table i.fa-pencil.edit-password").click(function () {
        let password = jQuery(this).siblings("span.password");
        password.html("<input class='form-control editing-password'/>");
        password.children("input").focus();
    });

    userTable.on("blur", ".editing-password", function () {
        let input = jQuery(this);
        let span = input.parents("span");

        function afterEdit() {
            span.text("●●●●●●●");
            span.siblings("i.fa-pencil").show();
        }

        let password = input.val();
        if (password === "") {
            afterEdit();
            return;
        }
        disableInputAndAddSpin(input);
        let tr = input.parents("tr");
        jQuery.ajax({
            type: "PATCH",
            url: userAPIURL + tr.attr("data-id"),
            data: {
                password: password
            }
        }).done(function () {
            afterEdit();
            refreshNotification(tr.attr("data-email"));
        }).fail(reloadPage);
    });
});
