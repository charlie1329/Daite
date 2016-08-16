var SlackBot = require('slackbots');
var S = require('string');
var fs = require('fs');
var key = 'xoxp-64843053010-67049675732-69596346227-f33084bdf3'; //should be read from key store, not put on git :/

var training-channel-id = 0;

var bot = new SlackBot({
    token: key,
    name: 'AImy'
});


bot.on('start', function() {
    bot.postMessageToChannel('training', 'Give me some training questions, followed by \'?\'', {});

    bot.getChannelId('training').then(function(data) {
        training-channel-id = data;
        console.log('training channel id: ' + channelID);
    });
});


questions = [];

/**
 * @param {object} data
 */
bot.on('message', function(data) {

    // training data
    if(data.channel == training-channel-id && data.username != 'AImy') {
        if(data.text.includes('?')){
            console.log('question added: ' + data.text);
            questions.push(data.text);
        }
        else if(data.text.includes("questions")){
            console.log('questions requested');
            bot.postMessageToChannel('training', questions, {});
        }
    }
});
