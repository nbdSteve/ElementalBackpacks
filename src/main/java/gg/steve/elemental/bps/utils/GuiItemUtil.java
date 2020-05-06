package gg.steve.elemental.bps.utils;

import gg.steve.elemental.bps.Backpacks;
import gg.steve.elemental.bps.core.Backpack;
import gg.steve.elemental.tokens.api.TokensApi;
import gg.steve.elemental.tokens.core.TokenType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class GuiItemUtil {

    public static ItemStack createItem(ConfigurationSection section, String entry, Backpack backpack) {
        ItemBuilderUtil builder = new ItemBuilderUtil(section.getString(entry + ".item"), section.getString(entry + ".data"));
        if (section.getString(entry + ".owner") != null) {
            SkullMeta meta = (SkullMeta) builder.getItemMeta();
            if (section.getString(entry + ".owner").equalsIgnoreCase("owner")) {
                meta.setOwner(Bukkit.getOfflinePlayer(backpack.getOwner()).getName());
            } else {
                meta.setOwner(Bukkit.getOfflinePlayer(UUID.fromString(section.getString(entry + ".owner"))).getName());
            }
            builder.setItemMeta(meta);
        }
        builder.addName(section.getString(entry + ".name"));
        builder.setLorePlaceholders("{amount-filled}", "{lifetime-amount}", "{created}", "{uuid}", "{capacity}", "{balance}");
        builder.addLore(section.getStringList(entry + ".lore"),
                Backpacks.getNumberFormat().format(backpack.getAmountFilled()),
                Backpacks.getNumberFormat().format(backpack.getLifetimeAmount()),
                backpack.getCreated(),
                String.valueOf(backpack.getBackpackId()),
                Backpacks.getNumberFormat().format(backpack.getCapacity()),
                Backpacks.getNumberFormat().format(TokensApi.getTokenPlayer(backpack.getOwner()).getTokens(TokenType.TOKEN)));
        builder.addEnchantments(section.getStringList(entry + ".enchantments"));
        builder.addItemFlags(section.getStringList(entry + ".item-flags"));
        builder.addNBT();
        return builder.getItem();
    }
}
