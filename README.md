# FreeRPG

![GitHub license](https://img.shields.io/github/license/AdvancedWipe/Minecraft_FreeRPG_1.16.svg)
![GitHub issues](https://img.shields.io/github/issues/AdvancedWipe/Minecraft_FreeRPG_1.16.svg)
![GitHub tag](https://img.shields.io/github/tag/AdvancedWipe/Minecraft_FreeRPG_1.16.svg)

FreeRPG is a spigot 1.15+ plugin that adds an RPG skill system to minecraft. Inspired by Mcmmo, the
goal is to offer a free Mcmmo alternative. Each skill has a skill tree, with an additional global
skill tree. The skill trees can be accessed with a very simplistic, Hypixel inspired GUI accessed
using ``/frpg``.

## Installation

### Current Updates

Current version is 1.4.52, check the tags/releases on GitHub for a current version or build it
yourself from source.

## Getting Started

To install on any Spigot server, simply drag the ``freerpg-{version}.jar`` file into the plugins
folder.

To get started playing, type ``/frpg``. This will bring up the Skills GUI seen in the gif above.
Clicking on any of the icons will take you to that skill's skill tree. Hovering over any of the
icons or clicking on them will tell you more about the perk.

Earning experience is straightforward, mine to get mining experience, cut trees to get woodcutting
experience, etc. By the default config, every level you will gain **1 passive token** which can be
invested in the passive skills represented by the dyes on the left. Every 100 levels, you will
gain **1 skill token** which can be invested in the skills represented by the terracotta skill tree.
Every 1000 levels, you will gain **1 global token** that can be used in the global skill tree.

To activate abilities, in most cases you can right click the corresponding tool then break a block.
The 10 abilities are as follows:

* **Big Dig**: Increases dig speed drastically.
    * Activated by right clicking a shovel then breaking a block.
* **Timber**: Will cause all logs above to break.
    * Activated by right clicking an axe then cutting a log.
* **Berserk Pick**: Will increase mining speed drastically.
    * Activated by right clicking a pickaxe then breaking a block.
* **Natural Regeneration**: Automatically replants all crops at a random growth stage.
    * Activated by right clicking a hoe then breaking a crop.
* **Super Bait**: Fish are instantly pulled out of the water, without waiting to get a bite.
    * Activated by left clicking a fishing rod then fishing.
* **Rapid Fire**: Arrow shots come out at maximum speed no matter how far the bow is pulled back.
    * Activated by left clicking a bow.
* **Spur Kick**: Grants your horse a temporary speed boost.
    * Activated by right clicking most items while on a horse then left clicking most items.
* **Swift Strikes**: Your swings no longer have a cooldown.
    * Activated by right clicking a sword then hitting an entity.
* **Stone Solid**: Grants resistance at the cost of being slowed down.
    * Activated by right clicking with an empty hand then hitting an entity.
* **Great Axe**: Your attacks become an AOE strike that deals damage in a certain radius.
    * Activated by right clicking an axe then hitting an entity.

For further features and information on configuration, permissions and usage check out
the [wiki](https://github.com/AdvancedWipe/Minecraft_FreeRPG_1.16/wiki).

## Compatibility

FreeRPG should be compatible with most popular plugins. It has been tested with Essentials,
Bssentials, World Edit, Worldguard, Silk Spawners, and PlaceholderAPI and yielded no issues. It has
additionally been tested with Multiverse and has no major issues. However, player profiles for
different worlds is not possible at the moment.

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
please open a pull request with the added language.

## Contribute

### Tasks to be done

- [ ] Clean up code
- [ ] Reduce code duplication
- [ ] Simplify methods, so code is more readable


### Reporting Issues

You can report bugs and crashes by opening an issue on
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

### Licence

FreeRPG is licensed under MIT license, a free and open-source license. For more information, please
see the [license](LICENSE) file.
