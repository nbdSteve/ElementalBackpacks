package gg.steve.elemental.bps.cmd.sub;

import gg.steve.elemental.bps.core.BackpackManager;
import gg.steve.elemental.bps.message.CommandDebug;
import gg.steve.elemental.bps.permission.PermissionNode;
import gg.steve.elemental.bps.player.BackpackPlayer;
import gg.steve.elemental.bps.player.PlayerBackpackManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SellCmd {

    public static void sell(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_ACCESSIBLE.message(sender);
            return;
        }
        if (!PermissionNode.GUI.hasPermission(sender)) {
            CommandDebug.INSUFFICIENT_PERMISSION.message(sender, PermissionNode.GUI.get());
            return;
        }
        String group = "all";
        if (args.length == 2) {
            if (!BackpackManager.isSellGroup(args[1])) {
                // message
                return;
            } else {
                group = args[1];
            }
        }
        BackpackPlayer player = PlayerBackpackManager.getBackpackPlayer(((Player) sender).getUniqueId());
        BackpackManager.sellGroup(player, group);
    }
}
