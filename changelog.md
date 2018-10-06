# Changelog

### 3.2_01
- Increased performance of CommandListener slightly
- Added configurable Predicate to `CommandSettings` which tests an event before execution. This may be useful for own checks that are not provided by this framework
- Added "listeners" for unknown commands (`CommandSettings#onUnknownCommand(Consumer<CommandEvent>)`) and Throwables (`CommandSettings#onException(BiConsumer<CommandEvent, Throwable>)`)
- Added possibility to configure a custom thread pool (`CommandSettings#useMultiThreading(ExecutorService)`)
- Added configurable messages that are sent in case someone is on cooldown or a command is unknown
- Added possibility to deactivate Exception logging (`CommandSettings#setLogExceptions(boolean)`)
- Added Regex util class (mainly for `AbstractCommand`)
- Added `CommandEvent.Command#getPrefix()` to get the prefix used in a command
- Changed Command parsing
- Adjusted `AbstractCommand`'s code - made it more stream-like
- Fixed bug in `DefaultHelpCommand` that prevented the general help message from working
- Corrected mistake in documentation of `SubCommand#moreArgs()`
- Updated JDA version to 3.8.0_433
- Updated readme

### 3.2
- Updated to JDA version 3.7.1_387
- Adjusted cooldown system: it is now configurable whether the cooldown will reset for each command execution attempt
- Deprecated help label methods: They will be removed in the near future as the help label system is merged with the normal command system
- Changed return type of some `CommandSettings` methods from void to `CommandSettings` to make fluent interface possible
- Added method `setCustomPrefixes` in `CommandSettings` to bulk add custom prefixes for better performance
- Deprecated `CommandEvent.Command#getJoinedArgs(int)` - it will be removed in the near future, there are better methods now.
- Added getFirstMention-methods to CommandEvent
- Added method `CommandEvent#respond` with overloads
- Improved Parsing in `CommandEvent.Command`
- Added method `CommandSettings#getLabels(ICommand)` to get a Set of labels for a specific command
- Added more fields to `CommandEvent.Command`
- Added method `isBlacklisted(long)` to `CommandSettings`
- Added classes AbstractHelpCommand and DefaultHelpCommand
- Added method `getCommandSettings` in `CommandEvent`
- Annotated `ICommand` as a `FunctionalInterface`
- Cleaned `CommandListener`
- Deprecated method `String info(Member)` in `ICommand`;
    New method is `Message info(Member, String, Set<String>)` because it is more flexible.
- Added annotation `SubCommand`
- Added class `AbstractCommand` with sub command system
- Added configurable unknown command message
- Made certain getters of `CommandSettings` public
- Improved documentation and readme

### 3.1
- Changed setCooldown-method's return to `this`
- Added possibility to change help labels (by clearing them or removing specific ones)
- Added possibility to clear all commands
- Added possibility to set the embed color for the help command with `CommandSettings#setHelpCommandColor(Color)`
- Added methods to remove Collections of labels from the commands
- Added methods to add Collections of labels to the commands
- Added public Getters for the registered labels, help labels and the boolean `activated`
- Added first steps for **logging**
- Changed the split regex for arguments from " " to "\\s+" (it doesn't matter how many spaces there are now)
- Changed the help command listing type
- Added some possibly-helpful  methods to class `Command`, such as joining arguments
- Added channel blacklist
- Fixed an issue where help labels case sensitivity ignored whether you would activate `labelIgnoreCase`
- Cleaned some code and fixed some minor bugs

### 3.0_3
- Changed parameters of info-method from Guild to Member
- Added possibility to set command cooldown (either in the constructor of `CommandSettings` or in its setter)

### 3.0_2
- Added permissions check before printing help messages
- Added argument to `ICommand`-method `String info()` - the guild (to make it possible to get the custom prefix)
- Made class `Command` inner class of `CommandEvent`, because it not really has a usage anywhere else
- Added changelog (lul)

### 3.0_1
- Added proper Readme file
- Removed option `useHelpCommand` from the `CommandSettings` constructor. If you want to use the help command, simply set your help labels.

### 3.0
- **Added Maven support**
- Added support for guild-specific prefixes
- Added support for JDA **and** ShardManager
- Fixed problems with regex patterns and added `CommandSettings.VALID_PREFIX` and `CommandSettings.VALID_LABEL`
- Cleaned some code in `CommandListener`

### 2.9
- Added possibility to generate an automated help command
- Added methods in `CommandSettings` for the usage of aliases
- Added default method `String info()` in `ICommand`
- Changed `void onCommand` in `ICommand`: added `Member member` as parameter

### 2.5
- Changed structure of command parsing; added class `Command`
- Removed `CommandHandler` and `CommandContainer`
- Made `CommandSetException` public
- Added method `remove` to `CommandSettings`
- Added method `deactivate` to `CommandSettings`; overall different structure than before
- Changed `@NotNull` annotations to `@Nonnull` annotations

### 1.6
- Added `CommandSetException`
- Fixed multiple activation of `CommandSettings`
- Improved command parsing in class `CommandContainer`

### 1.4
- Added `@NotNull` annotations to `CommandSettings`

### 1.3
- Removed method `boolean canBeExecuted` from `ICommand`
- Fixed possibility for bots to execute commands
- Removed method `MessageEmbed info` from `ICommand`

### 1.1
- Created CommandAPI