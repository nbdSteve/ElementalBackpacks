package gg.steve.elemental.bps;

import gg.steve.elemental.bps.core.BackpackManager;
import gg.steve.elemental.bps.managers.FileManager;
import gg.steve.elemental.bps.managers.SetupManager;
import gg.steve.elemental.bps.player.PlayerBackpackManager;
import gg.steve.elemental.bps.utils.LogUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;

public final class Backpacks extends JavaPlugin {
    private static Backpacks instance;
    private static Economy economy;
    private static DecimalFormat numberFormat = new DecimalFormat("#,###.##");

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        SetupManager.setupFiles(new FileManager(instance));
        SetupManager.registerCommands(instance);
        SetupManager.registerEvents(instance);
        BackpackManager.init();
        PlayerBackpackManager.loadPlayerBackpacks();
        // verify that the server is running vault so that eco features can be used
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
        } else {
            LogUtil.info("Unable to find economy instance, disabling economy features.");
            economy = null;
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PlayerBackpackManager.saveBackpacks();
    }

    public static Backpacks get() {
        return instance;
    }

    public static Economy eco() {
        return economy;
    }

    public static DecimalFormat getNumberFormat() {
        return numberFormat;
    }
}
