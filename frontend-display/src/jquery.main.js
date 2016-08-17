/**
 * Created by Artur on 09.08.2016.
 */

/* Variables */
var my_username;
var socket = io.connect("http://localhost:6969/chat");

/* ON LOAD */
$(document).ready(function () {

    // Send message when enter pressed in text box
    $('#type-message').keydown(function (ee) {
        if (ee.keyCode == 13) {
            sendMessage();
        }
    });

    // Show about dialogue
    $('.about-button').click(function () {
        $('.about-dialogue').slideDown();
    });

    // Hide about dialogue
    $('.close').click(function () {
        $('.about-dialogue').slideUp();
    });

    $('#end').click(function () {
            var content = $('.content');
            $('#end-convo-dialog').dialog({
                draggable: false,
                resizable: false,
                modal: true,
                position: {my: 'bottom-100%', of: content, within: content},
                buttons: [
                    {
                        text: "Yes",
                        click: function () {
                            $(this).dialog('close');
                            endConvo();
                        }
                    },
                    {
                        text: "No",
                        click: function () {
                            $(this).dialog('close');
                        }
                    }
                ]
            });
        }
    );
});

/* Sending and Receiving Messages */
socket.on("message", function (data) {
    if (data.username == my_username) {
        // Server sent the message back to us, so we know it was sent
        // Remove pending class
        setTimeout(function(){$(".pending").removeClass("pending");}, 100);

    }
    else {
        receiveMessage(data);
    }
});

function receiveMessage(data) {
    // Get username and message
    var match_username = data.username;
    var match_message = data.message;

    // Add message to the chat window
    var received_message = "<li class='message'> <span class='user-name match'>" +
        match_username +
        "</span> <span class='message-text'>" +
        match_message +
        "</span> </li>";
    $('.message-list').append(received_message);
}

function sendMessage() {

    //continuously grabbing a selector isn't free!
    var type_message = $('#type-message');

    if (type_message.val().length > 0) {
        // Add message to the chat window
        // Pending class used to grey out message until we know the server got it.
        var client_message = "<li class='message pending'> <span class='user-name me''>" +
            my_username +
            "</span> <span class='message-text'>" +
            type_message.val() +
            "</span> </li>";
        $('.message-list').append(client_message);

        // Clear text entry
        type_message.val("");

        // Sends raw message to server
        socket.send({username: my_username, message: type_message.val()});

        // Reset placeholder message
        type_message.attr('placeholder', 'Please Enter to send')
    }
    else {
        type_message.attr('placeholder', 'Please type a message to send it').val("");
    }
}

/* MISC FUNCTIONS */
function endConvo() {
    //alert("Your conversation has ended!");
    var content = $('.content');

    $('#convo-ended-dialog').dialog({
        draggable: false,
        resizable: false,
        modal: true,
        position: {my: 'bottom-100%', of: content, within: content},
        buttons: [
            {
                text: "Okay",
                click: function () {
                    location.reload();
                }
            }
        ]
    });
    $('.chat-section').slideUp();
}

// Toggle the typing indicator
function typingIndicator() {

}

/* Main stuff happens below!!! */
(function ($) {
    var view_enter_name = $.get('views/enter-name.html', function (enter_name) {
        $('.content').append(enter_name);
    });

    var view_chat_section = $.get('views/chat-section.html', function (chat_section) {
        $('.content').append(chat_section);
    });

    var about_float = $.get('views/about-float.html', function (about_float) {
        $('.content').append(about_float);
    });

    $.when(view_enter_name, view_chat_section, about_float).done(function () {
        $('.enter-name').slideDown();
        $('.enter-name .btn').click(function (event) {
            event.preventDefault();
            my_username = $('#name').val();
            if (my_username.length > 0) {
                $('.enter-name .btn').prop('disabled', true).html('Loading...');
                setTimeout(function () {
                    $('.enter-name').slideUp();
                }, 1000); //simulate connecting with back-end
                setTimeout(showChat, 2000);
            }
            else
            //alert('Enter your first name!')
                $('#no-name-dialog').dialog({
                    draggable: false,
                    resizable: false,
                    modal: true,
                    position: {my: 'bottom-100%', of: $('.content'), within: $('.content')},
                    buttons: [
                        {
                            text: "Okay",
                            click: function () {
                                $(this).dialog('close');
                            }
                        }
                    ]
                })
        });

        function showChat() {
            $('.chat-section').slideDown();

            // Moved this here to prevent the timer from starting upon page loading
            var start = new Date;
            setInterval(function () {

                // Total length of time you want the conversation to be
                var totalTime = 180;

                var time = totalTime - Math.floor((new Date - start) / 1000);
                var countdown = function () {
                    if (time > 0) {
                        return time;
                    }
                    else if (time == 0) {
                        endConvo(); //ends conversation when the timer reaches 0
                    }
                };
                $('#timer').text(countdown() + " seconds");
            }, 1000);
        }
    })
})(jQuery);
