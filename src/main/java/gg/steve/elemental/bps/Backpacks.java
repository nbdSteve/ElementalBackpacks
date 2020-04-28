package gg.steve.elemental.bps;

import gg.steve.elemental.bps.core.BackpackManager;
import gg.steve.elemental.bps.managers.FileManager;
import gg.steve.elemental.bps.managers.SetupManager;
import gg.steve.elemental.bps.player.PlayerBackpackManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Backpacks extends JavaPlugin {
    private static Backpacks instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        SetupManager.setupFiles(new FileManager(instance));
        SetupManager.registerCommands(instance);
        SetupManager.registerEvents(instance);
        BackpackManager.init();
        PlayerBackpackManager.loadPlayerBackpacks();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PlayerBackpackManager.saveBackpacks();
    }

    public static Backpacks get() {
        return instance;
    }
}
