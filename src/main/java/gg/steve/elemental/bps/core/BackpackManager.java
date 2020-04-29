package gg.steve.elemental.bps.core;

import gg.steve.elemental.bps.Backpacks;
import gg.steve.elemental.bps.managers.ConfigManager;
import gg.steve.elemental.bps.message.MessageType;
import gg.steve.elemental.bps.player.BackpackPlayer;
import gg.steve.elemental.bps.utils.LogUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BackpackManager {
    private static Map<ItemStack, UUID> backpackItemsByItemStack;
    private static Map<UUID, ItemStack> backpackItemsById;
    private static Map<UUID, Double> basePrices;
    private static Map<String, Map<UUID, Double>> groupPrices;

    public static void init() {
        backpackItemsByItemStack = new HashMap<>();
        backpackItemsById = new HashMap<>();
        basePrices = new HashMap<>();
        groupPrices = new HashMap<>();
        for (String entry : ConfigManager.CONFIG.get().getStringList("backpack-items")) {
            String[] parts = entry.split(":");
            if (parts.length != 4) {
                LogUtil.info("Error while loading backpack item: " + entry + ", please check your configuration.");
                continue;
            }
            UUID id = UUID.fromString(parts[2]);
            ItemStack item = new ItemStack(Material.getMaterial(parts[0].toUpperCase()), Byte.parseByte(parts[1]));
            addBackpackItem(item, id);
            basePrices.put(id, Double.parseDouble(parts[3]));
        }
        LogUtil.info("Successfully loaded all backpack items and base prices.");
        for (String group : ConfigManager.CONFIG.get().getConfigurationSection("sell-groups").getKeys(false)) {
            groupPrices.put(group, new HashMap<>());
            for (String item : ConfigManager.CONFIG.get().getConfigurationSection("sell-groups." + group).getKeys(false)) {
                groupPrices.get(group).put(UUID.fromString(item), ConfigManager.CONFIG.get().getDouble("sell-groups." + group + "." + item + ".price"));
            }
            LogUtil.info("Successfully loaded sell group: " + group);
        }
    }

    public static boolean isBackpackItem(ItemStack item) {
        return backpackItemsByItemStack.containsKey(getKeyItem(item));
    }

    public static boolean isBackpackBlock(Block block) {
        ItemStack key = new ItemStack(block.getType(), block.getData());
        return backpackItemsByItemStack.containsKey(key);
    }

    public static void addBackpackItem(ItemStack item, UUID id) {
        backpackItemsByItemStack.put(item, id);
        backpackItemsById.put(id, item);
    }

    public static void removeBackpackItem(ItemStack item, UUID id) {
        backpackItemsByItemStack.remove(item, id);
        backpackItemsById.remove(id, item);
    }

    public static UUID getItemId(ItemStack item) {
        ItemStack key = getKeyItem(item);
        if (!backpackItemsByItemStack.containsKey(key)) return null;
        return backpackItemsByItemStack.get(key);
    }

    public static ItemStack getItem(UUID id) {
        if (!backpackItemsById.containsKey(id)) return null;
        return backpackItemsById.get(id);
    }

    public static ItemStack getKeyItem(ItemStack item) {
        return new ItemStack(item.getType(), item.getDurability());
    }

    public static boolean isSellGroup(String group) {
        if (group.equalsIgnoreCase("all")) return true;
        return groupPrices.containsKey(group);
    }

    public static void sellGroup(BackpackPlayer player, String group) {
        int amountSold = 0;
        double totalDeposit = 0;
        Backpack backpack = player.getBackpack();
        if (group.equalsIgnoreCase("all")) {
            for (UUID id : backpack.getContents().keySet()) {
                amountSold += backpack.getAmount(id);
                totalDeposit += sellItem(id, player, basePrices.get(id));
            }
        } else {
            for (UUID id : groupPrices.get(group).keySet()) {
                amountSold += backpack.getAmount(id);
                totalDeposit += sellItem(id, player, basePrices.get(id));
            }
        }
        MessageType.SELL_ITEMS.message(player.getPlayer().getPlayer(), Backpacks.getNumberFormat().format(amountSold), Backpacks.getNumberFormat().format(totalDeposit));
    }

    public static double sellItem(UUID id, BackpackPlayer player, double price) {
        Backpack backpack = player.getBackpack();
        if (!backpack.hasItem(id)) return 0;
        double deposit = backpack.getAmount(id) * price;
        backpack.remove(id, backpack.getAmount(id));
        if (Backpacks.eco() != null) {
            Backpacks.eco().depositPlayer(player.getPlayer(), deposit);
        } else {
            LogUtil.warning("No amount was deposited because there isn't an economy.");
        }
        return deposit;
    }
}
