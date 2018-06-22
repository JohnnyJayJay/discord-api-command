# discord-api-command
A simple Command API for the JDA

VERSION: 3.0

Features:
- easy command implementation
- good performance
- custom prefixes for different guilds (if wanted)
- auto-generated help command (if wanted)
- deactivation of all commands by one simple method (and activation the same way)
- high flexibility due to `CommandSettings`
- prevention of exceptions and errors by substantial validation and exception handling
- error transparency through `CommandSetException`


To use it, download the .jar file from the folder out. I'm currently working on deploying it on a maven repository so it's even easier to implement.
On how to use it:
- (In the docs-folder, there's a documentation which you might want to take a look at.)
- Besides, you can read the comments

Quick starting guide:

1. After building your JDA, create a new CommandSettings-object.
2. Make your command classes by implementing ICommand.
3. Add your command classes to your CommandSettings by using put(ICommand, String). The String is the name of the command, meaning your command may later be called by [prefix][name]. You can also add aliases in an overload method by just passing multiple Strings as labels.
4. After having added everything, activate your CommandSettings. Otherwise it won't work. After the activation, you can still add and remove commands as well as set your prefix.
5. Also, you can deactivate your CommandSettings. After that, no command will be registered anymore, but you can activate it again with activate(). This is to temporarily disable commands. Notice that you cannot use a command for reactivation, since EVERY command is ignored.
