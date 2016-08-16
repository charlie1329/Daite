var SlackBot = require('slackbots');
var S = require('string');
var key = 'xoxp-64843053010-67049675732-69596346227-f33084bdf3'; //should be read from key store, not put on git :/
var fs = require(fs)

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

    bot.postMessageToChannel('training', 'Give me some training questions, followed by \'?\'', {});
    bot.postMessageToChannel('training', 'You can view the questions posted recently by entering \'questions\'', {});

    bot.getChannelId('training').then(function(data) {
        trainingChannelID = data;
        console.log('training channel id: ' + channelID);
    });
});

function writeQuestionToFile(question) {
    logger.write('\n' + question) // append string to your file
}


questions = [];

/**
 * @param {object} data
 */
bot.on('message', function(data) {

    // training data
    if(data.channel == trainingChannelID && data.username != 'AImy') {
        if(data.text.includes('?')){
            console.log('question added: ' + data.text);
            questions.push(data.text);
            writeQuestionToFile(data.text);
        }
        else if(data.text.includes("questions")){
            console.log('questions requested');
            bot.postMessageToChannel('training', questions, {});
        }
    }
});
