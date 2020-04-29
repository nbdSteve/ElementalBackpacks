package gg.steve.elemental.bps.cmd.sub;

import gg.steve.elemental.bps.message.CommandDebug;
import gg.steve.elemental.bps.permission.PermissionNode;
import gg.steve.elemental.bps.player.PlayerBackpackManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuiCmd {

    public static void gui(CommandSender sender) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_ACCESSIBLE.message(sender);
            return;
        }
        if (!PermissionNode.GUI.hasPermission(sender)) {
            CommandDebug.INSUFFICIENT_PERMISSION.message(sender, PermissionNode.GUI.get());
            return;
        }
        Player player = (Player) sender;
        PlayerBackpackManager.getBackpackPlayer(player.getUniqueId()).getBackpack().openGui(player);
    }
}
