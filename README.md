# GadgetShow

The plan is to create a dating chat-bot.
A random group of people will be asked to try out our 'new dating system', where they can choose who they want to talk to from a list of names and get to know in a ~3 minute time window.
They'll then be asked if they want to have a proper date with the person they talked with.
Some of the people on the list will be real people, and some will be our AI.
Our aim: Get our AI a date!

* Please keep within the module you're designated to edit to ensure minimal conflicts.
    * Also, use packages to keep your code easy to use for others.
* Branch from master for your own changes. Push to master only when you have pulled from master onto your own branch and fixed any merge conflicts.
* Fetch regularly to ensure you're up to date.

## Process for submitting code
So I've pre-made the packages so where each team should work is obvious.
Say for example you've been given the task: "implement a parser for the stored data".
1. Create a new branch, e.g. 'parser'.
2. Do yo thang.
3. When finished, 'git fetch' and then 'git rebase origin/master'
4. Fix any merge conflicts you may now have.
5. 'git rebase --continue' to make sure there isn't more stuff you need to fix. (Repeat 4-5 as necessary)
6. 'git checkout master'
7. 'git merge 'parser' (use whatever branch name you chose.
8. 'git status' just to make sure all's cool.
9. 'git push'

This way we can always ensure that the master branch will ALWAYS compile. It's really difficult for everyone if there's broken code on the master branch xo
