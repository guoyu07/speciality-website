jQuery(document).ready(function () {
    let articleListTable = jQuery("#article-list-table");

    function reload() {
        location.reload();
    }

    function getSelectVal(articleActionButton) {
        return jQuery(articleActionButton).siblings("select.article-action-select").val();
    }

    function getData(articleActionButton) {
        return jQuery("#article-action-form").serialize() + "&articleAction=" + getSelectVal(articleActionButton);
    }

    function getArticleRowTr(a) {
        return jQuery(a).parents("tr[data-id]");
    }

    function getArticleId(a) {
        return getArticleRowTr(a).attr("data-id");
    }

    function singlePost(articleAction, articleId) {
        jQuery.post(articleDoAPIPostURL, {
            articleAction: articleAction,
            selectedArticle: articleId
        }).always(reload);
    }

    function generateURL(key, value) {
        let myParam = key + "=" + value;
        let url = window.location.href;
        let params = window.location.search.substring(1).split("&");
        //无参数时
        if (params.length === 1 && params[0] === "") {
            if (url.indexOf("?") === -1) {
                url += "?";
            }
            return url + myParam;
        }
        let i;
        for (i = 0; i < params.length; i++) {
            if (params[i].split("=")[0] === key) {
                params[i] = myParam;
                break;
            }
        }
        if (i === params.length) {
            params.push(myParam);
        }
        return url.substring(0, url.indexOf("?") + 1) + params.join("&");
    }

    jQuery(".link-to-first").prop("href", generateURL("page", 0));
    jQuery(".link-to-previous").prop("href", generateURL("page", currentPage - 1));
    jQuery(".link-to-next").prop("href", generateURL("page", currentPage + 1));
    jQuery(".link-to-last").prop("href", generateURL("page", totalPage));

    jQuery(".select-all").change(function () {
        let allCheckbox = articleListTable.find("input[type='checkbox']");
        if (this.checked) {
            allCheckbox.prop("checked", true);
        } else {
            allCheckbox.prop("checked", false);
        }
    });

    jQuery(".article-action-select").change(function () {
        let articleActionButton = jQuery(this).siblings("button.article-action-button");
        if (this.value === "") {
            articleActionButton.prop("disabled", true);
        } else {
            articleActionButton.prop("disabled", false);
        }
    });

    jQuery(".article-action-button").click(function () {
        if (getSelectVal(this) !== "DELETE_ARTICLE") {
            jQuery.post(articleDoAPIPostURL, getData(this)).always(reload);
        } else {
            let articleActionButton = this;
            BootstrapDialog.show({
                title: "警告",
                message: "你真的想删除选中的 " + articleListTable.find("th input[type=checkbox]:checked").length + " 篇文章吗? 删除后无法恢复!",
                buttons: [{
                    label: "确定",
                    action: function () {
                        jQuery.post(articleDoAPIPostURL, getData(articleActionButton)).always(reload);
                    }
                }, {
                    label: "取消",
                    action: function (dialog) {
                        dialog.close();
                    }
                }]
            });
        }
    });

    jQuery(".publish-article").click(function () {
        singlePost("PUBLISH_ARTICLE", getArticleId(this));
    });

    jQuery(".cancel-publish").click(function () {
        singlePost("CANCEL_PUBLISH", getArticleId(this));
    });

    jQuery(".delete-article").click(function () {
        let id = getArticleId(this);
        let title = getArticleRowTr(this).attr("data-title");
        BootstrapDialog.show({
            title: "警告",
            message: "你真的想删除文章 '" + title + "' 吗? 删除后无法恢复!",
            buttons: [{
                label: "确定",
                action: function () {
                    singlePost("DELETE_ARTICLE", id);
                }
            }, {
                label: "取消",
                action: function (dialog) {
                    dialog.close();
                }
            }]
        });
    });

    jQuery("#article-filter-button").click(function () {
        window.location.href = generateURL("sortId", jQuery("#article-filter-select").val());
    });

    jQuery("#jump-to-page").keydown(function (event) {
        if (event.which === 13) {
            window.location.href = generateURL("page", jQuery(this).val() - 1);
        }
    });
});
