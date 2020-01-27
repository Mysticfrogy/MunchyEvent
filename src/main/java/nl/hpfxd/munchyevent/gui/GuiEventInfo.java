package nl.hpfxd.munchyevent.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import nl.hpfxd.munchyevent.MUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GuiEventInfo extends GuiScreen {
    private final GuiScreen parent;
    private final JSONObject event;

    public GuiEventInfo(GuiScreen parent, JSONObject event) {
        this.parent = parent;
        this.event = event;
    }

    public void initGui() {
        this.buttonList.clear();

        this.buttonList.add(new GuiButton(-100, this.width / 2 - 100, this.height - ((this.height / 4) - 20), "Back"));

        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, 120, "Maps"));

        if (event.has("kit")) {
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 150, "Get kit"));
        }
    }

    public void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == -100) {
                mc.displayGuiScreen(this.parent);
            } else if (button.id == 0) {
                mc.displayGuiScreen(new GuiEventMaps(this, event));
            } else if (button.id == 1) {
                MUtil.applyPKit(event.getJSONObject("kit"));
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
        int i = 0;
        for (String line : this.splitLine(event.getString("description"))) {
            this.drawCenteredString(this.fontRendererObj, line, this.width / 2, 27 + (12 * i), 16777215);
            i++;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private List<String> splitLine(String str) {
        List<String> result = new ArrayList<>();
        boolean w = false;
        String cstr = "";
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i % 100 == 0 && i != 0) {
                w = true;
            }

            if (w && c == ' ') {
                w = false;
                result.add(cstr);
                cstr = "";
            } else {
                cstr += c;
            }
        }

        result.add(cstr);
        return result;
    }
}
