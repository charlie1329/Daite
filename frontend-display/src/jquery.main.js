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
    $('.message-list').append($client_message);
    $('.chat-window').prop("scrollHeight")


}

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

// Main stuff happens below!!!

(function ($) {
    var view_enter_name = $.get('views/enter-name.html', function (enter_name) {
        $('.content').append(enter_name);
    });

    var view_chat_section = $.get('views/chat-section.html', function (chat_section) {
        $('.content').append(chat_section);
    });

    $.when(view_enter_name, view_chat_section).done(function () {
        $('.enter-name').slideDown();
        $('.enter-name .btn').click(function (event) {
            event.preventDefault();
            $('.enter-name .btn').prop('disabled', true).html('Loading...');
            username = $('#name').val();
            setTimeout(function () {
                $('.enter-name').slideUp();
            }, 1000); //simulate connecting with back-end
            setTimeout(showChat, 2000);
        });

        function showChat () {
            $('.chat-section').slideDown();

            //moved this here to prevent the timer from starting upon page loading
            var start = new Date;
            setInterval(function () {
                
                //total length of time you want the conversation to be
                var totalTime = 180;
                
                var time = totalTime - Math.floor((new Date - start) / 1000);
                var countdown = function(){
                    if(time > 0){
                        return time;
                    }
                    else if(time == 0){
                        endConvo(); //ends conversation when the timer reaches 0
                        $('.chat-section').slideUp();
                    }
                }
                $('#timer').text(countdown() + " seconds");
            }, 1000);
        }
    })
})(jQuery);

