var SlackBot = require('slackbots');
var S = require('string');
var fs = require('fs');
var key = 'xoxp-64843053010-67049675732-69596346227-f33084bdf3'; //should be read from key store, not put on git :/

// create a bot
var bot = new SlackBot({
    token: key, // Add a bot https://my.slack.com/services/new/bot and put the token 
    name: 'AImy'
});

var Cleverbot = require('cleverbot-node');
cleverbot = new Cleverbot;
var channelID = 0;

var questions = []
var qanda = []

bot.on('start', function() {
    // define channel, where bot exist. You can adjust it there https://my.slack.com/services 
    bot.postMessageToChannel('test', 'meow!', {});

    bot.getChannelId('test').then(function(data) {
        channelID = data;
        console.log('channel: ' + channelID);
    });


    
    

});


/**
 * @param {object} data
 */
bot.on('message', function(data) {
    // all ingoing events https://api.slack.com/rtm
    if(data.channel == channelID && data.username != 'AImy' && data.type == 'message') {
        console.log("***************");
        console.log(data);
        console.log("***************");
        //bot.postMessageToChannel('test', 'ok', {});
	if(data.text.includes("?")){
		questions.push(data.text);
	}
	else if(data.text.includes("questions")){
		bot.postMessageToChannel('test', questions, {});
	}
	else if(false) {
        Cleverbot.prepare(function(){
          cleverbot.write(data.text, function (response) {
               bot.postMessageToChannel('test', response.message, {});
          });
        });
	}
	else if(true) {
		qanda.push(data.text);
		var index = Math.floor((Math.random() * questions.length));
		var question = questions[index];
		bot.postMessageToChannel('test', question, {});
		qanda.push(question);
	}

    }

});
