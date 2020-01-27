package nl.hpfxd.munchyevent.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import nl.hpfxd.munchyevent.MUtil;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiHostEvent extends GuiScreen {
    private GuiTextField eventNameField;
    private GuiTextField eventPrizeField;
    private GuiTextField eventKitField;

    private int tabIndex = 0;

    public void updateScreen() {
        this.eventNameField.updateCursorCounter();
        this.eventPrizeField.updateCursorCounter();
        this.eventKitField.updateCursorCounter();
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.tabIndex = 0;
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 18, "Host"));

        this.eventNameField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 66, 200, 20);
        this.eventNameField.setFocused(true);

        this.eventPrizeField = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 100, 106, 200, 20);

        this.eventKitField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, 146, 200, 20);
        this.eventKitField.setMaxStringLength(16);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == 0) {
                MUtil.hostManualEvent(eventNameField.getText(), eventPrizeField.getText(), eventKitField.getText());
                mc.thePlayer.closeScreen();
            }
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        this.eventNameField.textboxKeyTyped(typedChar, keyCode);
        this.eventPrizeField.textboxKeyTyped(typedChar, keyCode);
        this.eventKitField.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == 15) { // tab
            // i'm aware there is a much better way to do this
            if (this.tabIndex == 0) { // prize
                this.eventNameField.setFocused(false);
                this.eventPrizeField.setFocused(true);
                this.eventKitField.setFocused(false);
                this.tabIndex++;
            } else if (this.tabIndex == 1) { // kit
                this.eventNameField.setFocused(false);
                this.eventPrizeField.setFocused(false);
                this.eventKitField.setFocused(true);
                this.tabIndex++;
            } else if (this.tabIndex == 2) { // kit
                this.eventNameField.setFocused(true);
                this.eventPrizeField.setFocused(false);
                this.eventKitField.setFocused(false);
                this.tabIndex = 0;
            }
        } else if (keyCode == 28 || keyCode == 156) { // enter or numpad enter
            this.actionPerformed(this.buttonList.get(0));
        } else if (keyCode == 1) { // esc
            mc.thePlayer.closeScreen();
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.eventNameField.mouseClicked(mouseX, mouseY, mouseButton);
        this.eventPrizeField.mouseClicked(mouseX, mouseY, mouseButton);
        this.eventKitField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Host Event", this.width / 2, 17, 16777215);
        this.drawString(this.fontRendererObj, "Event Name", this.width / 2 - 100, 53, 10526880);
        this.drawString(this.fontRendererObj, "Event Prize", this.width / 2 - 100, 94, 10526880);
        this.drawString(this.fontRendererObj, "Event Kit", this.width / 2 - 100, 135, 10526880);

        this.eventNameField.drawTextBox();
        this.eventPrizeField.drawTextBox();
        this.eventKitField.drawTextBox();

        String bstr = MUtil.getBroadcastString(this.eventNameField.getText(), this.eventPrizeField.getText()).replace("&", "§");
        this.drawCenteredString(
                this.fontRendererObj,
                bstr + EnumChatFormatting.WHITE + " (" + (bstr.length() > 100 ? EnumChatFormatting.RED : EnumChatFormatting.GREEN) + bstr.length()  + EnumChatFormatting.WHITE + "/100)",
                this.width / 2,
                33,
                10526880
        );
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
