Gadget Show Back-end Project
===========================

In order to be working with this project you will need to have gradle installed
As well as installing it, there are a couple of plug-ins for both Eclipse and IntelliJ I believe.
If you need to use and API's etc. please add the dependency in the build.gradle file in the appropriate place
(most API's will show you where if you don't know).

When adding to this project, please use packages properly with the format src/main/java/package_name. If you need to write any unit tests for any reason, please do the same but with src/test/package_name. (Obviously if you need to nest packages further that's fine). Also if everyone can ensure they well document their code. We will be working together closely on this back-end and so well documented code will make everything run smoother (as well as making me happy, un-commented code is one of my pet hates!)

If you have any questions, or alternatively if I have messed up the organisation of the git repository, please give me a shout!!! 


Also, because I have a tendency to be a bit crappy with git sometimes, when you push can you please ensure you don't get a load of weird gradle files appear on the repository please? (Like .settings or taskArtifacts etc.) I tried to set it up with .gitignore but I might have messed it up slightly, I'm not sure. 

Conversation data JSON files
============================

Any JSON files containing our conversation data should be stored directly in the data folder in this repository (that is where JSON is being read from anyway)! If the JSON files could be named topic_name.json for general neatness that would be fantastic. Additionally if they could be frequently copied over to this repository such that we have a) the data backed up should something go horribly wrong and b) it allows me to test the system with increasing amounts of data, which is incredibly useful!!