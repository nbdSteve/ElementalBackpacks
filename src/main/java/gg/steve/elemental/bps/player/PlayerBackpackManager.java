package gg.steve.elemental.bps.player;

import gg.steve.elemental.bps.Backpacks;
import gg.steve.elemental.bps.utils.LogUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerBackpackManager implements Listener {
    private static Map<UUID, BackpackPlayer> playerBackpacks;

    public static void loadPlayerBackpacks() {
        playerBackpacks = new HashMap<>();
//        File folder = new File(Backpacks.get().getDataFolder() + File.separator + "data");
//        if (folder.exists()) {
//            for (File backpack : folder.listFiles()) {
//                UUID owner = UUID.fromString(backpack.getName().split(".yml")[0]);
//                LogUtil.info("Loading backpack for player: " + owner);
//                playerBackpacks.put(owner, new BackpackPlayer(owner));
//            }
//        }
    }

    public static BackpackPlayer getBackpackPlayer(UUID owner) {
        if (playerBackpacks.containsKey(owner)) return playerBackpacks.get(owner);
        File folder = new File(Backpacks.get().getDataFolder() + File.separator + "data");
        if (!folder.exists()) return null;
        for (File backpack : folder.listFiles()) {
            if (!UUID.fromString(backpack.getName().split(".yml")[0]).equals(owner)) continue;
            LogUtil.info("Loading backpack for offline / unloaded player: " + owner);
            playerBackpacks.put(owner, new BackpackPlayer(owner));
            return playerBackpacks.get(owner);
        }
        return null;
    }

    public static void saveBackpacks() {
        if (playerBackpacks == null || playerBackpacks.isEmpty()) return;
        for (BackpackPlayer backpackPlayer : playerBackpacks.values()) {
            backpackPlayer.getBackpack().saveToFile();
        }
    }

    public static boolean addBackpackPlayer(UUID owner) {
        if (playerBackpacks.containsKey(owner)) return false;
        playerBackpacks.put(owner, new BackpackPlayer(owner));
        return true;
    }

    public static boolean removeBackpackPlayer(UUID owner) {
        if (!playerBackpacks.containsKey(owner)) return false;
        playerBackpacks.get(owner).getBackpack().saveToFile();
        playerBackpacks.remove(owner);
        return true;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        addBackpackPlayer(event.getPlayer().getUniqueId());
        LogUtil.info("Loading backpack for player: " + event.getPlayer().getName());
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        removeBackpackPlayer(event.getPlayer().getUniqueId());
        LogUtil.info("Saving backpack for disconnecting player: " + event.getPlayer().getName());
    }
}