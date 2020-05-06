package gg.steve.elemental.bps.message;

import gg.steve.elemental.bps.managers.ConfigManager;
import gg.steve.elemental.bps.utils.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public enum MessageType {
    RELOAD("reload"),
    HELP("help"),
    INCREASE_CAPACITY("increase-capacity", "{amount}", "{total-capacity}"),
    BACKPACK_FULL("backpack-full", "{total-capacity}"),
    SELL_ITEMS("sell-items", "{items-sold}", "{deposit}", "{booster}", "{times-proced}", "{booster-amount}"),
    INSUFFICIENT_TOKENS("insufficient-tokens", "{token-type}");

    private String path;
    private List<String> placeholders;

    MessageType(String path, String... placeholders) {
        this.path = path;
        this.placeholders = Arrays.asList(placeholders);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void message(Player receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (String line : ConfigManager.MESSAGES.get().getStringList(this.path)) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), data.get(i));
            }
            receiver.sendMessage(ColorUtil.colorize(line));
        }
    }

    public void message(CommandSender receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (String line : ConfigManager.MESSAGES.get().getStringList(this.path)) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), data.get(i));
            }
            receiver.sendMessage(ColorUtil.colorize(line));
        }
    }

    public static void doMessage(Player receiver, List<String> lines) {
        for (String line : lines) {
            receiver.sendMessage(ColorUtil.colorize(line));
        }
    }
}