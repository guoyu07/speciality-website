jQuery(document).ready(function () {
    var articleStatusDisplay = jQuery("#article-status-display");
    var articleStatusVal;
    var articleStatus = jQuery("#publish");
    var createTimeDisplay = jQuery("#createTime-display");
    var createTimeVal;
    var createTime = jQuery("#createTime");

    function hideParentDivAndShowPrevEditButton(button) {
        var parent = button.parent(".hide-if-js");
        parent.hide();
        parent.prev(".hide-if-no-js").show();
    }

    function updateArticleStatusDisplay() {
        var selected = articleStatus.children(":selected");
        articleStatusDisplay.text(selected.text());
        articleStatusVal = selected.val();
    }

    function updateCreateTimeStatusDisplay() {
        createTimeVal = createTime.val();
        createTimeDisplay.text(createTimeVal.replace("T", " "));
    }

    updateArticleStatusDisplay();
    updateCreateTimeStatusDisplay();

    jQuery(".hide-if-no-js").click(function () {
        var button = jQuery(this);
        button.hide();
        button.next(".hide-if-js").show();
    });

    jQuery("#save-article-status").click(function () {
        updateArticleStatusDisplay();
    });

    jQuery("#save-createTime").click(function () {
        updateCreateTimeStatusDisplay();
    });

    jQuery("#cancel-article-status").click(function () {
        articleStatus.val(articleStatusVal);
    });

    jQuery("#cancel-createTime").click(function () {
        createTime.val(createTimeVal);
    });

    jQuery(".save-button, .cancel-button").click(function () {
        hideParentDivAndShowPrevEditButton(jQuery(this));
    });

    jQuery("#save-article").click(function () {
        jQuery(".cancel-button").click();
    });
});
