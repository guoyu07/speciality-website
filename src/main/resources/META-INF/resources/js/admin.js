jQuery("#sidebar > li").each(function () {
    if (this.getAttribute("data-item") == activeSidebarItem) {
        jQuery(this).addClass("active");
    }
});
