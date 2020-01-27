package nl.hpfxd.munchyevent.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import nl.hpfxd.munchyevent.MUtil;
import nl.hpfxd.munchyevent.gui.GuiHostEvent;

public class MHostCommand extends CommandBase {
    private Minecraft mc = Minecraft.getMinecraft();

    @Override
    public String getCommandName() {
        return "mhost";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "mhost [event name] [event prize]";
    }

    @Override
    public void processCommand(ICommandSender s, String[] args) {
        if (args.length == 0) {
            MUtil.openGui(new GuiHostEvent());
        } else if (args.length == 3) {
            MUtil.hostManualEvent(args[0].replace("+", " "), args[1].replace("+", " "), args[2]);
        } else {
            MUtil.chat("&cInvalid usage.");
            MUtil.chat("/" + this.getCommandUsage(s));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender s) {
        return true;
    }
}
