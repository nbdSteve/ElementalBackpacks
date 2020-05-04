package gg.steve.elemental.bps.listener;

import gg.steve.elemental.bps.core.Backpack;
import gg.steve.elemental.bps.core.BackpackManager;
import gg.steve.elemental.bps.managers.ConfigManager;
import gg.steve.elemental.bps.message.MessageType;
import gg.steve.elemental.bps.player.PlayerBackpackManager;
import gg.steve.elemental.bps.utils.LogUtil;
import gg.steve.elemental.pets.api.PetApi;
import gg.steve.elemental.pets.core.PetType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerBlockBreakListener implements Listener {

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
        for (ItemStack drop : event.getBlock().getDrops(event.getPlayer().getItemInHand())) {
            if (!BackpackManager.isBackpackItem(drop)) continue;
            if (!backpack.add(BackpackManager.getItemId(drop), drop.getAmount())) {
                MessageType.BACKPACK_FULL.message(event.getPlayer(), String.valueOf(backpack.getCapacity()));
                continue;
            }
            if (PetApi.isPetActive(event.getPlayer(), PetType.FORTUNE) &&
                    PetApi.isProcing(PetApi.getActivePet(event.getPlayer(), PetType.FORTUNE), PetApi.getPetRarity(event.getPlayer(), PetType.FORTUNE))) {
                for (int i = 0; i < PetApi.getBoostAmount(PetType.FORTUNE); i++) {
                    event.getPlayer().sendMessage("double drops bitch");
                    if (!backpack.add(BackpackManager.getItemId(drop), drop.getAmount())) {
                        MessageType.BACKPACK_FULL.message(event.getPlayer(), String.valueOf(backpack.getCapacity()));
                    }
                }
            }
        }
        event.getBlock().getDrops().clear();
        event.getBlock().setType(Material.AIR);
    }
}
