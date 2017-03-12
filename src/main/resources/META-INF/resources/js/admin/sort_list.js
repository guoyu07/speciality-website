var saveTaxis = jQuery("#save-taxis");
var strongs = [];
var regExp = new RegExp("^[0-9]+$");
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
