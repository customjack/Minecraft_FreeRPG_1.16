# FreeRPG

![GitHub license](https://img.shields.io/github/license/AdvancedWipe/Minecraft_FreeRPG_1.16.svg)
![GitHub issues](https://img.shields.io/github/issues/AdvancedWipe/Minecraft_FreeRPG_1.16.svg)
![GitHub tag](https://img.shields.io/github/tag/AdvancedWipe/Minecraft_FreeRPG_1.16.svg)

This is a fork of the Spigot plugin with the equivalent name, for which further development was discontinued. FreeRPG is a spigot 1.15+ plugin that adds an RPG skill system to Minecraft. Inspired by Mcmmo, the
goal is to offer a free Mcmmo alternative. Each skill has a skill tree, with an additional global
skill tree. The skill trees can be accessed with a very simplistic, Hypixel inspired GUI.

## Download

* [Download on Modrinth](https://modrinth.com/plugin/freerpg)
* [Download on Github Releases](https://github.com/AdvancedWipe/Minecraft_FreeRPG_1.16/releases)

## Getting Started

![FreeRPG GUI](https://cdn-raw.modrinth.com/data/m3YFnJQM/images/c896cb6a3921b4687e9a45ba878ebdfff39bab38.png)

To get started playing, type ``/frpg``. This will bring up the GUI seen in the screenshot above.
Clicking on any of the icons will take you to that skill's skill tree. Hovering over any of the
icons or clicking on them will tell you more about the perk as shown in the screenshot below.

![](https://cdn-raw.modrinth.com/data/m3YFnJQM/images/bb28b3843f16076c7c07b5c4d30650a7adc314d1.png)

Earning experience is straightforward, mine blocks to get mining experience, cut trees to get woodcutting
experience, etc.

By the default configuration, each level you finish, will gain **1 passive token** which can be
invested in the passive skills represented by the dyes on the left. Every 100 levels, you will
gain **1 skill token** which can be invested in the skills represented by the terracotta skill tree.
Every 1000 levels, you will gain **1 global token** that can be used in the global skill tree.
Please check the wiki, for further information on how to configure FreeRPG to your needs.

FreeRPG provides 10 abilities to the player which can be leveled.

| Name                     | Related Skill | Description                                                                     | Activation                                                                 | Duration by default |
|--------------------------|---------------|---------------------------------------------------------------------------------|----------------------------------------------------------------------------|---------------------|
| **Big Dig**              | Digging       | Increases dig speed drastically.                                                | Right-clicking a shovel, then breaking a block.                            | 300 sec             |
| **Timber**               | Woodcutting   | Will cause all logs above to break.                                             | Right-clicking a pickaxe, then breaking a block.                           | 300 sec             |
| **Berserk Pick**         | Mining        | Will increase mining speed drastically.                                         | Right-clicking a pickaxe, then breaking a block.                           | 300 sec             |
| **Natural Regeneration** | Farming       | Automatically replants all crops at a random growth stage.                      | Right-clicking a hoe, then breaking a crop.                                | 300 sec             |
| **Super Bait**           | Fishing       | Fish are instantly pulled out of the water, without waiting to get a bite.      | Left-clicking a fishing rod, then fishing.                                 | 300 sec             |
| **Rapid Fire**           | Archery       | Arrow shots come out at maximum speed no matter how far the bow is pulled back. | Left-clicking a bow.                                                       | 300 sec             |
| **Spur Kick**            | Beast Mastery | Grants your horse a temporary speed boost.                                      | Right-clicking most items while on a horse, then left-clicking most items. | 300 sec             |
| **Swift Strikes**        | Swordsmanship | Your swings no longer have a cooldown.                                          | Right-clicking a sword then hitting an entity.                             | 300 sec             |
| **Stone Solid**          | Defense       | Grants resistance at the cost of being slowed down.                             | Right-clicking with an empty hand, then hitting an entity.                 | 300 sec             |
| **Great Axe**            | Axe Mastery   | Your attacks become an AOE strike that deals damage in a certain radius.        | Right-clicking an axe, then hitting an entity.                             | 300 sec             |

For further features and information on configuration, permissions and usage check out
the [wiki](https://github.com/AdvancedWipe/Minecraft_FreeRPG_1.16/wiki).

## Language Support

FreeRPG fully/partially supports the following languages by default:

- 🇺🇸 English
- 🇭🇺 Hungarian (Translated by: vERKE)
- 🇫🇷 French (Translated by: Temuel)
- 🇩🇪 German (Translated by: KlenerTeufel96 of FruitLab.gg)
- 🇵🇱 Polish (Translated by: QuarVey)
- 🇪🇸 Spanish (Translated by: PibeChileno)
- 🇷🇺 Russian (Translated by: Cr1stalz_, MoKotik, and DevilPlay)
- 🇵🇹 Portuguese (Translated by: gbuueno)
- 🇨🇿 Czech (Translated by: Pieck444 and Fractvival)
- 🇰🇷 Korean (Translated by: Re_Oh)

The default server language can be set in config.yml. Additionally, each player can pick which
language they want the plugin to be represented in. Server administrators can add their own
languages to languages.yml. If you want to add your translation to be hardcoded into languages.yml,
please open a pull request with the added language. For further information check the [CONTRIBUTING](https://github.com/AdvancedWipe/Minecraft_FreeRPG_1.16/blob/master/CONTRIBUTING.md) file in this repository

## Wiki

The [wiki](https://github.com/AdvancedWipe/Minecraft_FreeRPG_1.16/wiki) provides you with detailed information about the abilities, skill trees and plugin configuration.

## Compatibility

FreeRPG should be compatible with most popular plugins. It has been tested with Essentials,
Bssentials, World Edit, Worldguard, Silk Spawners, and PlaceholderAPI and yielded no issues. It has
additionally, been tested with Multiverse and has no major issues. However, player profiles for
different worlds are not possible at the moment.

## Links

* [Wiki](https://github.com/AdvancedWipe/Minecraft_FreeRPG_1.16/wiki)
* [Modrinth](https://modrinth.com/plugin/freerpg)
* [Github](https://github.com/AdvancedWipe/Minecraft_FreeRPG_1.16)

## Contribute

You want to contribute to this plugin by providing a translation, balancing changes or source code 
changes? Great, please check the [CONTRIBUTING](https://github.com/AdvancedWipe/Minecraft_FreeRPG_1.16/blob/master/CONTRIBUTING.md) 
file in this repository for further information.

### Reporting Issues

You can report bugs, crashes or any other issue you found by opening a new issue on
our [issue tracker](https://github.com/AdvancedWipe/Minecraft_FreeRPG_1.16/issues). Before opening a
new issue, use the search tool to make sure that your issue has not already been reported and ensure
that you have completely filled out the issue template.

Please provide the following details in your issue:

* The exact version of the plugin you are running and the server version you are using.
* If your issue is a crash, attach the latest log which include the crash you are referencing to.
* If your issue is a bug or otherwise unexpected behavior, explain what you expected to happen.
* If your issue only occurs with other plugins installed, be sure to specify the names and versions
  of those.

## Licence

FreeRPG is licensed under MIT license, a free and open-source license. For more information, please
see the [license](LICENSE) file.








