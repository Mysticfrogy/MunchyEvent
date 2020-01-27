package nl.hpfxd.munchyevent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Session;
import net.minecraft.world.World;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MUtil {
    private static Minecraft mc = Minecraft.getMinecraft();
    private static HttpClient httpClient = HttpClientBuilder.create().build();
    public static JSONObject eventInfo;

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    static {
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static void chat(String msg) {
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(("&e[MunchyEvent] &7" + msg).replace("&", "§")));
    }

    static void sendAnalytics() throws Exception {
        Session session = mc.getSession();

        HttpPost request = new HttpPost("https://analytics.hpfxd.nl/submit");

        JSONObject data = new JSONObject();

        data.put("project", MunchyEvent.MODID);
        data.put("version", MunchyEvent.VERSION);

        JSONObject d = new JSONObject();

        if (session != null) {
            d.put("uuid", session.getPlayerID());
            d.put("username", session.getUsername());
        }

        data.put("data", d);

        StringEntity params = new StringEntity(data.toString());
        request.addHeader("Content-Type", "application/json");
        request.setEntity(params);
        HttpResponse response = httpClient.execute(request);
    }

    public static String getBroadcastString(String eventName, String eventPrize) {
        return "&eHosting &c&l" + eventName + " &efor &6" + eventPrize + "&e! &3/confirm &eto join!";
    }

    public static boolean reloadEvents() {
        try (InputStream inputStream = new URL("https://cdn.hpfxd.nl/misc/eventinfo.json").openStream()) {
            eventInfo = new JSONObject(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void hostManualEvent(String name, String prize, String skit) {
        new Thread(() -> {
            try {
                for (int i = 0; i < 4; i++) {
                    mc.thePlayer.sendChatMessage("/broadcast " + getBroadcastString(name, prize));
                    Thread.sleep(50);
                    mc.thePlayer.sendChatMessage("/eventtpall");
                    if (i == 3) { // 4th and final tp
                        Thread.sleep(100);
                        mc.thePlayer.sendChatMessage("&e^ &c&lLAST TP &e^");
                    }

                    MUtil.chat("TP " + (i + 1) + "/4");

                    for (int i1 = 0; i1 < 10; i1++) {
                        Thread.sleep(1500);
                        mc.thePlayer.sendChatMessage("/skit apply " + skit + " 50");
                    }
                }
                MUtil.chat("Done");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static String getRewardMessage(String name, String event, String prize, String giftedBy) {
        name = name.replace("_", "\\_"); // replace _ with \_ to prevent markdown formatting in discord

        StringBuilder sb = new StringBuilder();

        // NAME
        sb.append(name);

        sb.append(" - ");  // PRIZE

        sb.append(prize);

        sb.append(" - "); // EVENT

        sb.append(event);

        sb.append(" - "); // TIME
        sb.append(df.format(new Date())); // current time
        sb.append(" UTC");

        if (giftedBy != null && !giftedBy.equals("")) {
            giftedBy = giftedBy.replace("_", "\\_");

            sb.append(" - "); // GIFTED BY

            sb.append("won by ");
            sb.append(giftedBy);
        }


        return sb.toString();
    }

    public static NetworkPlayerInfo getNetworkPlayer(String name) {
        for (NetworkPlayerInfo np : mc.getNetHandler().getPlayerInfoMap()) {
            if (name.equalsIgnoreCase(np.getGameProfile().getName())) {
                return np;
            }
        }

        return null;
    }

    public static List<String> getAllPlayerNames() {
        List<String> result = new ArrayList<>();
        for (NetworkPlayerInfo np : mc.getNetHandler().getPlayerInfoMap()) {
            result.add(np.getGameProfile().getName());
        }

        return result;
    }

    public static void applyPKit(JSONObject kit) {
        if (!mc.thePlayer.capabilities.isCreativeMode) {
            mc.thePlayer.sendChatMessage("/gamemode creative");
        }
        for (int i = 0; i < mc.thePlayer.inventoryContainer.getInventory().size(); ++i) { // clear inventory
            mc.playerController.sendSlotPacket(null, i);
        }

        JSONArray items = kit.getJSONArray("items");

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);

            ItemStack stack = new ItemStack(Item.getItemById(item.getInt("id")), item.getInt("amount"));

            if (item.has("enchantments")) {
                JSONArray enchantments = item.getJSONArray("enchantments");

                for (int j = 0; j < enchantments.length(); j++) {
                    JSONObject enchantment = enchantments.getJSONObject(j);

                    stack.addEnchantment(Enchantment.getEnchantmentById(enchantment.getInt("id")), enchantment.getInt("level"));
                }
            }

            mc.playerController.sendSlotPacket(stack, item.getInt("slot"));
        }

        JSONArray effects = kit.getJSONArray("effects");

        for (int i = 0; i < effects.length(); i++) {
            JSONObject effect = effects.getJSONObject(i);

            mc.thePlayer.sendChatMessage("/eeffect " + mc.thePlayer.getName() + " " + effect.getString("name") + " " + effect.getInt("length") + " " + effect.getInt("amplifier"));
        }
    }

    public static JSONArray sortJSONArray(JSONArray jsonArr, String key) {
        JSONArray result = new JSONArray();

        List<JSONObject> jsonValues = new ArrayList<>();
        for (int i = 0; i < jsonArr.length(); i++) {
            jsonValues.add(jsonArr.getJSONObject(i));
        }
        jsonValues.sort(Comparator.comparing(a -> ((String) a.get(key))));

        for (int i = 0; i < jsonArr.length(); i++) {
            result.put(jsonValues.get(i));
        }

        return result;
    }

    public static JSONObject getEvent(String id) {
        JSONArray events = eventInfo.getJSONArray("events");

        for (int i = 0; i < events.length(); i++) {
            JSONObject event = events.getJSONObject(i);

            if (event.getString("id").equals(id)) {
                return event;
            }
        }

        return null;
    }

    public static JSONObject getMap(JSONObject event, String id) {
        JSONArray maps = event.getJSONArray("maps");

        for (int i = 0; i < maps.length(); i++) {
            JSONObject map = maps.getJSONObject(i);
            if (map.has("id")) {
                if (map.getString("id").equals(id)) {
                    return map;
                }
            }
        }

        return null;
    }

    public static String locationToString(JSONArray loc) {
        return loc.getDouble(0) + " " + loc.getDouble(1) + " " + loc.getDouble(2);
    }

    // https://gist.github.com/ddevault/459a1691c3dd751db160
    public static int dataSlotToNetworkSlot(int index) {
        if (index <= 8) index += 36;
        else if (index == 100) index = 8;
        else if (index == 101) index = 7;
        else if (index == 102) index = 6;
        else if (index == 103) index = 5;
        else if (index >= 80 && index <= 83) index -= 79;
        return index;
    }

    public static int networkSlotToDataSlot(int index) {
        if (index >= 36 && index <= 44) index -= 36;
        else if (index == 8) index = 100;
        else if (index == 7) index = 101;
        else if (index == 6) index = 102;
        else if (index == 5) index = 103;
        else if (index >= 1 && index <= 4) index += 79;
        return index;
    }

    public static void openGui(GuiScreen gui) {
        new Thread(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mc.addScheduledTask(() -> mc.displayGuiScreen(gui)); // add scheduled task to prevent threading issues
        }).start();
    }

    public static EntityPlayer getEntityPlayerByName(String name) {
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }

        return null;
    }
}
