package gg.steve.elemental.bps.core;

import gg.steve.elemental.bps.Backpacks;
import gg.steve.elemental.bps.managers.ConfigManager;
import gg.steve.elemental.bps.utils.LogUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class BackpackDataFileUtil {
    //Store the file name string
    private String owner;
    //Store the backpack id
    private String backpackId;
    //Store the player file
    private File file;
    //Store the yaml config
    private YamlConfiguration config;

    public BackpackDataFileUtil(UUID owner, UUID backpackId) {
        //Set instance variable
        this.owner = String.valueOf(owner);
        this.backpackId = String.valueOf(backpackId);
        //Get the player file
        file = new File(Backpacks.get().getDataFolder(), "data" + File.separator + this.owner + ".yml");
        //Load the configuration for the file
        config = YamlConfiguration.loadConfiguration(file);
        //If the file doesn't exist then set the defaults
        if (!file.exists()) {
            setupFileDefaults(config);
        }
        save();
    }

    public BackpackDataFileUtil(UUID owner) {
        //Set instance variable
        this.owner = String.valueOf(owner);
        //Get the player file
        file = new File(Backpacks.get().getDataFolder(), "data" + File.separator + this.owner + ".yml");
        //Load the configuration for the file
        config = YamlConfiguration.loadConfiguration(file);
        this.backpackId = config.getString("uuid");
        //If the file doesn't exist then set the defaults
        save();
    }

    private void setupFileDefaults(YamlConfiguration config) {
        //Set defaults for the information about the players tiers and currency
        config.set("uuid", this.backpackId);
        config.set("created", new SimpleDateFormat(ConfigManager.CONFIG.get().getString("created-format")).format(new Date(System.currentTimeMillis())));
        config.set("capacity", ConfigManager.CONFIG.get().getInt("starting-capacity"));
        config.set("lifetime-amount", 0);
        config.createSection("contents");
        //Send a nice message
        LogUtil.info("Successfully created a new backpack data file: " + this.owner + ", actively creating / setting defaults.");
    }

    public void saveCapacity(int capcity) {
        config.set("capacity", capcity);
        save();
    }

    public void saveLifetimeAmount(int amount) {
        config.set("lifetime-amount", amount);
        save();
    }

    public void saveItem(UUID id, int amount) {
        config.set("contents." + id, amount);
        save();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            LogUtil.warning("Critical error saving the file: " + this.owner + ", please contact nbdSteve#0583 on discord.");
        }
        reload();
    }

    public void reload() {
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            LogUtil.warning("Critical error loading the file: " + this.owner + ", please contact nbdSteve#0583 on discord.");
        }
    }

    public void delete() {
        file.delete();
    }

    public YamlConfiguration get() {
        return config;
    }
}
