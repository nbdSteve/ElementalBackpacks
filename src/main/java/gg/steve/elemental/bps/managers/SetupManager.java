package gg.steve.elemental.bps.managers;

import gg.steve.elemental.bps.Backpacks;
import gg.steve.elemental.bps.cmd.PackCmd;
import gg.steve.elemental.bps.gui.GuiClickListener;
import gg.steve.elemental.bps.listener.PlayerBlockBreakListener;
import gg.steve.elemental.bps.player.PlayerBackpackManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * Class that handles setting up the plugin on start
 */
public class SetupManager {

    private SetupManager() throws IllegalAccessException {
        throw new IllegalAccessException("Manager class cannot be instantiated.");
    }

    /**
     * Loads the files into the file manager
     *
     * @param fileManager FileManager, the plugins file manager
     */
    public static void setupFiles(FileManager fileManager) {
        // general files
        for (ConfigManager file : ConfigManager.values()) {
            file.load(fileManager);
        }
    }

    public static void registerCommands(Backpacks instance) {
        instance.getCommand("pack").setExecutor(new PackCmd());
    }

    /**
     * Register all of the events for the plugin
     *
     * @param instance Plugin, the main plugin instance
     */
    public static void registerEvents(Plugin instance) {
        PluginManager pm = instance.getServer().getPluginManager();
        pm.registerEvents(new PlayerBackpackManager(), instance);
        pm.registerEvents(new GuiClickListener(), instance);
        pm.registerEvents(new PlayerBlockBreakListener(), instance);
    }
}
