var SlackBot = require('slackbots');
var S = require('string');
var key = 'xoxp-64843053010-67049675732-69596346227-f33084bdf3'; //should be read from key store, not put on git :/
var fs = require('fs');

var trainingChannelID = 0;
var questionsFileUrl = 'questions.txt';
var logger;

var bot = new SlackBot({
    token: key,
    name: 'AImy'
});


bot.on('start', function() {
    logger = fs.createWriteStream(questionsFileUrl, {
      flags: 'a' // 'a' means appending (old data will be preserved)
    });

    //bot.postMessageToChannel('ai_training', 'Give me some training questions, followed by \'?\'', {});
    //bot.postMessageToChannel('ai_training', 'You can view the questions posted recently by entering \'questions\'', {});

    bot.getChannelId('ai_training').then(function(data) {
        trainingChannelID = data;
        console.log('training channel id: ' + channelID);
    });

    
});

function getRandomQuestion() {
	var size = questions.length;
	var index = Math.floor(Math.random()*size);
	var question = questions[index];
	return question;
}

function askRandomQuestion() {
	bot.getUsers().then(function(data) {
		console.log('askRandomQuestion, users retrieved');
		var index;
		for(index in data.members) {		
			var name = data.members[index].name;
			if(questions.length > 0) {
				console.log('sending message to ' + console.log(name));
				postMessageToUser(name, getRandomQuestion(), {});
			}
		}
	});
}

questions = [];

/**
 * @param {object} data
 */
bot.on('message', function(data) {

    // training data
    if(data.type == 'message' && data.channel == trainingChannelID && data.username != 'AImy') {
	console.log("########################################");
	console.log(data);
	console.log("########################################");

        if(data.text.includes('?')){
            console.log('question added: ' + data.text);
            questions.push(data.text);
            logger.write('\n' + data.text);
	    bot.postMessageToChannel('ai_training', 'new question added.', {});
        } else if(data.text.includes('questions')){
            console.log('questions requested');
            bot.postMessageToChannel('ai_training', questions, {});
        } else if(data.text == 'ask') {
		console.log('ask command entered');
		askRandomQuestion();
	}
    }
});
