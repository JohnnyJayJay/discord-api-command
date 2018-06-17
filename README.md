# discord-api-command
A simple Command API for the JDA

To use it, download the .jar file from the folder out. I'm currently working on deploying it on a maven repository so it's even easier to implement.
On how to use it:
- (In the docs-folder, there's a documentation which you might want to take a look at.)
- Besides, you can read the comments

Quick starting guide:

1. After building you JDA, create a new CommandSettings-object.
2. Make your command classes by implementing ICommand.
3. Add your command classes to your CommandSettings by using put(String, ICommand). The first String is the name of the command, meaning your command may later be called by [prefix][name]
4. After having added everything, activate your CommandSettings. Otherwise it won't work. After the activation, you can still add and remove commands.
5. Also, you can deactivate your CommandSettings. After that, no command will be registered anymore, but you can activate it again with activate(). This is to temporarily disable commands. Notice that you of course can't use a command to reactivate them...
