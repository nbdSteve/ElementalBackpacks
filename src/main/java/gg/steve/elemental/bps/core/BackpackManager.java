package gg.steve.elemental.bps.core;

import gg.steve.elemental.bps.event.PreBackpackSaleEvent;
import gg.steve.elemental.bps.event.SellMethodType;
import gg.steve.elemental.bps.managers.ConfigManager;
import gg.steve.elemental.bps.utils.LogUtil;
import gg.steve.elemental.pets.api.PetApi;
import gg.steve.elemental.pets.core.PetType;
import org.bukkit.Bukkit;
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
    private static Map<String, Double> groupMultipliers;

    public static void init() {
        backpackItemsByItemStack = new HashMap<>();
        backpackItemsById = new HashMap<>();
        basePrices = new HashMap<>();
        groupMultipliers = new HashMap<>();
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
            groupMultipliers.put(group, ConfigManager.CONFIG.get().getDouble("sell-groups." + group));
            LogUtil.info("Successfully loaded sell group: " + group);
        }
//        for (String group : ConfigManager.CONFIG.get().getConfigurationSection("sell-groups").getKeys(false)) {
//            groupPrices.put(group, new HashMap<>());
//            for (String item : ConfigManager.CONFIG.get().getConfigurationSection("sell-groups." + group).getKeys(false)) {
//                groupPrices.get(group).put(UUID.fromString(item), ConfigManager.CONFIG.get().getDouble("sell-groups." + group + "." + item + ".price"));
//            }
//            LogUtil.info("Successfully loaded sell group: " + group);
//        }
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
        return groupMultipliers.containsKey(group);
    }

    public static double getItemPrice(String group, UUID id) {
        if (group.equalsIgnoreCase("all")) return basePrices.get(id);
        return basePrices.get(id) * groupMultipliers.get(group);
//        if (!groupPrices.get(group).containsKey(id)) return -1;
//        return groupPrices.get(group).get(id);
    }

    public static void sellBackpack(Backpack backpack, String group, SellMethodType sellMethod) {
        Bukkit.getPluginManager().callEvent(new PreBackpackSaleEvent(backpack, group, PetApi.getActivePet(backpack.getPlayer().getPlayer(), PetType.MONEY), -1, sellMethod));
    }

    public static void sellBackpack(Backpack backpack, String group, int amount, SellMethodType sellMethod) {
        Bukkit.getPluginManager().callEvent(new PreBackpackSaleEvent(backpack, group, PetApi.getActivePet(backpack.getPlayer().getPlayer(), PetType.MONEY), amount, sellMethod));
    }
}
