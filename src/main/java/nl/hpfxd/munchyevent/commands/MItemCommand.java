package nl.hpfxd.munchyevent.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import nl.hpfxd.munchyevent.MUtil;

import java.util.Arrays;
import java.util.List;

public class MItemCommand extends CommandBase {
    private Minecraft mc = Minecraft.getMinecraft();

    @Override
    public String getCommandName() {
        return "mitem";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "mitem [enchant <enchantment> [level]] | [rename <name>]";
    }

    @Override
    public void processCommand(ICommandSender s, String[] args) throws NumberInvalidException, WrongUsageException {
        if (args.length > 0) {
            if (args[0].equals("enchant")) {
                if (args.length == 2) {
                    Enchantment enchantment = Enchantment.getEnchantmentByLocation(args[1]);
                    if (enchantment != null) {
                        if (enchantCurrentStack(enchantment, 1)) {
                            MUtil.chat("Applied &e" + enchantment.getTranslatedName(1) + "&7.");
                        } else {
                            MUtil.chat("Failed to apply enchantment.");
                        }
                    } else {
                        MUtil.chat("Unknown enchantment.");
                    }
                } else if (args.length == 3) {
                    Enchantment enchantment = Enchantment.getEnchantmentByLocation(args[1]);
                    int level = parseInt(args[2], 1, 127);

                    if (enchantment != null) {
                        if (enchantCurrentStack(enchantment, level)) {
                            MUtil.chat("Applied &e" + enchantment.getTranslatedName(level) + "&7.");
                        } else {
                            MUtil.chat("Failed to apply enchantment.");
                        }
                    } else {
                        MUtil.chat("Unknown enchantment.");
                    }
                } else {
                    throw new WrongUsageException(this.getCommandUsage(s));
                }
            } else if (args[0].equals("rename")) {
                if (args.length == 1) {
                    if (this.renameCurrentStack(null)) {
                        MUtil.chat("Successfully cleared item name.");
                    } else {
                        MUtil.chat("Failed to clear item name.");
                    }
                } else {
                    int idx = 1;
                    if (args[1].equals("rainbow")) {
                        idx = 2;
                    }
                    String name = String.join(" ", Arrays.copyOfRange(args, idx, args.length)); // join args to string except 1st/2nd arg
                    if (idx == 2) {
                        name = this.getRainbowName(name);
                    }
                    name = name.replace("&", "§"); // replace & with § to allow for color codes.

                    if (this.renameCurrentStack(name)) {
                        MUtil.chat("Successfully renamed item to &e" + name + "&7.");
                    } else {
                        MUtil.chat("Failed to rename item.");
                    }
                }
            } else {
                throw new WrongUsageException(this.getCommandUsage(s));
            }
        } else {
            throw new WrongUsageException(this.getCommandUsage(s));
        }
    }

    private boolean renameCurrentStack(String name) {
        if (mc.thePlayer.capabilities.isCreativeMode) {
            ItemStack held = mc.thePlayer.getCurrentEquippedItem();

            if (held == null) return false;

            if (name == null) {
                held.clearCustomName();
            } else {
                held.setStackDisplayName(name);
            }

            mc.playerController.sendSlotPacket(held, MUtil.dataSlotToNetworkSlot(mc.thePlayer.inventory.currentItem)); // update server

            return true;
        }

        return false;
    }

    private boolean enchantCurrentStack(Enchantment enchantment, int level) {
        if (mc.thePlayer.capabilities.isCreativeMode) {
            ItemStack held = mc.thePlayer.getCurrentEquippedItem();

            if (held == null) return false;

            held.addEnchantment(enchantment, level);

            mc.playerController.sendSlotPacket(held, MUtil.dataSlotToNetworkSlot(mc.thePlayer.inventory.currentItem)); // update server

            return true;
        }

        return false;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender s) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "enchant", "rename");
        } else if (args.length == 2) {
            if (args[0].equals("enchant")) {
                return getListOfStringsMatchingLastWord(args, Enchantment.func_181077_c());
            }
        }

        return null;
    }

    private String getRainbowName(String input) {
        char[] rainbowColors = new char[]{'a', 'b', 'c', 'd', 'e'};
        int idx = 0;

        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {
            result.append('&')
                    .append(rainbowColors[idx])
                    .append(c);
            idx++;
            if (idx == rainbowColors.length) idx = 0;
        }

        return result.toString();
    }
}