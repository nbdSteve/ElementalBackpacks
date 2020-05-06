package gg.steve.elemental.bps.gui;

import gg.steve.elemental.bps.Backpacks;
import gg.steve.elemental.bps.message.CommandDebug;
import gg.steve.elemental.bps.message.MessageType;
import gg.steve.elemental.bps.permission.PermissionNode;
import gg.steve.elemental.bps.player.BackpackPlayer;
import gg.steve.elemental.bps.utils.GuiItemUtil;
import gg.steve.elemental.tokens.api.TokensApi;
import gg.steve.elemental.tokens.core.TokenPlayer;
import gg.steve.elemental.tokens.core.TokenType;
import org.bukkit.configuration.ConfigurationSection;

public class BackpackGui extends AbstractGui {
    private BackpackPlayer player;
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
                        player1.closeInventory();
                        doCapacityUpgrade(section.getInt(entry + ".cost"), section.getInt(entry + ".action"));
                        break;
                }
            });
        }
    }

    private void doCapacityUpgrade(int cost, int amount) {
        // check that player can upgrade
        if (!PermissionNode.CAPACITY.hasPermission(this.player.getPlayer().getPlayer())) {
            CommandDebug.INSUFFICIENT_PERMISSION.message(this.player.getPlayer().getPlayer(), PermissionNode.CAPACITY.get());
            return;
        }
        // check that player has enough money first
        if (TokensApi.getInstance() != null) {
            TokenPlayer tokenPlayer = TokensApi.getTokenPlayer(this.player.getPlayer().getUniqueId());
            if (tokenPlayer.getTokens(TokenType.TOKEN) < cost) {
                MessageType.INSUFFICIENT_TOKENS.message(this.player.getPlayer().getPlayer(), TokenType.TOKEN.name());
                return;
            } else {
                tokenPlayer.removeTokens(TokenType.TOKEN, cost);
            }
        }
        this.player.getBackpack().increaseCapacity(amount);
        MessageType.INCREASE_CAPACITY.message(this.player.getPlayer().getPlayer(), Backpacks.getNumberFormat().format(amount), Backpacks.getNumberFormat().format(this.player.getBackpack().getCapacity()));
    }
}