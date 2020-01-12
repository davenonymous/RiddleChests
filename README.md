# Riddle Chests

## Description

A homage to the old role playing game [Betrayal at Krondor](https://en.wikipedia.org/wiki/Betrayal_at_Krondor).
Quoting the wiki entry:

One of the game's unique features are the large assortment of Moredhel wordlock chests, hidden throughout the land.
These chests have combination locks with letters on each dial, and a riddle written upon them whose answer opens the
chest. Wordlock chests can hold valuable items and equipment.

This mod adds chests similiar to those in Betrayal at Krondor to the minecraft world.


## World Gen and Chests

The chance that a vanilla loot chest will be replaced by a Riddle Chest can be configured in the options. No additional
chests will be generated.

The system behind this currently requires a Mixin/CoreMod to detect generated lootChests. If modded structures
do not use the `StructurePiece.generateChest()` methods we have currently no efficient way of detecting their
chests and can not replace them.

If you want certain loot chests to never be touched you can prevent that by configuring the `loottableBlacklist`
option and include the resource name of the loottable you want to keep intact, e.g. `minecraft:chests/spawn_bonus_chest`.


## How to add more riddles/quizzes/trivia

This is quite easy and you can find many categories in the datapacks folder.

Essentially you want to do this:

- Grab and install the [Open Loader](https://www.curseforge.com/minecraft/mc-mods/open-loader) mod from CurseForge
- Start the game once so that the proper directories will be created for you
  OR create the folder `<minecraft-instance>/openloader/data` manually.
- Copy one of the zips from the datapacks folder in this repository to the `openloader/data` folder


## No, I meant, how do I add my own riddles!

This is slightly more complicated, but if you are handy with .json files you will have no problems.
There are many examples you can look at in the datapacks folder in this repository.

You need to do at least two things:

### Create a riddle .json file that looks something like this

```json
{
   "type": "riddlechests:word",
   "category": "riddlechests:your_own_category",
   "lang": "en_us",
   "original": "ABASD BTDXU",
   "riddle": [
      "The first thing you do",
      "when you test something new"
   ],
   "solution": "HELLO WORLD",
}
```
Place this file in the `openloader/data/riddlechests/recipes/riddles/your_own_category/` directory.
Repeat this step for each riddle you want to create.


### Create a loot table mapping between your category and an existing loot table

```json
{
  "type": "riddlechests:loottable_mapping",
  "category": "riddlechests:your_own_category",

  "lootTables": [
    "riddlechests:default_loot",
    "minecraft:chests/abandoned_mineshaft"
  ]
}
```
Place this file in the `openloader/data/riddlechests/recipes/loottable_mappings/` directory.