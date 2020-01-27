package nl.hpfxd.munchyevent.gui;

import com.google.common.base.Splitter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import nl.hpfxd.munchyevent.MUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GuiEventMaps extends GuiScreen {
    private final GuiScreen parent;
    private final JSONObject event;
    private final JSONArray maps;

    public GuiEventMaps(GuiScreen parent, JSONObject event) {
        this.parent = parent;
        this.event = event;
        this.maps = MUtil.sortJSONArray(event.getJSONArray("maps"), "name");
    }

    public void initGui() {
        this.buttonList.clear();

        this.buttonList.add(new GuiButton(-100, this.width / 2 - 100, this.height - ((this.height / 4) - 20), "Back"));

        for (int i = 0; i < maps.length(); i++) {
            JSONObject map = maps.getJSONObject(i);

            boolean e = i % 2 == 0;

            this.buttonList.add(new GuiButton(i, this.width / 2 - (e ? 250 : -50), 24 + ((e ? i : (i - 1)) * 12), map.getString("name")));
        }
    }

    public void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == -100) {
                mc.displayGuiScreen(this.parent);
            } else {
                mc.thePlayer.closeScreen();

                JSONObject map = maps.getJSONObject(button.id);

                if (map != null) {
                    JSONArray location = map.getJSONArray("location");

                    mc.thePlayer.sendChatMessage("/tp " + location.getDouble(0) + " " + location.getDouble(1) + " " + location.getDouble(2));
                    MUtil.chat("Teleporting you to &e" + event.getString("name") + "&7: &e" + map.getString("name"));
                    if (map.has("authors")) {
                        List<String> authors = new ArrayList<>();
                        for (Object author : map.getJSONArray("authors")) {
                            authors.add(author.toString());
                        }
                        MUtil.chat("This map was made by &e" + String.join("&7, &e", authors) + "&7.");
                    }
                }
            }
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) { // esc
            mc.thePlayer.closeScreen();
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD + event.getString("name"), this.width / 2, 17, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
