package nl.hpfxd.munchyevent.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import nl.hpfxd.munchyevent.MUtil;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiReward extends GuiScreen {
    private GuiTextField nameField;
    private GuiTextField eventNameField;
    private GuiTextField prizeField;
    private GuiTextField giftedByField;

    private int tabIndex = 0;

    public void updateScreen() {
        this.nameField.updateCursorCounter();
        this.eventNameField.updateCursorCounter();
        this.prizeField.updateCursorCounter();
        this.giftedByField.updateCursorCounter();
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.tabIndex = 0;
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 18, "Copy"));

        this.nameField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 66, 200, 20);
        this.nameField.setFocused(true);
        this.nameField.setMaxStringLength(16);

        this.eventNameField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 106, 200, 20);

        this.prizeField = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 100, 146, 200, 20);

        this.giftedByField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, 186, 200, 20);
        this.giftedByField.setMaxStringLength(16);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == 0) {
                GuiScreen.setClipboardString(MUtil.getRewardMessage(nameField.getText(), eventNameField.getText(), prizeField.getText(), giftedByField.getText()));
                mc.thePlayer.closeScreen();
            }
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        this.nameField.textboxKeyTyped(typedChar, keyCode);
        this.eventNameField.textboxKeyTyped(typedChar, keyCode);
        this.prizeField.textboxKeyTyped(typedChar, keyCode);
        this.giftedByField.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == 15) { // tab
            // i'm aware there is a much better way to do this
            if (this.tabIndex == 0) { // event name
                this.nameField.setFocused(false);
                this.eventNameField.setFocused(true);
                this.prizeField.setFocused(false);
                this.giftedByField.setFocused(false);
                this.tabIndex++;
            } else if (this.tabIndex == 1) { // prize
                this.nameField.setFocused(false);
                this.eventNameField.setFocused(false);
                this.prizeField.setFocused(true);
                this.giftedByField.setFocused(false);
                this.tabIndex++;
            } else if (this.tabIndex == 2) { // gifted by
                this.nameField.setFocused(false);
                this.eventNameField.setFocused(false);
                this.prizeField.setFocused(false);
                this.giftedByField.setFocused(true);
                this.tabIndex++;
            } else if (this.tabIndex == 3) { // name
                this.nameField.setFocused(true);
                this.eventNameField.setFocused(false);
                this.prizeField.setFocused(false);
                this.giftedByField.setFocused(false);
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
        this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
        this.eventNameField.mouseClicked(mouseX, mouseY, mouseButton);
        this.prizeField.mouseClicked(mouseX, mouseY, mouseButton);
        this.giftedByField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Reward player", this.width / 2, 17, 16777215);
        this.drawString(this.fontRendererObj, "Player name", this.width / 2 - 100, 53, 10526880);
        this.drawString(this.fontRendererObj, "Event Name", this.width / 2 - 100, 94, 10526880);
        this.drawString(this.fontRendererObj, "Event Prize", this.width / 2 - 100, 135, 10526880);
        this.drawString(this.fontRendererObj, "Gifted by", this.width / 2 - 100, 176, 10526880);

        this.nameField.drawTextBox();
        this.eventNameField.drawTextBox();
        this.prizeField.drawTextBox();
        this.giftedByField.drawTextBox();

        this.drawCenteredString(this.fontRendererObj, MUtil.getRewardMessage(nameField.getText(), eventNameField.getText(), prizeField.getText(), giftedByField.getText()), this.width / 2, 33, 10526880);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
