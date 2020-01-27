package nl.hpfxd.munchyevent.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import nl.hpfxd.munchyevent.MUtil;

import java.util.HashMap;
import java.util.List;

public class PNoteCommand extends CommandBase {
    private Minecraft mc = Minecraft.getMinecraft();
    private HashMap<String, String> pnotes = new HashMap<>(); // name, description

    @Override
    public String getCommandName() {
        return "pnote";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "pnote save\npnote clear\npnote <player> [description]";
    }

    @Override
    public void processCommand(ICommandSender s, String[] args) {
        if (args.length == 0) {
            if (pnotes.size() > 0) {
                for (String name : pnotes.keySet()) {
                    String value = pnotes.get(name);
                    MUtil.chat("&e" + name + "&7: " + (value == null ? "None" : value));
                }
            } else {
                MUtil.chat("You have no PNotes!");
            }
        } else if (args.length == 1) {
            if (args[0].equals("save")) {
                pnotes.clear();
                for (EntityPlayer player : mc.theWorld.playerEntities) { // all players in viewing distance
                    if (player != mc.thePlayer) {
                        pnotes.put(player.getName(), null);
                    }
                }
                MUtil.chat("Saved &e" + (mc.theWorld.playerEntities.size() - 1)+ " &7players.");
            } else if (args[0].equals("clear")) {
                MUtil.chat("Cleared");
                pnotes.clear();
            } else {
                if (pnotes.containsKey(args[0])) {
                    String value = pnotes.get(args[0]);
                    MUtil.chat(value == null ? "None" : value);
                } else {
                    MUtil.chat("Saved.");
                    pnotes.put(args[0], null);
                }
            }
        } else if (args.length == 2) {
            pnotes.put(args[0], args[1]);
            MUtil.chat("Saved.");
        } else {
            MUtil.chat("&cInvalid usage.");
            MUtil.chat("/" + this.getCommandUsage(s));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender s) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, MUtil.getAllPlayerNames()) : null;
    }
}
