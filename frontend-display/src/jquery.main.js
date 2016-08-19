/**
 * Created by Artur on 09.08.2016.
 */

/* Variables */
var my_username;
var my_age;
var my_gender; 

var typing = false;

//var socket = io.connect("http://duk.im:6969/chat");
var socket = io.connect("http://localhost:6969/chat");


/* ON LOAD */
$(document).ready(function () {
    
    // Brings focus to name entry text box on page load
    $("#name").focus();

    // Define the dialogs
    var content = $('.content'),
        warning_dialog = $('#warning-dialog'),
        yes_no_dialog = $('#yes-no-dialog');

    warning_dialog.dialog({
        autoOpen: false,
        draggable: false,
        resizable: false,
        modal: true,
        show: {effect: 'fade', duration: 500},
        hide: {effect: 'fade', duration: 500},
        position: {my: 'bottom-75%', of: content, within: content},
        buttons: [
            {
                text: "Okay",
                click: function () {
                    $(this).dialog('close');
                }
            }
        ]
    });

    yes_no_dialog.dialog({
        autoOpen: false,
        draggable: false,
        resizable: false,
        modal: true,
        show: {effect: 'fade', duration: 500},
        hide: {effect: 'fade', duration: 500},
        position: {my: 'bottom-75%', of: content, within: content},
        buttons: [
            {
                text: "Yes",
                click: function () {
                    $(this).dialog('close');
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

    // End conversation dialogue
    $('#end').click(function () {
            yes_no_dialog.dialog('option', 'title', 'End conversation')
                .text("Are you sure you want to end this conversation early?")
                .dialog('option', 'buttons', [
                    {
                        text: "Yes",
                        click: function () {
                            $(this).dialog('close');
                            endConvo()
                        }
                    },
                    {
                        text: "No",
                        click: function () {
                            $(this).dialog('close');
                        }
                    }
                ])
                .dialog('open');
        }
    );
});

/* Sending and Receiving Messages */
socket.on("message", function (data) {
    if (data.username == my_username) {
        // Server sent the message back to us, so we know it was sent
        // Remove pending class
        setTimeout(function () {
            $(".pending").removeClass("pending");
        }, 100);

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
    var received_message =
        "<li class='message' style='display: list-item'>" +
        "<span class='user-name match'>" +
        match_username +
        "</span> <span class='message-text'>" +
        match_message +
        "</span> </li>";
    $('.message-list').append(received_message);
    autoScroll('.chat-window');

}

function sendMessage() {

    //continuously grabbing a selector isn't free!
    var type_message = $('#type-message');

    if (type_message.val().length > 0) {
        // Add message to the chat window
        // Pending class used to grey out message until we know the server got it.
        var client_message =
            "<li class='message pending'>" +
            "<span class='user-name me''>" +
            my_username +
            "</span> <span class='message-text'>" +
            type_message.val() +
            "</span> </li>";
        $('.message-list').append(client_message);

        // Sends raw message to server
        socket.send({username: my_username, message: type_message.val()});

        // Clear text entry
        type_message.val("");
        autoScroll('.chat-window');

        // Reset placeholder message
        type_message.attr('placeholder', 'Press Enter to send');
    }
    else {
        type_message.attr('placeholder', 'Please type a message to send it').val("");
    }
}

/* MISC FUNCTIONS */
function endConvo() {
    // Leave the room
    socket.emit("leaveroom", "test");
    
    $('.chat-section').slideUp();
    $('#warning-dialog').dialog('option', 'title', "It's over!")
        .text("Time's up, the conversation is now finished.")
        .dialog('option', 'buttons', [
            {
                text: "Okay",
                click: function () {
                    location.reload()
                }
            }
        ])
        .dialog('open');
}

function autoScroll(id){
    var myDiv = $(id).get(0);
    myDiv.scrollTop = myDiv.scrollHeight;
}

// Toggle the typing indicator
function typingIndicator() {

}

/* Main stuff happens below!!! */
(function ($) {
    
    // Load external views
    var view_enter_name = $.get('views/enter-name.html', function (enter_name) {
        $('.content').append(enter_name);
    });
    
    var view_enter_details = $.get('views/enter-details.html', function (enter_details) {
        $('.content').append(enter_details);
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
                }, 500); //simulate connecting with back-end
                setTimeout(showDetails, 1000);
            }
            else
                $('#warning-dialog').dialog('option', 'title', 'Uh oh!')
                    .text('You must enter your first name to continue.')
                    .dialog('open')
        });
        
        // Show details entering view
        function showDetails() {
            $('.enter-details').slideDown();
            
            // Brings focus to age text box as soon as page loads
            $("#age").focus(); 

            $('.enter-details .btn').click(function (event) {  
                
                event.preventDefault();
                
                // Get entered age
                my_age = $('#age').val();
                
                // Get gender chosen
                my_gender = $('input[name=gender]:checked').val();
        
                // Prompt if no age entered
                if (my_age == null || !my_age) {
                    $('#warning-dialog').dialog('option', 'title', 'Uh oh!')
                        .text('You must enter your age to continue.')
                        .dialog('open')
                } 
                
                // Prompt if no gender selected
                else if (my_gender == null || !my_age) {
                    $('#warning-dialog').dialog('option', 'title', 'Uh oh!')
                        .text('You must choose your gender to continue.')
                        .dialog('open')
                }
                else {
                    $('.enter-details .btn').prop('disabled', true).html('Loading...');
                    setTimeout(function () {
                        $('.enter-details').slideUp();
                        }, 500); //simulate connecting with back-end
                        setTimeout(showChat, 1500);
                }             
            })
        }

        // Show chat view
        function showChat() {
            $('.chat-section').slideDown();
            
            // Bring focus to text-entry box
            $("#type-message").focus(); 
            
            // Registers the specific client
            socket.emit("register", {name: my_username, age: my_age, gender: my_gender});
            socket.emit("joinroom", "test");
            
            
            // Start timer
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

/* TYPING -- MOVE ME LATER */

function typingTimeout() {  
  typing = false;
  socket.emit("typing", false);
}

// If receives an isTyping from the socket, toggles the typing display
socket.on("isTyping", function(data) {  
    if (data.isTyping) {
        $('.typing-indicator').slideDown;
    }
    else {
        $('.typing-indicator').slideUp;
    }
});

/// STILL TO BE CONTINUED -- BASING UPON http://www.tamas.io/further-additions-to-the-node-jssocket-io-chat-app/

