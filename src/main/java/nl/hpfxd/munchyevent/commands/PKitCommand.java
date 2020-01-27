package nl.hpfxd.munchyevent.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import nl.hpfxd.munchyevent.MUtil;
import org.json.JSONArray;
import org.json.JSONObject;

public class PKitCommand extends CommandBase {
    private Minecraft mc = Minecraft.getMinecraft();

    @Override
    public String getCommandName() {
        return "pkit";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "pkit create|<name>";
    }

    @Override
    public void processCommand(ICommandSender s, String[] args) {
        if (args.length == 1) {
            if (args[0].equals("create")) {
                InventoryPlayer inv = mc.thePlayer.inventory;
                JSONObject pkit = new JSONObject();
                JSONArray items = new JSONArray();
                JSONArray effects = new JSONArray();
                for (int i = 5; i <= 44; i++) { // armor and inventory slot range
                    ItemStack item;
                    if (i < 9) {
                        item = inv.armorInventory[4-(i-5)];
                    } else {
                        item = inv.mainInventory[MUtil.networkSlotToDataSlot(i)];
                    }
                    if (item == null) continue;

                    JSONObject json = new JSONObject();

                    json.put("type", item.getItem().getUnlocalizedName());
                    json.put("id", Item.getIdFromItem(item.getItem()));
                    json.put("amount", item.stackSize);
                    json.put("slot", i);
                    if (item.hasTagCompound()) {
                        NBTTagList enchs = item.getEnchantmentTagList();
                        if (enchs != null) {
                            JSONArray enchantments = new JSONArray();

                            for (int j = 0; j < enchs.tagCount(); j++) {
                                NBTTagCompound tag = enchs.getCompoundTagAt(j);

                                int id = tag.getShort("id");

                                Enchantment enchantment = Enchantment.getEnchantmentById(id);

                                if (enchantment != null) {
                                    JSONObject ejson = new JSONObject();

                                    ejson.put("id", id);
                                    ejson.put("level", tag.getShort("lvl"));

                                    enchantments.put(ejson);
                                }
                            }

                            json.put("enchantments", enchantments);
                        }
                    }

                    items.put(json);
                }

                pkit.put("items", items);
                pkit.put("effects", effects);

                System.out.println(pkit.toString());
                MUtil.chat("Logged.");
            }
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