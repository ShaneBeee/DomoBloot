package com.shanebeestudios.domo.item;

import com.shanebeestudios.domo.DomoBloot;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

@SuppressWarnings("unused")
public class Item {

    private final NamespacedKey key;
    final ItemStack itemStack;

    Item(@NotNull String key, @NotNull Material material) {
        this.key = DomoBloot.getKey(key.toLowerCase(Locale.ROOT));

        // Add custom-item key to NBT
        NBTItem nbtItem = new NBTItem(new ItemStack(material));
        nbtItem.setString("key", this.key.toString());
        this.itemStack = nbtItem.getItem();
        Items.ITEMS.put(this.key.toString(), this);
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    public Material getBukkitType() {
        return itemStack.getType();
    }

    public int getAmount() {
        return itemStack.getAmount();
    }

    public String getMinecraftNamespace() {
        return itemStack.getType().getKey().toString();
    }

    public NBTItem getNBT() {
        return new NBTItem(itemStack);
    }

    public void drop(Player player, int amount) {
        World world = player.getWorld();
        ItemStack itemStack = this.itemStack.clone();
        itemStack.setAmount(amount);
        Entity drop = world.dropItem(player.getLocation(), itemStack);
        drop.setVelocity(new Vector(0, 0, 0));
    }

    @SuppressWarnings("deprecation")
    public String getName() {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta.hasDisplayName()) {
            return meta.getDisplayName();
        }
        return key.getKey().replace("_", " ");
    }

    public NamespacedKey getKey() {
        return key;
    }

    public boolean compare(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasTag("key")) {
            return nbtItem.getString("key").equalsIgnoreCase(this.key.toString());
        }
        return false;
    }

    public void use(Player player) {
    }

}
