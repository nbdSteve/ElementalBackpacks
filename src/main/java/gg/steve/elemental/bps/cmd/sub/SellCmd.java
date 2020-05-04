package gg.steve.elemental.bps.cmd.sub;

import gg.steve.elemental.bps.core.BackpackManager;
import gg.steve.elemental.bps.message.CommandDebug;
import gg.steve.elemental.bps.permission.PermissionNode;
import gg.steve.elemental.bps.player.BackpackPlayer;
import gg.steve.elemental.bps.player.PlayerBackpackManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SellCmd {

    public static void sell(CommandSender sender, String[] args) {
        if (args.length > 2) {
            if (!PermissionNode.ADMIN_SELL.hasPermission(sender)) {
                CommandDebug.INSUFFICIENT_PERMISSION.message(sender, PermissionNode.ADMIN_SELL.get());
                return;
            }
            consoleSell(sender, args);
            return;
        }
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_ACCESSIBLE.message(sender);
            return;
        }
        if (!PermissionNode.SELL.hasPermission(sender)) {
            CommandDebug.INSUFFICIENT_PERMISSION.message(sender, PermissionNode.SELL.get());
            return;
        }
        String group = "all";
        if (args.length == 2) {
            if (!BackpackManager.isSellGroup(args[1])) {
                CommandDebug.INVALID_SELL_GROUP.message(sender);
                return;
            } else {
                group = args[1];
            }
        }
        BackpackPlayer player = PlayerBackpackManager.getBackpackPlayer(((Player) sender).getUniqueId());
        BackpackManager.sellGroup(player, group);
    }


    public static void consoleSell(CommandSender sender, String[] args) {
        // /pack sell nbdsteve all
        if (args.length != 3) {
            CommandDebug.INVALID_NUMBER_OF_ARGUMENTS.message(sender);
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            CommandDebug.TARGET_NOT_ONLINE.message(sender);
            return;
        }
        String group = args[2];
        if (!group.equalsIgnoreCase("all")) {
            if (!BackpackManager.isSellGroup(args[1])) {
                CommandDebug.INVALID_SELL_GROUP.message(sender);
                return;
            }
        }
        BackpackPlayer player = PlayerBackpackManager.getBackpackPlayer(target.getUniqueId());
        BackpackManager.sellGroup(player, group);
    }
}
