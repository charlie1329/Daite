# GadgetShow - Networking

#####Networking/Chat implementation for GadgetShow Dating chatbot.

Currently just a basic implementation of the netty-socketio server that echos messages sent on the /chat namespace to all clients. At the moment the code is a bit messy and everything is just being run from main but it shouldn't be too hard to clean up. 

There's a 'client' in client.html though really it's just a html page that loads the javascript and lets you play around in the developer javascript console (try opening up two tabs with the console on both, and using `socket.send({username:"user", message:"hello!"})`)

I've set up the project using gradle since it seems a bit easier than maven.

Use the gradle wrapper script (`./gradlew`) since it automatically downloads the right version of gradle for you.

To build:

`./gradlew build`

Run:

`./gradlew run`

###Rooms: 
Now supports rooms and requires socket.io clients to register

e.g. 

`socket.emit(“register”, {name:”dukky”, age:22, gender:”Male”});`

`socket.emit(“joinroom”, “test”);`

After this continue to use `socket.send({username:$name, message:$message});` and when the chat is over you can leave the room using `socket.emit("leaveroom", $room)`


Server sends an “error” event if you try and join a room or send a message when not registered

