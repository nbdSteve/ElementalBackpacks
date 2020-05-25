package gg.steve.elemental.bps.listener;

import gg.steve.elemental.bps.Backpacks;
import gg.steve.elemental.bps.core.Backpack;
import gg.steve.elemental.bps.core.BackpackManager;
import gg.steve.elemental.bps.managers.ConfigManager;
import gg.steve.elemental.bps.message.MessageType;
import gg.steve.elemental.bps.player.PlayerBackpackManager;
import gg.steve.elemental.bps.utils.LogUtil;
import gg.steve.elemental.pets.api.PetApi;
import gg.steve.elemental.pets.core.PetType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerBlockBreakListener implements Listener {
    private static List<UUID> messaged = new ArrayList<>();

    @EventHandler
    public void playerBreak(BlockBreakEvent event) {
        if (event.isCancelled()
                || !ConfigManager.CONFIG.get().getBoolean("enable-backpacks")) return;
        if (!BackpackManager.isBackpackBlock(event.getBlock())) return;
        Backpack backpack = PlayerBackpackManager.getBackpackPlayer(event.getPlayer().getUniqueId()).getBackpack();
        if (backpack == null) {
            LogUtil.info("Critical error, please contact nbdSteve#0583 on discord");
            return;
        }
        int fortune = 1;
        if (event.getPlayer().getItemInHand().getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
            fortune = event.getPlayer().getItemInHand().getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS) + 1;
        }

        if (isSilktouch(event)) {
            ItemStack drop = new ItemStack(event.getBlock().getType());
            for (int i = 0; i < (fortune / 10) + 1; i++) {
                if (!BackpackManager.isBackpackItem(drop)) continue;
                if (!backpack.add(BackpackManager.getItemId(drop), 1)) {
                    if (!messaged.contains(event.getPlayer().getUniqueId())) {
                        messaged.add(event.getPlayer().getUniqueId());
                        MessageType.BACKPACK_FULL.message(event.getPlayer(), Backpacks.getNumberFormat().format(backpack.getCapacity()));
                    }
                    continue;
                }
                if (PetApi.isPetActive(event.getPlayer(), PetType.FORTUNE) &&
                        PetApi.isProcing(PetApi.getActivePet(event.getPlayer(), PetType.FORTUNE), PetApi.getPetRarity(event.getPlayer(), PetType.FORTUNE))) {
                    for (int y = 0; y < PetApi.getBoostAmount(PetType.FORTUNE); y++) {
                        if (!backpack.add(BackpackManager.getItemId(drop), 1)) {
                            MessageType.BACKPACK_FULL.message(event.getPlayer(), Backpacks.getNumberFormat().format(backpack.getCapacity()));
                        }
                    }
                }
            }
        } else {
            for (ItemStack drop : event.getBlock().getDrops(event.getPlayer().getItemInHand())) {
                for (int i = 0; i < (fortune / 10) + 1; i++) {
                    if (!BackpackManager.isBackpackItem(drop)) continue;
                    if (!backpack.add(BackpackManager.getItemId(drop), drop.getAmount())) {
                        if (!messaged.contains(event.getPlayer().getUniqueId())) {
                            messaged.add(event.getPlayer().getUniqueId());
                            MessageType.BACKPACK_FULL.message(event.getPlayer(), Backpacks.getNumberFormat().format(backpack.getCapacity()));
                        }
                        continue;
                    }
                    if (PetApi.isPetActive(event.getPlayer(), PetType.FORTUNE) &&
                            PetApi.isProcing(PetApi.getActivePet(event.getPlayer(), PetType.FORTUNE), PetApi.getPetRarity(event.getPlayer(), PetType.FORTUNE))) {
                        for (int y = 0; y < PetApi.getBoostAmount(PetType.FORTUNE); y++) {
                            if (!backpack.add(BackpackManager.getItemId(drop), drop.getAmount())) {
                                MessageType.BACKPACK_FULL.message(event.getPlayer(), Backpacks.getNumberFormat().format(backpack.getCapacity()));
                            }
                        }
                    }
                }
            }
        }
        messaged.remove(event.getPlayer().getUniqueId());
        event.getBlock().getDrops().clear();
        event.getBlock().setType(Material.AIR);
    }

    public boolean isSilktouch(BlockBreakEvent event) {
        ItemStack item = event.getPlayer().getItemInHand();
        if (item.getType().equals(Material.AIR)) return false;
        return item.getEnchantments().containsKey(Enchantment.SILK_TOUCH);
    }
}
