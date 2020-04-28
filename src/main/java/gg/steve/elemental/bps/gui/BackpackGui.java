package gg.steve.elemental.bps.gui;

import gg.steve.elemental.bps.player.BackpackPlayer;
import gg.steve.elemental.bps.utils.GuiItemUtil;
import org.bukkit.configuration.ConfigurationSection;

public class BackpackGui extends AbstractGui {
    BackpackPlayer player;
    private ConfigurationSection section;

    /**
     * Constructor the create a new Gui
     *
     * @param section
     */
    public BackpackGui(ConfigurationSection section, BackpackPlayer player) {
        super(section, section.getString("type"), section.getInt("size"), player);
        this.player = player;
        this.section = section;
        refresh();
    }

    public void refresh() {
        for (String entry : section.getKeys(false)) {
            try {
                Integer.parseInt(entry);
            } catch (Exception e) {
                continue;
            }
            setItemInSlot(section.getInt(entry + ".slot"), GuiItemUtil.createItem(section, entry, this.player.getBackpack()), player1 -> {
                switch (section.getString(entry + ".action")) {
                    case "none":
                        break;
                    case "close":
                        player1.closeInventory();
                        break;
                    default:
                        doCapacityUpgrade(section.getInt(entry + ".cost"), section.getInt(entry + ".action"));
                        break;
                }
            });
        }
    }

    private void doCapacityUpgrade(double cost, int amount) {
        // check that player has enough money first
        this.player.getBackpack().increaseCapacity(amount);
        // send a message
    }
}
