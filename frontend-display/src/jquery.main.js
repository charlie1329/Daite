/**
 * Created by Artur on 09.08.2016.
 * George (12.8) - May need to clear this up later, I just used this file to test some of the additional JQuery featurse
 */

var username;

function sendMessage() {
    
    var $client_message = "<li class='message'> <span class='user-name me''>" +
                                username +
                                "</span> <span class='message-text'>" +
                                $('#type-message').val() +
                                "</span> </li>";

     // Clear text entry
     $('#type-message').val("");

     // Add client message to window
     $('.message-list').append($client_message)
    $('.chat-window').prop("scrollHeight")


}

(function ($) {
    $.get('views/enter-name.html', function (enter_name) {
        $('.content').append(enter_name);
        $('.enter-name').slideDown();
        $('.enter-name .btn').click(function (event) {
            event.preventDefault();
            $('.enter-name .btn').prop('disabled', true).html('Loading...');
            username = $('#name').val();
            setTimeout(function () {
                $('.enter-name').slideUp();
            }, 1500); //simulate connecting with back-end
            setTimeout(function () {
                $('.chat-section').slideDown();
            }, 2500);
        });
     })
}(jQuery));

$(document).ready(function() {
    $('#type-message').keydown(function(ee) {
        if(ee.keyCode == 13) {
            sendMessage();
        }
    }
)});



function endConvo() {
    confirm("You are about to end the conversation.");
}

$('#end').click(endConvo);

var start = new Date;

setInterval(function() {
    $('#timer').text(Math.floor((new Date - start) / 1000) + " Seconds");
}, 1000);

