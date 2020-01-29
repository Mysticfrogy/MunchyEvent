package nl.hpfxd.munchyevent.features.crr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import nl.hpfxd.munchyevent.MUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CRRCommand  extends CommandBase {
    private Minecraft mc = Minecraft.getMinecraft();

    public static JSONObject map;
    private Map<NetworkPlayerInfo, Integer> spectateQueue = new ConcurrentHashMap<>();
    private List<String> spectators = new ArrayList<>(); // used for coloring /crr list

    @Override
    public String getCommandName() {
        return "crr";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "crr";
    }

    @Override
    public void processCommand(ICommandSender s, String[] args) throws CommandException {
        if ((args.length == 1 || args.length == 2) && args[0].equals("reset")) {
            JSONObject crrEvent = MUtil.getEvent("crr");
            if (crrEvent == null) throw new CommandException("commands.generic.exception");

            spectators.clear();
            spectateQueue.clear();
            map = null;

            if (args.length == 1) {
                MUtil.chat("Reset.");
            } else {
                JSONObject map = MUtil.getMap(crrEvent, args[1]);

                if (map == null || !map.has("locations")) {
                    MUtil.chat("Map is not supported.");
                    MUtil.chat("If you'd like support for this map, please tell &ehpf&7!");
                } else {
                    MUtil.chat("Map set to &e" + map.getString("name"));
                    CRRCommand.map = map;

                    MUtil.chat("Make sure you have a 'crr' skit!");
                }
            }

            CRRTeam.red.reset();
            CRRTeam.blue.reset();
        } else if (args.length == 3 && args[0].equals("captain")) {
            CRRTeam team = CRRTeam.getTeamByName(args[1]);

            if (team != null) {
                CRRPlayer player = CRRPlayer.getPlayerByNameOrCreate(args[2]);

                if (player == null) {
                    MUtil.chat("&cUnable to locate player.");
                    return;
                }

                CRRTeam.setPlayerTeam(team, player);
                team.setCaptain(player);
                MUtil.chat("Moved &e" + player.getName() + " &7to " + team.getColor() + team.getName() + " &7as captain.");
            } else {
                MUtil.chat("&cInvalid team.");
            }
        } else if (args.length == 3 && args[0].equals("pick")) {
            CRRTeam team = CRRTeam.getTeamByName(args[1]);

            if (team != null) {
                CRRPlayer player = CRRPlayer.getPlayerByNameOrCreate(args[2]);

                if (player == null) {
                    MUtil.chat("&cUnable to locate player.");
                    return;
                }

                CRRTeam.setPlayerTeam(team, player);
                MUtil.chat("Moved &e" + player.getName() + " &7to " + team.getColor() + team.getName() + "&7.");
            } else {
                MUtil.chat("&cInvalid team.");
            }
        } else if (args.length == 2 && args[0].equals("unpick")) {
            CRRPlayer player = CRRPlayer.getPlayerByName(args[1]);

            if (player == null) {
                MUtil.chat("&cUnable to locate player.");
                return;
            }

            player.removeTeam();
            MUtil.chat("Removed &e" + player.getName() + " &7from all teams.");
        } else if ((args.length == 2 || args.length == 3) && args[0].equals("fight")) {
            CRRPlayer player = CRRPlayer.getPlayerByName(args[1]);

            if (player == null) {
                MUtil.chat("&cUnable to locate player.");
                return;
            }

            CRRTeam team = CRRTeam.getTeam(player);

            if (team == null) {
                MUtil.chat("&cUnable to locate team.");
                return;
            }

            if (args.length != 3) player.onFight(); // tp only

            MUtil.chat(team.getColor() + player.getName() + "&7: &e" + player.getFightsWon() + " &7fights won.");
            mc.thePlayer.sendChatMessage("/tp " + player.getName() + " " + MUtil.locationToString(team.getLocations().getJSONArray("fight")));
        } else if (args.length == 1 && args[0].equals("tpcaptains")) {
            if (CRRTeam.red.getCaptain() != null) mc.thePlayer.sendChatMessage("/tp " + CRRTeam.red.getCaptain().getName() + " " + MUtil.locationToString(CRRTeam.red.getLocations().getJSONArray("captain")));
            if (CRRTeam.blue.getCaptain() != null)  mc.thePlayer.sendChatMessage("/tp " + CRRTeam.blue.getCaptain().getName() + " " + MUtil.locationToString(CRRTeam.blue.getLocations().getJSONArray("captain")));
            MUtil.chat("Teleported team captains.");
        } else if (args.length == 1 && args[0].equals("list")) {
            MUtil.chat("&c&lRed &7(" + (CRRTeam.red.getPlayers().size()) + "&7)");
            for (CRRPlayer player : CRRUtils.sortByKills(CRRTeam.red.getPlayers())) {
                if (CRRTeam.red.getCaptain() == player) { // bold captain
                    MUtil.chat("&7- &c&l" + player.getName() + " &7(" + player.getFightsWon() + ")");
                } else if (spectators.contains(player.getName()) || MUtil.getEntityPlayerByName(player.getName()) == null) { // gray specs
                    MUtil.chat("&7- &7" + player.getName() + " &7(" + player.getFightsWon() + ")");
                } else {
                    MUtil.chat("&7- &c" + player.getName() + " &7(" + player.getFightsWon() + ")");
                }
            }

            MUtil.chat("&9&lBlue &7(" + (CRRTeam.blue.getPlayers().size()) + "&7)");

            for (CRRPlayer player : CRRUtils.sortByKills(CRRTeam.blue.getPlayers())) {
                if (CRRTeam.blue.getCaptain() == player) { // bold captain
                    MUtil.chat("&7- &9&l" + player.getName() + " &7(" + player.getFightsWon() + ")");
                } else if (spectators.contains(player.getName()) || MUtil.getEntityPlayerByName(player.getName()) == null) { // gray specs
                    MUtil.chat("&7- &7" + player.getName() + " &7(" + player.getFightsWon() + ")");
                } else {
                    MUtil.chat("&7- &9" + player.getName() + " &7(" + player.getFightsWon() + ")");
                }
            }
        } else if (args.length == 2 && args[0].equals("spec")) {
            NetworkPlayerInfo player = MUtil.getNetworkPlayer(args[1]);

            if (player == null) {
                MUtil.chat("&cUnable to locate player.");
                return;
            }

            EntityPlayer p = MUtil.getEntityPlayerByName(player.getGameProfile().getName());

            if (p != null) {
                mc.thePlayer.sendChatMessage("/tp " + p.getName() + " " + MUtil.locationToString(map.getJSONObject("locations").getJSONArray("spectate")));
                mc.thePlayer.sendChatMessage("/skit apply w " + p.getName());

                MUtil.chat("Teleported &e" + player.getGameProfile().getName() + "&7.");
            } else {
                mc.thePlayer.sendChatMessage("/eventtphere " + player.getGameProfile().getName()); // event members can't force tp across worlds
                spectateQueue.put(player, 15 * 20); // 15 seconds

                MUtil.chat("Sent teleport request to &e" + player.getGameProfile().getName() + "&7.");
            }
        } else {
            this.sendHelp();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        for (NetworkPlayerInfo player : spectateQueue.keySet()) {
            int t = spectateQueue.get(player);

            if (t == 0) {
                spectateQueue.remove(player);
            } else {
                spectateQueue.put(player, t - 1);

                EntityPlayer p = MUtil.getEntityPlayerByName(player.getGameProfile().getName());

                if (p != null) { // player accepted tp request
                    mc.thePlayer.sendChatMessage("/tp " + p.getName() + " " + MUtil.locationToString(map.getJSONObject("locations").getJSONArray("spectate")));
                    mc.thePlayer.sendChatMessage("/skit apply w " + p.getName());

                    spectateQueue.remove(player);

                    MUtil.chat("Teleported &e" + player.getGameProfile().getName() + "&7.");
                }
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender s) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "reset", "captain", "pick", "unpick", "tpcaptains", "fight", "list", "spec");
        } else if (args.length == 2) {
            if (args[0].equals("captain") || args[0].equals("pick")) {
                return getListOfStringsMatchingLastWord(args, "red", "blue");
            } else if (args[0].equals("fight") || args[0].equals("unpick") || args[0].equals("spec")) {
                return getListOfStringsMatchingLastWord(args, MUtil.getAllPlayerNames());
            }
        } else if (args.length == 3) {
            if (args[0].equals("captain") || args[0].equals("pick")) {
                return getListOfStringsMatchingLastWord(args, MUtil.getAllPlayerNames());
            }
        }

        return null;
    }

    private void sendHelp() {
        MUtil.chat("&cInvalid usage.");
        MUtil.chat("/crr reset <map id> &eReset the event with specified map.");
        MUtil.chat("/crr captain <team> <name> &eSet captain");
        MUtil.chat("/crr pick <team> <name> &eAdd player to team");
        MUtil.chat("/crr unpick <name> &eRemove player's team");
        MUtil.chat("/crr tpcaptains &eTeleport captains to their pods.");
        MUtil.chat("/crr fight <name> &eTeleport player to fighting pod and apply crr skit");
        MUtil.chat("/crr list &eLists all players.");
    }
}
