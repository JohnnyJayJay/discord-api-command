# Changelog

### 3.1
- Changed setCooldown-method's return to `this`
- Added possibility to change help labels (by clearing them or removing specific ones)
- Added possibility to clear all commands
- Added methods to remove Sets of labels from the commands
- Added public Getters for the registered labels, help labels and the boolean `activated`
- Added first steps for **logging**
- Changed the split regex for arguments from " " to "\\s+" (it doesn't matter how many spaces there are now)
- Changed the help command listing type
- Added some possibly-helpful  methods to class `Command`
- Fixed an issue where help labels ignored whether you would activate `labelIgnoreCase`
- Cleaned some code

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