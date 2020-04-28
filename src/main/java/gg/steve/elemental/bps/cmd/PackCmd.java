package gg.steve.elemental.bps.cmd;

import gg.steve.elemental.bps.player.PlayerBackpackManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PackCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerBackpackManager.getBackpackPlayer(player.getUniqueId()).getBackpack().openGui(player);
        }
        return true;
    }
}
