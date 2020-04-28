package gg.steve.elemental.bps.player;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BackpackPlayer {
    private UUID owner;

    public BackpackPlayer(OfflinePlayer player) {
        this.owner = player.getUniqueId();
    }

    public BackpackPlayer(Player player) {
        this.owner = player.getUniqueId();
    }
}
