jQuery(document).ready(function () {
    jQuery("#config-form select.multi-line").each(function (index, value) {
        let select = jQuery(value);
        if (select.children("option[selected]").length === 0) {
            select.parent().remove();
        }
    });

    jQuery(document).on("click", ".remove-parent", function () {
        let parent = jQuery(this).parent();
        parent.animate({height: "0"}, "normal", "swing", function () {
            parent.remove();
        });
    });

    jQuery(document).on("click", ".add-more", function () {
        let parent = jQuery(this).parents("div[data-template]");
        let template = jQuery("#" + parent.attr("data-template")).clone();
        template.removeAttr("id");
        template.appendTo(parent);
    });

    jQuery(document).on("click", ".add-more-child", function () {
        let template = jQuery("#" + jQuery(this).parents("div[data-template]").attr("data-template")).clone();
        template.removeAttr("id");
        jQuery(this).parent().after(template);
    });
});
