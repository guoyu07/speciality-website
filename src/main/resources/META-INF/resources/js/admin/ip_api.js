jQuery(document).ready(function () {
    jQuery("td[data-ip]").each(function () {
        var td = jQuery(this);
        jQuery.ajax({
            url: "http://ip-api.com/json/" + td.attr("data-ip"),
            success: function (data) {
                if (data.status != "success") {
                    td.text(data.message);
                } else {
                    td.text(data.country + " " + data.regionName + " " + data.city);
                }
            }
        });
    });
});
