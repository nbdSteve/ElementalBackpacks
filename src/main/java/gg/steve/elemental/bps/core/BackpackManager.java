package gg.steve.elemental.bps.core;

import gg.steve.elemental.bps.managers.ConfigManager;
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

    public static void init() {
        backpackItemsByItemStack = new HashMap<>();
        backpackItemsById = new HashMap<>();
        for (String entry : ConfigManager.CONFIG.get().getStringList("backpack-items")) {
            String[] parts = entry.split(":");
            if (parts.length != 3) {
                LogUtil.info("Error while loading backpack item: " + entry + ", please check your configuration.");
                continue;
            }
            UUID id = UUID.fromString(parts[2]);
            ItemStack item = new ItemStack(Material.getMaterial(parts[0].toUpperCase()), Byte.parseByte(parts[1]));
            addBackpackItem(item, id);
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
}
