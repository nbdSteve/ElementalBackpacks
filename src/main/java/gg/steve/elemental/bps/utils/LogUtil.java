package gg.steve.elemental.bps.utils;

import gg.steve.elemental.bps.Backpacks;

public class LogUtil {

    public static void info(String message) {
        Backpacks.get().getLogger().info(message);
    }

    public static void warning(String message) {
        Backpacks.get().getLogger().warning(message);
    }
}
