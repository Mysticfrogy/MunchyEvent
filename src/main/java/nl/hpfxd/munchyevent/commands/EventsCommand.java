package nl.hpfxd.munchyevent.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import nl.hpfxd.munchyevent.MUtil;
import nl.hpfxd.munchyevent.gui.GuiEventList;
import nl.hpfxd.munchyevent.gui.GuiReward;

public class EventsCommand extends CommandBase {
    private Minecraft mc = Minecraft.getMinecraft();

    @Override
    public String getCommandName() {
        return "events";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "events";
    }

    @Override
    public void processCommand(ICommandSender s, String[] args) {
        if (args.length == 0) {
            MUtil.openGui(new GuiEventList());
        } else if (args.length == 1 && args[0].equals("reload")) {
            if (MUtil.reloadEvents()) {
                MUtil.chat("Reloaded events.");
            } else {
                MUtil.chat("Could not load events.");
            }
        } else if (args.length == 1 && args[0].equals("reward")) {
            MUtil.openGui(new GuiReward());
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