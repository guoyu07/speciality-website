jQuery(document).ready(function () {
    var saveTaxis = jQuery("#save-taxis");
    var strongs = [];
    var regExp = new RegExp("^[0-9]+$");
    //有 taxis input 为非整数时不允许提交表单
    jQuery("input[name='taxis']").each(function () {
        var input = jQuery(this);
        var strong = input.siblings("strong");
        strongs.push(strong);
        input.keyup(function () {
            if (input.val() != "" && !regExp.test(input.val())) {
                strong.removeClass("hidden");
                saveTaxis.prop("disabled", true);
            } else {
                strong.addClass("hidden");
                var flag = false;
                jQuery.each(strongs, function (index, currentStrong) {
                    if (!currentStrong.hasClass("hidden")) {
                        flag = true;
                    }
                });
                saveTaxis.prop("disabled", flag);
            }
        });
    });

    //点击 删除 按钮时发送 ajax 至后台
    jQuery(".ajax-delete").each(function () {
        var button = jQuery(this);
        var td = button.parent("td").parent("tr");
        button.click(function () {
            jQuery.ajax({
                url: "/api/admin/sort/" + button.attr("data-id"),
                type: "delete",
                success: function () {
                    td.height(td.height());
                    td.empty();
                    td.animate({height: "0"}, "normal", "swing", function () {
                        td.remove();
                    });
                },
                error: function (data) {
                    if (data.status == 401) {
                        window.location.href = loginPage;
                    }
                }
            });
        });
    });
});
