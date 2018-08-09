# discord-api-command
**A simple Command API for the JDA**

CURRENT VERSION:  **3.2** <p>
Other versions:  **3.1**, **3.0_3**, **3.0_2**, **3.0_1**, **3.0** <p>
**[Changelog](https://github.com/JohnnyJayJay/discord-api-command/blob/master/changelog.md)**<br>
**[Documentation](http://docs.johnnyjayjay.me)**

## Features:
- easy command implementation
- good performance
- custom prefixes for different guilds (if wanted)
- auto-generated help command (if wanted)
- Support of either JDA or ShardManager
- deactivation of all commands by one simple method (and activation the same way)
- high flexibility due to `CommandSettings`
- prevention of exceptions and errors by substantial validation and exception handling
- error transparency through `CommandSetException`

## On how to add this to your project
### Adding as a library
You can download the .jar-file in [this directory](https://github.com/JohnnyJayJay/discord-api-command/tree/master/builds) and add it to the project
 by performing the following steps:
 1. Create a new directory in your project folder
 2. Copy the jar-file to it
 3. Eclipse: Right-click the jar: `Build Path -> Add To Build Path` <p> IntelliJ: Right-click the jar: `Add As Library`
 4. Done. You can now use it.
 
### Adding Dependency in pom
If you use Maven, you can add this library even less complicated: Just add the following dependency in your `pom.xml`:
```xml
<dependency>
    <groupId>com.github.johnnyjayjay</groupId>
    <artifactId>CommandAPI</artifactId>
    <version>VERSION</version>
</dependency>
```
And that's it!

## Getting started
### Wrting Commands
Generally speaking, there are two ways to write your own commands.

#### Implementing ICommand directly
This method is simpler and more performant. If you are seeking high performance, this might be your choice.
Only classes that implement the interface ICommand are accepted by the `put`-method in `CommandSettings`.
`ICommand` is also a `FunctionalInterface`, meaning it can be used with lambda expression. This may be useful for temporary commands, which is possible with this framework.
An implementation of `ICommand` might look like this:
```java
public class PingCommand implements ICommand {
    
    private final Message infoMessage = new MessageBuilder().setContent("This command pongs you. Try it out!").build();
    
    // Important (must be overwritten)
    // This command simply responds with "Pong" when it's called
    @Override
    public void onCommand(CommandEvent event, Member member, TextChannel channel, String[] args) {
        event.respond("Pong! " + member.getAsMention());
    }
    
    // Optional: Returns information about the command (can be used for help commands for example)
    @Override
    public Message info(Member member, String prefix, Set<String> labels) {
        return infoMessage;
    }
}
```

#### Extending AbstractCommand
The other way to create a command is making a sub class of `AbstractCommand`. This provides you with more possibilities, though it is less performant.
The main reason why you should rather use `AbstractCommand` than `ICommand` is readability and less confusion, mainly for big commands.<br>
`AbstractCommand` supports the sub command system, meaning you may have multiple methods for different kinds of command execution. 
Here's an example to make it more understandable: 
You want to have a report-command, which has multiple options:<br>
1. Report a member with a reason (!report @Member <reason>)
2. Get the reports of a member (!report get @Member)
3. Remove all reports from a member (!report remove @Member)
4. Get your own reports (!report)
<br>With `ICommand`, it would probably look like this:
```java
public class ReportCommand implements ICommand {
    
    @Override
    public void onCommand(CommandEvent event, Member member, TextChannel channel, String[] args) {
        List<Member> mentions = event.getMessage().getMentionedMembers();
        if (args.length > 1 && mentions.size() == 1) {
            if (args[0].matches("<@!?\\d+")) { // regex for a member mention
                // report the mentioned member
            } else if (args[0].equalsIgnoreCase("get")) {
                // get the reports of the mentioned member
            } else if (args[0].equalsIgnoreCase("remove")) {
                // remove the reports from the mentioned member
            }
        } else if (args.length == 0){
            // Show your own reports
        } else {
            // Error message
        }
    }
}
```
Now, if your system is complex, this class won't be a beauty at the end (trust me). How would it look like with `AbstractCommand`?
```java
public class ReportCommand extends AbstractCommand {
    
    @SubCommand(isDefault = true) // will be called if no other sub command matches
    public void onWrongUsage(CommandEvent event, Member member, TextChannel channel, String[] args) {
        // Error message
    }
    
    @SubCommand(args = {this.MEMBER_MENTION}, moreArgs = true) // moreArgs: means that there has to be at least one more argument than specified. Useful for the report reason.
    public void onReport(CommandEvent event, Member member, TextChannel channel, String[] args) {
        // report the mentioned member
    }
    
    @SubCommand(args = {"get", this.MEMBER_MENTION})
    public void onReportsGet(CommandEvent event, Member member, TextChannel channel, String[] args) {
        // get the reports of the mentioned member
    }
    
    @SubCommand(args = {"remove", this.MEMBER_MENTION})
    public void onReportsRemove(CommandEvent event, Member member, TextChannel channel, String[] args) {
        // remove the reports from the mentioned member
    }
    
    @SubCommand // wut? no arguments. That means that the args-array is empty, so this method is triggered if no arguments are given.
    public void onSelfReportGet(CommandEvent event, Member member, TextChannel channel, String[] args) {
        // Show your own reports
    }
}
```
This looks much better, right? You can even pass an argument called `botPerms` to the SubCommand annotation that contains Permissions the bot has to have for this command.<br>
As you can see, almost every SubCommand has an argument called `args`. You may have guessed: it specifies how many arguments must be given and what they have to look like, more precisely what regular expression they have to match.
`this.MEMBER_MENTION` is a `protected final String` from `AbstractCommand` that may be used as a regex for member mentions. There's also a regex for role mentions and channel mentions.<br>
The regular expressions in the args-array will get a case insensitivity flag if `labelIgnoreCase` in `CommandSettings` is true. If it isn't, the arguments will be case sensitive (unless you flag them yourself, of course).

### CommandSettings
Now, just creating an `ICommand` class won't do much - actually, nothing at all.
Imagine we want to have this Ping Command registered. This would perhaps look like this:
```java
public class Bot {
    public void start() {
        // somewhere in our code
        // "!" would be the default prefix, jda an instance of either JDA or ShardManager, true says that labels are case insensitive. 
        // So it doesn't make a difference whether you type `!ping` or `!PING`
        CommandSettings settings = new CommandSettings("!", jda, true);
        settings.put(new PingCommand(), "ping", "pingpong", "p") // You can add aliases by just giving more labels
                .activate();  // And don't forget to activate these settings.
    }
}
```

Simple, right? Don't let the method name `put` confuse you. Even though it has to do with a `Map`, `new PingCommand()` is not the key, but the value.
In this case, someone could write `!ping` in a channel the bot is also in and the bot would respond with `Pong! @Member`.
This person could also type `!pingpong` or `!p`. These are aliases.<br>
`CommandSettings` has a lot of other useful methods. Make sure to check the docs for more.

### Using help commands
This framework provides a template and a default implementation for help commands.
You are not forced to use it, though it can be a useful tool for common help commands.<br>
To use the default implementation, register an instance of `DefaultHelpCommand` as a command.
To create an own implementation based on the template, make a sub class of `AbstractHelpCommand`.
```java
public class HelpCommand extends AbstractHelpCommand {
    
    @Override
    public void provideGeneralHelp(CommandEvent event, String prefix, Map<String, ICommand> commands) {
        // general help here (if no (valid) label is given)
    }
    
    @Override
    public void provideSpecificHelp(CommandEvent event, String prefix, ICommand command, Set<String> labels) {
        // specific help for a specific instance of ICommand here
    }
}
```
You may register this now like any other command.

### Other features
This API has supported guild-custom prefixes for some time now. By using `CommandSettings#setCustomPrefix` you can associate a prefix to a specific guild (or `CommandSettings#setCustomPrefixes` for bulk addition). 
Though it is important to know that this is only saved temporarily, meaning as long as the program runs. If you want to apply changes like this permanently, you have to keep track of that manually.
I am currently not planning to add any database integration. 

Since version 3.1, you can also blacklist channels by using `CommandSettings#addChannelToBlacklist(long)` and passing the id. This is only for temporary usage like custom 
prefixes.  

There are a few more features to `CommandSettings`, such as:
- Allowing bots to execute commands
- Setting a message that is displayed in case of an unknown command
- Setting a command cooldown (and specifying whether the cooldown should be reset on each execution attempt)
- Setting a Color for `DefaultHelpCommand`

### Exceptions
This API should only throw one kind of exception, `CommandSetException` (except for explicitly thrown `IllegalArgumentException`s). 
If this is **NOT** the case and you get another exception thrown by anything inside the API you don't have control over, [please report this here](https://github.com/JohnnyJayJay/discord-api-command/issues).

`CommandSetException` is a sub class of `RuntimeException`, meaning that they don't have to be caught and they don't terminate the program.

A `CommandSetException` is thrown if:
- a label or a prefix does not match the requirements, i.e. the regex defined in `CommandSettings.VALID_PREFIX` and `CommandSettings.VALID_LABEL`. This includes:
    - prefixes that contain one or more of the characters `\+*^|$?` or are just an empty String 
    - labels that contain any kind of blank spaces or are just an empty String
- an instance of `CommandSettings` is activated or deactivated twice (which is not possible)
- any other settings input for `CommandSettings` is invalid

If you don't want to have any exceptions concerning prefixes and labels, it is recommended to check whether they match `CommandSettings.VALID_PREFIX` 
or `CommandSettings.VALID_LABEL`.

```java
String prefix = // user input or something else you can't verify directly
if (prefix.matches(CommandSettings.VALID_PREFIX)) {
    // ...
} else {
    // Tell the user
}
```
With sub commands, you can even set `CommandSettings.VALID_PREFIX`/`CommandSettings.VALID_LABEL` as an argument regular expression.

## Contributing
If you think this framework is missing a feature and you think you're able to write it yourself, fork this repository and go for it. 
I recommend opening an issue first to prevent misunderstandings or waste of time because I'm already making your feature.<br>
Then you may make a pull request and I'll give it an inspection. If it's error prone in any way or simply bad code style, I probably won't
accept it before any necessary adjustments.<br> 
Requirements:
- Please hold on to the [Java conventions](https://www.oracle.com/technetwork/java/codeconventions-150003.pdf) (I know, they're old).
- Document your feature (classes, constructors, methods)
- This project is object-oriented. Avoid using `static` (except for utility classes or public immutable fields) and other bad practices.
- Make your code as performant as possible
- Test your code (maybe even write unit tests)
- Only contribute features that fit in. Simple, useful and enhancing.

### Requests and ideas
If you don't have time or you don't want to make a feature yourself: 
you can always request new features as well as ask questions [here](https://github.com/JohnnyJayJay/discord-api-command/issues). Or just add me on Discord (Johnny#3826) and we 
can talk there. But note that this framework ought to stay simple. So unfortunately there won't be any built-in unicorn command. 
~~Sorry.~~
<p>
I hope you like this project. 
Thanks for reading this shit to the bitter end. <p> Have fun programming your Discord bot with JDA and CommandAPI!





 
