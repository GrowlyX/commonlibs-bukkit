package com.solexgames.lib.commons.menu;

import com.solexgames.lib.commons.menu.slot.Slot;
import org.bukkit.inventory.Inventory;

import java.util.Map;

/**
 * @author GrowlyX
 * @since 6/29/2021
 */

public abstract class Menu {



    public abstract String getTitle();

    public abstract int getSize();

    public abstract Map<Integer, Slot> getSlots();

}
