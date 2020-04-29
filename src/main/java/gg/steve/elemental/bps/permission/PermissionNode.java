package gg.steve.elemental.bps.permission;

import gg.steve.elemental.bps.managers.ConfigManager;
import org.bukkit.command.CommandSender;

public enum PermissionNode {
    GUI("command.gui"),
    CAPACITY("gui.increase-capacity"),
    RELOAD("command.reload"),
    HELP("command.help");

    private String path;

    PermissionNode(String path) {
        this.path = path;
    }

    public String get() {
        return ConfigManager.PERMISSIONS.get().getString(this.path);
    }

    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(get());
    }
}