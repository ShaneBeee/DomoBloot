package com.shanebeestudios.tl.item;

import com.shanebeestudios.tl.TenLives;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Item {

    private final NamespacedKey key;
    final ItemStack itemStack;

    Item(@NotNull String key, @NotNull ItemStack itemStack) {
        this.key = new NamespacedKey(TenLives.getPlugin(), key.toLowerCase());

        // Add custom-item key to NBT
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setString("key", this.key.toString());
        this.itemStack = nbtItem.getItem();
        Items.ITEMS.put(this.key.toString(), this);
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    public NamespacedKey getKey() {
        return key;
    }

    public boolean compare(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("key")) {
            return nbtItem.getString("key").equalsIgnoreCase(this.key.toString());
        }
        return false;
    }

    public void use(Player player) {
    }

}
