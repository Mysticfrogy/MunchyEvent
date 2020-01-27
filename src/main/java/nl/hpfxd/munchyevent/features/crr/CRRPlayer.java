package nl.hpfxd.munchyevent.features.crr;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import nl.hpfxd.munchyevent.MUtil;

import java.util.UUID;

public class CRRPlayer {
    private Minecraft mc = Minecraft.getMinecraft();

    private UUID uuid;
    private String name;
    private int fightsWon;
    private boolean flag = false;

    public CRRPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getFightsWon() {
        return this.fightsWon;
    }

    public void setTeam(CRRTeam team) {
        CRRTeam.setPlayerTeam(team, this);
    }

    public void removeTeam() {
        CRRTeam.red.removePlayer(this);
        CRRTeam.blue.removePlayer(this);

        mc.thePlayer.sendChatMessage("/tp " + this.getName() + " " + MUtil.locationToString(CRRCommand.map.getJSONArray("location"))); // teleport player to waiting area
    }

    public void onFight() {
        if (flag) {
            fightsWon++;
        } else {
            flag = true;
        }

        if (fightsWon % 3 == 0) { // fights won is a multiple of 3, give refill
            mc.thePlayer.sendChatMessage("/skit apply crr " + this.name);
            MUtil.chat("Gave &e" + this.name + " &7refill.");
        }
    }

    public static CRRPlayer getPlayerByName(String name) {
        CRRPlayer redp = getPlayerOnTeamByName(name, CRRTeam.red);
        CRRPlayer bluep = getPlayerOnTeamByName(name, CRRTeam.blue);

        return redp != null ? redp : bluep;
    }

    public static CRRPlayer getPlayerByNameOrCreate(String name) {
        CRRPlayer redp = getPlayerOnTeamByName(name, CRRTeam.red);
        CRRPlayer bluep = getPlayerOnTeamByName(name, CRRTeam.blue);
        EntityPlayer player = MUtil.getEntityPlayerByName(name);

        if (redp != null) return redp;
        else if (bluep != null) return bluep;
        else if (player != null) return new CRRPlayer(player.getUniqueID(), player.getName());
        else return null;
    }

    private static CRRPlayer getPlayerOnTeamByName(String name, CRRTeam team) {
        for (CRRPlayer player : team.getPlayers()) {
            if (player.getName().equalsIgnoreCase(name)) return player;
        }

        return null;
    }
}
