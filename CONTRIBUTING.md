# Contributing

## Issues

Found an issue? Great, you can report bugs and crashes by opening an issue on
our [issue tracker](https://github.com/AdvancedWipe/Minecraft_FreeRPG_1.16/issues). Before opening a
new issue, use the search tool to make sure that your issue has not already been reported and ensure
that you have completely filled out the issue template. Issues that are duplicates or do not contain
the necessary information to triage and debug may be closed.

Please provide the following details in your issue:

* The exact version of the plugin you are running and the server version you are using.
* If your issue is a crash, attach the latest log which include the crash you are referencing to.
* If your issue is a bug or otherwise unexpected behavior, explain what you expected to happen.
* If your issue only occurs with other plugins installed, be sure to specify the names and versions
  of those.

## Balancing

Want to contribute balancing changes to the default config? Great, please open an issue and let us 
know about your changes. We will be happy to integrate them.

## Translation

You want to translate FreeRPG to a different language? Great, clone this repository or download the 
[languages.yml](https://github.com/AdvancedWipe/Minecraft_FreeRPG_1.16/blob/master/src/main/resources/languages.yml) file.
Open it using a texteditor or an IDE.
Start by coping for example the english translation (check out the 
[link](https://github.com/AdvancedWipe/Minecraft_FreeRPG_1.16/blob/2301f9fbbea164e581973130d62509f891cb0103/src/main/resources/languages.yml#L4-L634) 
for the marked section of a file that needs to be copied for a full translation) to the bottom of the file.
Replace `enUS:` by your country and start translating the values of the key. A key value pair is 
defined by `variableName: Variable Name` for example `languageName: English`, `languageName` is the 
variable name and `English` is the translation. In other words you always need to replace the words 
next to the variable name with your translation.

## Adding Blocks and Entities from newer Minecraft versions to FreeRPG

Most new blocks, items and entities can be simply added by adding them to the different configuration 
files. Afterwards some changes need to be done to the source code to actually apply some of these changes. 
Check out these two commits ([2455c8c](https://github.com/AdvancedWipe/Minecraft_FreeRPG_1.16/commit/2455c8c45134a0cd2392e096fd17ac357ee8c35c), [21cda30](https://github.com/AdvancedWipe/Minecraft_FreeRPG_1.16/commit/21cda30e69d3b567a295c2ba9b07098de1dc0540))
to get a rough understanding where and how to add them.

## Source code

Want to contribute changes to the source code? Set up your development environment as described in 
the [spigot wiki](https://www.spigotmc.org/wiki/spigot-plugin-development/). Please make sure your 
changes are following .editorconfig style, let your IDE format your changes to ensure that. Open a pull request with
your changes, and we will happily integrate them.

