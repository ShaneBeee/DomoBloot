package com.shanebeestudios.tl.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Item {

    private final String key;
    final ItemStack itemStack;

    Item(@NotNull String key, @NotNull ItemStack itemStack) {
        this.key = key;
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    public String getKey() {
        return key;
    }

}
