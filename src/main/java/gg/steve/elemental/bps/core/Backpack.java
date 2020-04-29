package gg.steve.elemental.bps.core;

import gg.steve.elemental.bps.core.exception.LoadBackpackFullException;
import gg.steve.elemental.bps.gui.BackpackGui;
import gg.steve.elemental.bps.managers.ConfigManager;
import gg.steve.elemental.bps.player.PlayerBackpackManager;
import gg.steve.elemental.bps.utils.LogUtil;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Backpack {
    private UUID owner;
    private UUID backpackId;
    private BackpackDataFileUtil data;
    private int capacity;
    private int amountFilled;
    private int lifetimeAmount;
    private Map<UUID, Integer> contents;
    private String created;
    private BackpackGui gui;

    public Backpack(UUID owner) {
        this.owner = owner;
        this.backpackId = UUID.randomUUID();
        this.data = new BackpackDataFileUtil(this.owner, this.backpackId);
        this.capacity = data.get().getInt("capacity");
        this.amountFilled = 0;
        this.lifetimeAmount = data.get().getInt("lifetime-amount");
        this.created = this.data.get().getString("created");
        try {
            this.loadContents();
        } catch (LoadBackpackFullException e) {
            e.printStackTrace();
            LogUtil.info("Error while loading backpack for: " + this.owner + ", the contents is larger than the capacity");
        }
    }

    public void loadContents() throws LoadBackpackFullException {
        this.contents = new HashMap<>();
        for (String entry : data.get().getConfigurationSection("contents").getKeys(false)) {
            if (this.amountFilled + data.get().getInt("contents." + entry) > this.capacity) {
                throw new LoadBackpackFullException();
            }
            this.amountFilled += data.get().getInt("contents." + entry);
            contents.put(UUID.fromString(entry), data.get().getInt("contents." + entry));
        }
    }

    /**
     * returns false if the item could not be added to the backpack, true otherwise
     *
     * @param itemId
     * @param amount
     * @return boolean
     */
    public boolean add(UUID itemId, int amount) {
        if (this.contents == null) this.contents = new HashMap<>();
        if (this.amountFilled + amount > this.capacity) {
            return false;
        }
        this.amountFilled += amount;
        this.lifetimeAmount += amount;
        if (this.contents.containsKey(itemId)) {
            amount += getAmount(itemId);
        }
        this.contents.put(itemId, amount);
        return true;
    }

    /**
     * returns false if the item could not be removed from the backpack, true otherwise
     *
     * @param itemId
     * @param amount
     * @return boolean
     */
    public boolean remove(UUID itemId, int amount) {
        if (this.contents == null) this.contents = new HashMap<>();
        if (!this.contents.containsKey(itemId) || amount > getAmount(itemId)) {
            return false;
        }
        this.amountFilled -= amount;
        this.contents.put(itemId, getAmount(itemId) - amount);
        return true;
    }

    public int getAmount(UUID itemId) {
        if (this.contents.containsKey(itemId)) {
            return this.contents.get(itemId);
        }
        return 0;
    }

    public void increaseCapacity(int amount) {
        this.capacity += amount;
    }

    public void saveToFile() {
        this.data.saveCapacity(this.capacity);
        this.data.saveLifetimeAmount(this.lifetimeAmount);
        if (this.contents == null || this.contents.isEmpty()) return;
        for (UUID itemId : contents.keySet()) {
            this.data.saveItem(itemId, this.contents.get(itemId));
        }
    }

    public void openGui(Player player) {
        if (this.gui == null) {
            this.gui = new BackpackGui(ConfigManager.CONFIG.get().getConfigurationSection("gui"), PlayerBackpackManager.getBackpackPlayer(this.owner));
        } else {
            this.gui.refresh();
        }
        this.gui.open(player);
    }

    public boolean hasItem(UUID id) {
        return contents.containsKey(id);
    }

    public int getLifetimeAmount() {
        return lifetimeAmount;
    }

    public UUID getOwner() {
        return owner;
    }

    public UUID getBackpackId() {
        return backpackId;
    }

    public BackpackDataFileUtil getData() {
        return data;
    }

    public int getAmountFilled() {
        return amountFilled;
    }

    public Map<UUID, Integer> getContents() {
        return contents;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isFilled() {
        return this.amountFilled == this.capacity;
    }

    public String getCreated() {
        return created;
    }
}