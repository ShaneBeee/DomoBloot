package com.shanebeestudios.tl.item;

import com.shanebeestudios.tl.TenLives;
import com.shanebeestudios.tl.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HealthSuit extends Item {

    private static final double HEALTH_MOD = 2;
    // This number puts a players run speed at .10 (same as vanilla walk speed)
    // when a player is wearing full armor
    private static final double SPEED_MOD = -0.00576923;

    HealthSuit(@NotNull Material material, @NotNull EquipmentSlot slot) {
        super("GOLD_PLATED_" + material.toString(), new ItemStack(material));
        // Item
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(Util.getColString("&6Gold Plated &3" + Util.caps(material.toString())));
        AttributeModifier healthMod = new AttributeModifier(UUID.randomUUID(), "health_gd_" + slot.toString().toLowerCase(), HEALTH_MOD,
                AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, healthMod);
        AttributeModifier speedMod = new AttributeModifier(UUID.randomUUID(), "speed_gd_" + slot.toString().toLowerCase(), SPEED_MOD,
                AttributeModifier.Operation.ADD_NUMBER,  slot);
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, speedMod);
        itemStack.setItemMeta(meta);

        // Recipe
        NamespacedKey key = new NamespacedKey(TenLives.getPlugin(), "gold_plated_" + material.toString().toLowerCase());
        Items.RECIPE_KEYS.add(key);
        ShapedRecipe shapedRecipe = new ShapedRecipe(key, itemStack);
        shapedRecipe.shape("ggg", "gcg", "ggg");
        shapedRecipe.setIngredient('g', Material.GOLD_NUGGET);
        shapedRecipe.setIngredient('c', material);
        Bukkit.addRecipe(shapedRecipe);

        Items.ITEMS.add(this);
    }

}
