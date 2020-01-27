package nl.hpfxd.munchyevent.features.crr;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import nl.hpfxd.munchyevent.MUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CRRTeam {
    public static CRRTeam red;
    public static CRRTeam blue;

    private static Minecraft mc = Minecraft.getMinecraft();

    private String id;
    private String name;
    private EnumChatFormatting color;
    private List<CRRPlayer> players = new ArrayList<>();
    private CRRPlayer captain;
    private JSONObject locations;

    public CRRTeam(String id, String name, EnumChatFormatting color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public EnumChatFormatting getColor() {
        return this.color;
    }

    public void addPlayer(CRRPlayer player) {
        this.players.add(player);
    }

    public void removePlayer(CRRPlayer player) {
        if (this.getCaptain() == player) this.setCaptain(null);
        this.players.remove(player);
    }

    public CRRPlayer getCaptain() {
        return this.captain;
    }

    public void setCaptain(CRRPlayer captain) {
        this.captain = captain;
    }

    public List<CRRPlayer> getPlayers() {
        return this.players;
    }

    public JSONObject getLocations() {
        return this.locations;
    }

    public void reset() {
        this.players.clear();
        this.captain = null;

        if (CRRCommand.map != null) this.locations = CRRCommand.map.getJSONObject("locations").getJSONObject(this.id);; // map might have changed
    }

    /*
    * Utility methods
    */

    public static CRRTeam getTeamByName(String input) {
        if (input.equals("red") || input.equals("r")) return CRRTeam.red;
        else if (input.equals("blue") || input.equals("b")) return CRRTeam.blue;
        else return null;
    }

    public static CRRTeam getTeam(CRRPlayer player) {
        if (red.getPlayers().contains(player)) return red;
        else if (blue.getPlayers().contains(player)) return blue;
        else return null;
    }

    public static void setPlayerTeam(CRRTeam team, CRRPlayer player) {
        // remove player from any existing teams first
        red.removePlayer(player);
        blue.removePlayer(player);

        team.addPlayer(player);

        mc.thePlayer.sendChatMessage("/tp " + player.getName() + " " + MUtil.locationToString(team.getLocations().getJSONArray("waiting"))); // teleport player to team's waiting box
    }

    public static void init() {
        red = new CRRTeam("red", "Red", EnumChatFormatting.RED);
        blue = new CRRTeam("blue", "Blue", EnumChatFormatting.BLUE);
    }
}
