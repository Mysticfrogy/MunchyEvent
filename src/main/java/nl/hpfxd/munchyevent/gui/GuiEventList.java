package nl.hpfxd.munchyevent.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import nl.hpfxd.munchyevent.MUtil;
import org.json.JSONArray;
import org.json.JSONObject;

public class GuiEventList extends GuiScreen {
    private JSONArray events;

    public void initGui() {
        events = MUtil.sortJSONArray(MUtil.eventInfo.getJSONArray("events"), "name");
        this.buttonList.clear();

        this.buttonList.add(new GuiButton(-100, this.width / 2 - 100, this.height - ((this.height / 4) - 20), "Exit"));

        for (int i = 0; i < events.length(); i++) {
            JSONObject event = events.getJSONObject(i);

            boolean e = i % 2 == 0;

            this.buttonList.add(new GuiButton(i, this.width / 2 - (e ? 250 : -50), 48 + ((e ? i : (i - 1)) * 12), event.getString("name")));
        }
    }

    public void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == -100) {
                mc.thePlayer.closeScreen();
            } else {
                JSONObject event = events.getJSONObject(button.id);
                if (event != null) {
                    mc.displayGuiScreen(new GuiEventInfo(this, event));
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
        this.drawCenteredString(this.fontRendererObj, "Event List", this.width / 2, 17, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
