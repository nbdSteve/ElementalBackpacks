package gg.steve.elemental.bps.event;

import gg.steve.elemental.bps.core.Backpack;
import gg.steve.elemental.bps.player.BackpackPlayer;
import gg.steve.elemental.pets.core.Pet;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BackpackSellEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private Backpack backpack;
    private String group;
    private Pet pet;
    private int amountToSell;
    private SellMethodType sellMethod;
    private boolean cancel;

    public BackpackSellEvent(Backpack backpack, String group, Pet pet, int amountToSell, SellMethodType sellMethod) {
        this.backpack = backpack;
        this.group = group;
        this.pet = pet;
        this.amountToSell = amountToSell;
        this.sellMethod = sellMethod;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Backpack getBackpack() {
        return backpack;
    }

    public String getGroup() {
        return group;
    }

    public Pet getPet() {
        return pet;
    }

    public SellMethodType getSellMethod() {
        return sellMethod;
    }

    public int getAmountToSell() {
        return amountToSell;
    }

    public BackpackPlayer getOwner() {
        return this.backpack.getPlayer();
    }
}
