package gg.steve.elemental.bps.message;

import gg.steve.elemental.bps.managers.ConfigManager;
import gg.steve.elemental.bps.utils.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public enum CommandDebug {
    INSUFFICIENT_PERMISSION("insufficient-permission", "{node}"),
    INVALID_COMMAND("invalid-command"),
    INVALID_NUMBER_OF_ARGUMENTS("invalid-number-of-arguments"),
    TARGET_NOT_ONLINE("target-not-online"),
    INVALID_SELL_GROUP("invalid-sell-group"),
    ONLY_PLAYERS_ACCESSIBLE("only-player-accessible");

    private final String path;
    private List<String> placeholders;

    CommandDebug(String path, String... placeholders) {
        this.path = path;
        this.placeholders = Arrays.asList(placeholders);
    }

    public void message(Player receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (String line : ConfigManager.DEBUG.get().getStringList(this.path)) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), data.get(i));
            }
            receiver.sendMessage(ColorUtil.colorize(line));
        }
    }

    public void message(CommandSender receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (String line : ConfigManager.DEBUG.get().getStringList(this.path)) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), data.get(i));
            }
            receiver.sendMessage(ColorUtil.colorize(line));
        }
    }
}
