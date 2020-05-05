package gg.steve.elemental.bps.api;

import gg.steve.elemental.bps.Backpacks;
import gg.steve.elemental.bps.player.PlayerBackpackManager;
import org.bukkit.entity.Player;

public class BackpacksApi {

    public static Backpacks getInstance() {
        return Backpacks.get();
    }

    public static void openBackpack(Player player) {
        PlayerBackpackManager.getBackpackPlayer(player.getUniqueId()).getBackpack().openGui(player);
    }
}
