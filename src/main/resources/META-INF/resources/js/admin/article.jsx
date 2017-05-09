jQuery(document).ready(function () {
    let articleStatusDisplay = jQuery("#article-status-display");
    let articleStatusVal;
    let articleStatus = jQuery("#publish");
    let createTimeDiv = jQuery("#createTime-div");
    let createTimeDisplay = jQuery("#createTime-display");
    let createTimeVal;
    let createTime = jQuery("#createTime");
    let createTimeHelpBlock = jQuery("#createTime-help-block");
    let articleForm = jQuery("#article-form");
    let alertDanger = jQuery("#alert-danger");
    let id = jQuery("#id").val();
    let saveArticleTimer;
    let summerNote = jQuery("#summernote");

    class AlertDangerComponent extends React.Component {
        render() {
            return <div className="alert alert-danger alert-dismissible">
                <button type="button" className="close" data-dismiss="alert" aria-hidden="true">×</button>
                <h4><i className="icon fa fa-ban"></i> Alert!</h4>
                {this.props.errors.map(function (error) {
                    return <p>{error.defaultMessage}!</p>;
                })}
            </div>;
        }
    }

    function hideParentDivAndShowPrevEditButton(button) {
        let parent = button.parent(".hide-if-js");
        parent.hide();
        parent.prev(".hide-if-no-js").show();
    }

    function updateArticleStatusDisplay() {
        articleStatusVal = articleStatus.val();
        articleStatusDisplay.text(articleStatus.children(":selected").text());
    }

    function updateCreateTime() {
        createTimeVal = createTimeDisplay.text();
        createTime.val(createTimeVal.replace(" ", "T"));
    }

    function getArticleFormData() {
        return {
            title: jQuery("#title").val(),
            tag: jQuery("#tag").val(),
            image: jQuery("#image").val(),
            content: summerNote.summernote("code"),
            sort: jQuery("#sort").val(),
            createTime: createTimeVal,
            publish: articleStatusVal
        };
    }

    function saveArticle(isAutoSave = true) {
        let saveTimeNotification = jQuery("#save-time-notification");
        if (!articleForm.valid()) {
            saveTimeNotification.text("");
            return;
        }
        let saveSpinner = jQuery("#save-spinner");
        let saveStatus = jQuery(".save-status");
        let saveArticleButton = jQuery("#save-article-button");
        saveArticleButton.prop("disabled", true);
        saveSpinner.show();
        if (!isAutoSave) {
            saveStatus.hide();
        }
        let url;
        let method;
        if (id == 0) {
            url = newArticlePostURL;
            method = "POST";
        } else {
            url = editArticlePatchURL + id;
            method = "PATCH";
        }
        jQuery.ajax(url, {
            method: method,
            data: getArticleFormData()
        }).done(function (data) {
            alertDanger.empty();
            saveTimeNotification.text("已保存于 " + new Date());
            if (id == 0) {
                id = data;
            }
            restartAutoSaveArticleLoop();
            if (isAutoSave) {
                return;
            }
            jQuery("#save-status-success").show().delay(2000).hide(0);
        }).fail(function (data) {
            saveTimeNotification.text("");
            if (isAutoSave) {
                return;
            }
            jQuery("#save-status-error").show().delay(2000).hide(0);
            switch (data.status) {
                case 401: {
                    alert("登录后才能编辑文章");
                    window.location.href = loginPage;
                }
                    break;
                case 400: {
                    ReactDOM.render(
                        <AlertDangerComponent errors={data.responseJSON}/>,
                        alertDanger[0]
                    );
                }
                    break;
                case 500: {
                    let message = "服务器错误: ";
                    if (data.responseJSON !== undefined) {
                        message += data.responseJSON.message;
                    } else {
                        message += data.statusText;
                    }
                    alert(message);
                }
                    break;
                case 0: {
                    alert("网络不可达");
                }
                    break;
                default: {
                    alert("未知错误: " + data.statusText);
                }
                    break;
            }
        }).always(function () {
            saveArticleButton.prop("disabled", false);
            saveSpinner.hide();
        });
    }

    function restartAutoSaveArticleLoop() {
        if (id == 0) {
            return;
        }
        clearInterval(saveArticleTimer);
        saveArticleTimer = setInterval(saveArticle, 60 * 1000);
    }

    function initSummernote() {
        summerNote.summernote({
            height: 250,
            minHeight: null,
            maxHeight: null,
            focus: true,
            callbacks: {
                onImageUpload: files => {
                    let formData = new FormData();
                    Array.from(files).forEach(file => {
                        if (file.type.match("image.*")) {
                            formData.append("multipartFiles", file);
                        }
                    });
                    jQuery.post({
                        url: articleImagesUploadURL,
                        data: formData,
                        cache: false,
                        contentType: false,
                        processData: false,
                    }).done(data => {
                        data.forEach((name) => {
                            summerNote.summernote("insertImage", articleImagesLocation + name);
                        });
                    });
                }
            }
        });
        summerNote.summernote("code", jQuery("#content").text());
    }

    updateArticleStatusDisplay();
    updateCreateTime();
    initSummernote();

    jQuery(".hide-if-no-js").click(function () {
        let button = jQuery(this);
        button.hide();
        button.next(".hide-if-js").show();
    });

    jQuery("#save-article-status").click(function () {
        updateArticleStatusDisplay();
        hideParentDivAndShowPrevEditButton(jQuery(this));
    });

    jQuery("#save-createTime").click(function () {
        createTimeDiv.removeClass("has-error");
        createTimeHelpBlock.hide();
        if (createTime.val() === null || createTime.val() === "") {
            createTimeDiv.addClass("has-error");
            createTimeHelpBlock.show();
            return;
        }
        createTimeVal = createTime.val().replace("T", " ");
        createTimeDisplay.text(createTimeVal);
        hideParentDivAndShowPrevEditButton(jQuery(this));
    });

    jQuery("#cancel-article-status").click(function () {
        articleStatus.val(articleStatusVal);
    });

    jQuery("#cancel-createTime").click(function () {
        createTimeDiv.removeClass("has-error");
        createTimeHelpBlock.hide();
        createTime.val(createTimeVal.replace(" ", "T"));
    });

    jQuery(".cancel-button").click(function () {
        hideParentDivAndShowPrevEditButton(jQuery(this));
    });

    articleForm.validate({
        submitHandler: function () {
            saveArticle(false);
        }
    });
});

jQuery(window).bind("beforeunload", function () {
    return "You will lose all unsaved data if you close this page!";
});
