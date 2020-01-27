# Classic Red Rover
MunchyEvent has a feature to make hosting CRR much easier.  
If a map is not supported, please message [hpf](https://hpfxd.nl/) the location and name of the map you'd like to be supported.

## Commands
- **/crr reset** Clear game data.
- **/crr reset &lt;map id&gt;** Clear game data, and set the current map to *map*.
- **/crr captain &lt;team&gt; &lt;player&gt;** Set the captain of *team* to *player*.
- **/crr pick &lt;team&gt; &lt;player&gt;** Set the team of *player* to *team*.
- **/crr unpick &lt;player&gt;** Remove team of *player*.
- **/crr tpcaptains** Teleport the captains to their locations.
- **/crr fight &lt;player&gt;** Teleport player to fighting pod, and apply crr skit. This will also keep track of kills. Every 3 kills = refill.
- **/crr list** List players on both teams with the amount of kills they have.
- **/crr spec &lt;player&gt;** Request for the player to be teleported to you. Once they */confirm*, they will be teleported to the spectator box.

## Hosting
- Use **/crr reset &lt;map id&gt;** to let the mod know what map you'll be using.
- Create an skit with the name 'crr'. This is the kit players will fight with.
- Make sure PvP is on, and picking up items is off using **/togglepvp** and **/togglepickup**.
- Use **[/mhost](../#mhost)** to start hosting the event and teleport players.
- Pick 2 captains with the **/crr captain** command. This is usually done by whoever types something in chat first.
- Tell a captain to pick 1-2 players. Once they do, use **/crr pick** to teleport them. Repeat this until there are no more players.
- Use **/crr tpcaptains** to teleport the captains to their special location.
- Ask the captains to message you who their first picks are. Then use **/crr fight** to teleport the players.
___
- Use **/broadcast Fight!** and open the gates to let the players fight.
- Once both players have left their pods, close the gate.
- Once a player dies, use **/crr fight** on the winning player to store their win and teleport them back into the fighting pod.
- Ask the losing player's captain who their next pick is and use **/crr fight** on them.
- Repeat this until there are no players left, and a team wins.
___
Once an event is over, use **/crr reset** to reset the game data.