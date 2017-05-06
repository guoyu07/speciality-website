jQuery(document).ready(function () {
    let saveTaxis = jQuery("#save-taxis");
    let strongs = [];
    let regExp = new RegExp("^[0-9]+$");

    //有 taxis input 为非整数时不允许提交表单
    jQuery("input[name='taxis']").each(function () {
        let input = jQuery(this);
        let strong = input.siblings("strong");
        strongs.push(strong);
        input.keyup(function () {
            if (input.val() !== "" && !regExp.test(input.val())) {
                strong.removeClass("hidden");
                saveTaxis.prop("disabled", true);
            } else {
                strong.addClass("hidden");
                let flag = false;
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
        let button = jQuery(this);
        let td = button.parent("td").parent("tr");
        button.click(function () {
            BootstrapDialog.show({
                title: "警告",
                message: "你真的想删除分类 '" + button.attr("data-name").replace(/-/g, "").replace(" ", "") + "' 吗?",
                buttons: [{
                    label: "确定",
                    action: function (dialog) {
                        jQuery.ajax({
                            url: "/api/admin/sort/" + button.attr("data-id"),
                            type: "DELETE"
                        }).done(function () {
                            td.height(td.height());
                            td.empty();
                            td.animate({height: "0"}, "normal", "swing", function () {
                                td.remove();
                            });
                        }).fail(function (data) {
                            if (data.status === 401) {
                                location.reload();
                            }
                        }).always(function () {
                            dialog.close();
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
    });
});
