package gg.steve.elemental.bps.player;

import gg.steve.elemental.bps.core.Backpack;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BackpackPlayer {
    private UUID owner;
    private Backpack backpack;

    //
//    public BackpackPlayer(OfflinePlayer player) {
//        this.owner = player.getUniqueId();
//        this.loadBackpack();
//    }
//
//    public BackpackPlayer(Player player) {
//        this.owner = player.getUniqueId();
//        this.loadBackpack();
//    }
//
    public BackpackPlayer(UUID owner) {
        this.owner = owner;
        this.loadBackpack();
    }

    public void loadBackpack() {
        this.backpack = new Backpack(owner);
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(this.owner);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.owner);
    }

    public UUID getOwner() {
        return owner;
    }

    public Backpack getBackpack() {
        return backpack;
    }
}
