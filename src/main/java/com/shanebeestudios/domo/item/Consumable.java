package com.shanebeestudios.domo.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Consumable extends Item {

    Consumable(@NotNull String key, @NotNull Material material) {
        super(key, material);
    }

    public void consume(Player player) {}

}
