package com.solexgames.lib.commons.menu.slot;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

/**
 * @author GrowlyX
 * @since 6/29/2021
 */

public abstract class Slot {

    public abstract ItemStack getItem();

    public abstract BiConsumer<Player, ClickType> onClick();

}
