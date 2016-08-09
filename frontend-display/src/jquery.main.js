/**
 * Created by Artur on 09.08.2016.
 */

(function ($) {
    $.get('views/enter-name.html', function (enter_name) {
        $('.content').append(enter_name);
        $('.enter-name').slideDown();
        $('.enter-name .btn').click(function (event) {
            event.preventDefault();
            $('.enter-name .btn').prop('disabled', true).html('Loading...');
            setTimeout(function () {
                $('.enter-name').slideUp()
            }, 1500); //simulate connecting with back-end
        })
    })
}(jQuery));