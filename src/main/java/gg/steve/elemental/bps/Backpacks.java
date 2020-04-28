package gg.steve.elemental.bps;

import gg.steve.elemental.bps.managers.FileManager;
import gg.steve.elemental.bps.managers.SetupManager;
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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Backpacks get() {
        return instance;
    }
}
