package gg.steve.elemental.bps.papi;

import gg.steve.elemental.bps.Backpacks;
import gg.steve.elemental.bps.player.BackpackPlayer;
import gg.steve.elemental.bps.player.PlayerBackpackManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class BackpacksExpansion extends PlaceholderExpansion {
    private Backpacks instance;

    public BackpacksExpansion(Backpacks instance) {
        this.instance = instance;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return instance.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "backpacks";
    }

    @Override
    public String getVersion() {
        return instance.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) return "";
        BackpackPlayer bPlayer = PlayerBackpackManager.getBackpackPlayer(player.getUniqueId());
        if (identifier.equalsIgnoreCase("filled")) {
            return Backpacks.getNumberFormat().format(bPlayer.getBackpack().getAmountFilled());
        }
        if (identifier.equalsIgnoreCase("is-filled")) {
            return String.valueOf(bPlayer.getBackpack().isFilled());
        }
        if (identifier.equalsIgnoreCase("capacity")) {
            return Backpacks.getNumberFormat().format(bPlayer.getBackpack().getCapacity());
        }
        if (identifier.equalsIgnoreCase("lifetime")) {
            return Backpacks.getNumberFormat().format(bPlayer.getBackpack().getLifetimeAmount());
        }
        if (identifier.equalsIgnoreCase("created")) {
            return bPlayer.getBackpack().getCreated();
        }
        return "0";
    }
}
