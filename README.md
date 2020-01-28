# MunchyEvent
![Java CI](https://github.com/hpfxd/MunchyEvent/workflows/Java%20CI/badge.svg?branch=master)

Mod to help MunchyMC Event Team members.

## Highlight Features
- No more having to go through a long list of eventspots to find a map, now you can just use [/events](#events)
- Use [/events reward](#events-reward) to easily reward a player for an event.
- Easily teleport players and apply skits for manual events with [/mhost](#mhost)
- Tired of using a notes application for keeping track of players in events? Now you can just use [/pnote](#pnote)
- Easily host Classic RR events with [/crr](https://hpfxd.nl/MunchyEvent/docs/crr)
- A command to enchant items has been a requested feature for the event team for a long time. Now you can use [/mitem enchant](#mitem-enchant-enchantment-level)
- Rename items using [/mitem rename](#mitem-rename-name)

## Installation
- Install [Minecraft Forge](https://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.8.9.html) for 1.8.9.
- Download the jar file from the [latest release](https://github.com/hpfxd/MunchyEvent/releases/latest) of the MunchyEvent mod.
- Put the jar file in your 1.8.9 mods folder.
- Start Minecraft with 1.8.9 forge.

## Commands
### /events
Open a GUI to select an event from a list.  
Once you select an event, it will show a description and allow you to teleport to a list of maps for that event.
### /events reward
Reward a player for winning an event.  
Once you enter the relevant info in the GUI, press the "copy" button to copy the message to your clipboard.
You can then paste this message into the `#event-records` channel to reward the player.
### /mitem enchant &lt;enchantment&gt; [level]
Enchant the item you are holding with an enchantment with the specified level.  
The level must be in the range of 1-127.  
*Example: /mitem enchant sharpness 100*
### /mitem rename &lt;name&gt;
Rename the item in your hand.
You can use [formatting codes](https://minecraft.gamepedia.com/Formatting_codes) in the item name to color it.   
Using `/mitem rename rainbow <name>` will rename the item with colors cycling from a-e.  
*Example: /mitem rename &c&lepic name*
### /mhost
Open a GUI to host a manual event.  
Once you fill in the GUI, the mod will automatically send 4 teleportation requests, and apply the skit provided.
### /pnote
Player notes are a useful feature for many events where you have to keep track of players.  
  - **/pnote**
  List all player notes.
  - **/pnote save**
  Clear all player notes, then save everyone within your render distance with an empty description. (useful for games such as waterdrop)
  - **/pnote &lt;player&gt; [note]**
  Save a player with specified note.
  - **/pnote &lt;player&gt;**
  If the player has a note, print it. If not, save the player with no note.
  - **/pnote clear**
  Clear all player notes.

### /crr
See [this page](https://hpfxd.nl/MunchyEvent/docs/crr) for more info about this command.