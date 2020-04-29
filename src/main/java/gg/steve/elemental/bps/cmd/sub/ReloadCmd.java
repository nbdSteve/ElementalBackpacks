package gg.steve.elemental.bps.cmd.sub;

import gg.steve.elemental.bps.Backpacks;
import gg.steve.elemental.bps.managers.ConfigManager;
import gg.steve.elemental.bps.message.CommandDebug;
import gg.steve.elemental.bps.message.MessageType;
import gg.steve.elemental.bps.permission.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ReloadCmd {

    public static void reload(CommandSender sender) {
        if (!PermissionNode.RELOAD.hasPermission(sender)) {
            CommandDebug.INSUFFICIENT_PERMISSION.message(sender, PermissionNode.RELOAD.get());
            return;
        }
        Bukkit.getPluginManager().disablePlugin(Backpacks.get());
        Bukkit.getPluginManager().enablePlugin(Backpacks.get());
        ConfigManager.reload();
        MessageType.RELOAD.message(sender);
    }
}
