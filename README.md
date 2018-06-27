# discord-api-command
**A simple Command API for the JDA**

*CURRENT VERSION: **3.0_1***<p>
*Other versions: **3.0***

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
#### Adding as a library
You can download the .jar-file in [this directory](https://github.com/JohnnyJayJay/discord-api-command/tree/master/out/artifacts/CommandAPI_jar) and add it to the project
 by performing the following steps:
 1. Create a new directory in your project folder
 2. Copy the jar-file to it
 3. Eclipse: Right-click the jar: `Build Path -> Add To Build Path` <p> IntelliJ: Right-click the jar: `Add As Library`
 4. Done. You can now use it.
 
#### Adding Dependency in pom
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
#### ICommand
Using the CommandAPI is quite easy. All you need to do is writing your own commands, the rest won't concern you.<p>
Command classes have to implement the interface `ICommand`, which represents a class whose objects are able to execute commands.

This interface contains two methods, `void onCommand(CommandEvent, Member, TextChannel, String[])` and `String info()`.
The first method **must** be implemented. It will be called in case an object of this class is registered as a command executor and its label gets called by a member of a guild 
the bot is on. So, an example implementation of `ICommand` might look like this:

```java
public class PingCommand implements ICommand {
    
    // Important!
    @Override
    public void onCommand(CommandEvent event, Member member, TextChannel channel, String[] args) {
        channel.sendMessage("Pong! " + member.getAsMention()).queue();
    }
    
    // Optional
    @Override
    public String info() {
        return "This command pongs you. Try it out!";
    }
}
```

As you can see, we've already implemented the other method here. The returned String will be displayed if you use the auto-generated help command of this API and someone 
requests help for this command. You'll see later how this works.

#### CommandSettings
Now, just creating an `ICommand` class won't do much - actually, nothing at all.
Imagine we want to have this command registered. This would perhaps look like this:

```java
// somewhere in our code
// "!" would be the default prefix, jda an instance of either JDA or ShardManager, true says that labels are case insensitive. 
// So it doesn't make a difference whether you type `!ping` or `!PING`
CommandSettings settings = new CommandSettings("!", jda, true); 
settings.put(new PingCommand(), "ping", "pingpong", "p").activate(); // You can add aliases by just giving more labels! And don't forget to activate these settings.
```

Simple, right? Don't let the method name `put` confuse you. Even though it has to do with a `Map`, `new PingCommand()` is not the key, but the value. It has been changed, because 
Varargs can only be used for the last argument. And yes, this is Varargs.
In this case, someone could write `!ping` in a channel the bot is also in and the bot would respond with `Pong! @Member`.
This person could also type `!pingpong` or `!p`. These are aliases.

What if we wanted to use this help command? Then we only have to adjust two things:
```java
CommandSettings settings = new CommandSettings("!", jda, true); 
settings.setHelpLabels("help", "helpme", "h") // Again: Varargs! label case insensivity also applies to help labels
        .put(new PingCommand(), "ping", "pingpong", "p")
        .activate();
```

If someone either calls `!help`, `!helpme` or `!h`, a list of all commands will be displayed along with the information that more help can be received by adding a command label 
as the first argument (e.g. `!help ping`). In this case, the content od the method `info()` will be shown. If this method is not overwritten, it will show the default text which
is "No info, description or help set for this command". If you think that this kind of help command is too basic or you dislike it for whatever reasons, just don't set any help 
labels and it will not be used. You can still make your own help command implementation of course.

Note that the help command doesn't have priority over normal commands. Or, to put it in another way: if a help label is the same as a normal command label, the normal command 
will be executed if this label is called. This also deals with case (in)sensitivity, so if you set `labelIgnoreCase` in the `CommandSettings` constructor to false, you could 
create a command with the label `h` whilst still being able to set `H` as a help label.

#### Custom Prefixes and other cool stuff
This API has supported guild-custom prefixes for some time now. By using `CommandSettings#setCustomPrefix` you can associate a prefix to a specific guild. Though it is important
to know that this is only saved temporarily, meaning as long as the program runs. If you want to apply changes like this permanently, you have to keep track of that manually.

Another thing which I personally find very cool is that you are able to easily deactivate your `CommandSettings` which also causes every command to stop working. To do so, 
simply use `CommandSettings#deactivate`. Of course you can activate the settings again with `CommandSettings#activate`. Note that this, of course, is not possible by using a 
command that belongs to this API. 

#### Exceptions
This API should only throw one kind of exception, `CommandSetException`. If this is **NOT** the case and you get another exception thrown by anything inside the API you don't have 
control over, [please report this here](https://github.com/JohnnyJayJay/discord-api-command/issues).

`CommandSetException` is a sub class of `RuntimeException`, meaning that they don't have to be caught and they don't terminate the program.

A `CommandSetException` is thrown if:
- a label or a prefix does not match the requirements, i.e. the regex defined in `CommandSettings.VALID_PREFIX` and `CommandSettings.VALID_LABEL`. This includes:
    - prefixes that contain one or more of the characters `\+*^|$?` or are just an empty String 
    - labels that contain any kind of blank spaces or are just an empty String
- an instance of `CommandSettings` is activated or deactivated twice (which is not possible)

If you don't want to have any exceptions concerning prefixes and labels, it is recommended to check whether they match `CommandSettings.VALID_PREFIY` 
or `CommandSettings.VALID_LABEL`.

```java
String prefix = // user input or something else you can't verify directly
if (prefix.matches(CommandSettings.VALID_PREFIX)) {
    // ...
} else {
    // Tell the user
}
```

## Requests and ideas
You can always request new features as well as ask questions [here](https://github.com/JohnnyJayJay/discord-api-command/issues). Or just add me on Discord (Johnny#3826) and we 
can talk there. But note that this API should stay simple. So unfortunately there won't be any built-in unicorn command. 
~~Sorry.~~

I hope that you like this project! I'm currently planning to add a changelog to keep track of the versions and what changes in the future. Because it's already been a huge jump 
from the 
last version to this one, and nobody should be confused about that.

Thanks for reading this shit to the bitter end. <p> __Have fun programming your Discord bot with JDA and CommandAPI!__





 