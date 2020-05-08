package gg.steve.elemental.bps.api;

import gg.steve.elemental.bps.Backpacks;
import gg.steve.elemental.bps.core.BackpackManager;
import gg.steve.elemental.bps.event.SellMethodType;
import gg.steve.elemental.bps.player.BackpackPlayer;
import gg.steve.elemental.bps.player.PlayerBackpackManager;
import org.bukkit.entity.Player;

public class BackpacksApi {

    public static Backpacks getInstance() {
        return Backpacks.get();
    }

    public static void openBackpack(Player player) {
        PlayerBackpackManager.getBackpackPlayer(player.getUniqueId()).getBackpack().openGui(player);
    }

    public static void sellBackpack(Player player, String group, SellMethodType sellMethod) {
        BackpackPlayer bPlayer = PlayerBackpackManager.getBackpackPlayer(player.getUniqueId());
        BackpackManager.sellBackpack(bPlayer.getBackpack(), group, sellMethod);
    }

    public static void sellBackpack(Player player, String group, int amount, SellMethodType sellMethod) {
        BackpackPlayer bPlayer = PlayerBackpackManager.getBackpackPlayer(player.getUniqueId());
        BackpackManager.sellBackpack(bPlayer.getBackpack(), group, amount, sellMethod);
    }
}
