package gg.steve.elemental.bps.listener;

import gg.steve.elemental.booster.api.BoostersApi;
import gg.steve.elemental.booster.core.BoosterType;
import gg.steve.elemental.bps.Backpacks;
import gg.steve.elemental.bps.core.BackpackManager;
import gg.steve.elemental.bps.event.BackpackSellEvent;
import gg.steve.elemental.bps.event.PreBackpackSaleEvent;
import gg.steve.elemental.bps.event.SellMethodType;
import gg.steve.elemental.bps.message.MessageType;
import gg.steve.elemental.bps.utils.LogUtil;
import gg.steve.elemental.pets.api.PetApi;
import gg.steve.elemental.pets.core.PetType;
import gg.steve.elemental.pets.rarity.PetRarity;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

public class BackpackSellListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void preSale(PreBackpackSaleEvent event) {
        if (event.isCancelled()) return;
        Bukkit.getPluginManager().callEvent(new BackpackSellEvent(event.getBackpack(), event.getGroup(), event.getPet(), event.getAmountToSell(), event.getSellMethod()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void packSell(BackpackSellEvent event) {
        if (event.isCancelled()) return;
        int amountSold = 0, petProcAmount = 0;
        double petBoostAmount = 0, totalDeposit = 0;
        PetRarity rarity = null;
        double overallBoost = 0;
        if (event.getPet() != null) {
            rarity = PetApi.getPetRarity(event.getOwner().getPlayer(), PetType.MONEY);
            overallBoost += PetApi.getBoostAmount(PetType.MONEY) - 1;
        }
        overallBoost += BoostersApi.getBoostAmount(event.getOwner().getPlayer(), BoosterType.SELL);
        boolean saleComplete = false;
        for (UUID id : event.getBackpack().getContents().keySet()) {
            for (int i = 1; i <= event.getBackpack().getAmount(id); i++) {
                if (event.getAmountToSell() != -1 && amountSold >= event.getAmountToSell()) {
                    event.getBackpack().remove(id, i);
                    saleComplete = true;
                    break;
                }
                amountSold++;
                double boost = 1;
                if (event.getPet() != null && PetApi.isProcing(PetApi.getActivePet(event.getOwner().getPlayer(), PetType.MONEY), rarity)) {
                    boost = PetApi.getBoostAmount(PetType.MONEY);
                    petProcAmount++;
                }
                boost += BoostersApi.getBoostAmount(event.getOwner().getPlayer(), BoosterType.SELL);
                petBoostAmount += BackpackManager.getItemPrice(event.getGroup(), id) * boost - BackpackManager.getItemPrice(event.getGroup(), id);
                totalDeposit += BackpackManager.getItemPrice(event.getGroup(), id) * boost;
            }
            if (saleComplete) break;
            event.getBackpack().remove(id, event.getBackpack().getAmount(id));
        }
        if (event.getSellMethod().equals(SellMethodType.MERCHANT)) {
            totalDeposit *= 2;
            petBoostAmount *= 2;
        }
        if (Backpacks.eco() != null) {
            Backpacks.eco().depositPlayer(event.getOwner().getPlayer(), totalDeposit);
        } else {
            LogUtil.warning("No amount was deposited because there isn't an economy.");
        }
        if (event.getSellMethod().equals(SellMethodType.COMMAND)) {
            MessageType.SELL_ITEMS.message(event.getOwner().getPlayer(),
                    Backpacks.getNumberFormat().format(amountSold),
                    Backpacks.getNumberFormat().format(totalDeposit),
                    Backpacks.getNumberFormat().format(overallBoost * 100),
                    Backpacks.getNumberFormat().format(petProcAmount),
                    Backpacks.getNumberFormat().format(petBoostAmount));
        } else if (event.getSellMethod().equals(SellMethodType.BUSTER)) {
            MessageType.BUSTER_SELL_ITEMS.message(event.getOwner().getPlayer(),
                    Backpacks.getNumberFormat().format(amountSold),
                    Backpacks.getNumberFormat().format(totalDeposit),
                    Backpacks.getNumberFormat().format(overallBoost * 100),
                    Backpacks.getNumberFormat().format(petProcAmount),
                    Backpacks.getNumberFormat().format(petBoostAmount));
        } else if (event.getSellMethod().equals(SellMethodType.MERCHANT)) {
            MessageType.MERCHANT_SELL_ITEMS.message(event.getOwner().getPlayer(),
                    Backpacks.getNumberFormat().format(amountSold),
                    Backpacks.getNumberFormat().format(totalDeposit),
                    Backpacks.getNumberFormat().format(overallBoost * 100),
                    Backpacks.getNumberFormat().format(petProcAmount),
                    Backpacks.getNumberFormat().format(petBoostAmount));
        }
    }
}
