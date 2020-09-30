package com.shanebeestudios.domo.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Consumable extends Item {

    Consumable(@NotNull String key, @NotNull ItemStack itemStack) {
        super(key, itemStack);
    }

    public void consume(Player player) {}

}
