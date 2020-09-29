package com.shanebeestudios.tl.item;

import com.shanebeestudios.tl.TenLives;
import com.shanebeestudios.tl.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HealthStone extends Item {
    // This number puts a players run speed at .10 (same as vanilla walk speed)
    // when a player is wearing full armor
    private static final double SPEED_MOD = -0.00576923;

    HealthStone(@NotNull String stoneKey, Material result, Material modMaterial, double health, String name, boolean glowing) {
        super(stoneKey + "_HEALTH_STONE", new ItemStack(result));
        // Item
        ItemMeta meta = itemStack.getItemMeta();
        String keyName = result.toString().toLowerCase();
        meta.setDisplayName(Util.getColString(name));
        if (glowing) {
            meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        AttributeModifier modifier = new AttributeModifier(
                UUID.randomUUID(),
                "hs_health_" + keyName,
                health,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlot.OFF_HAND);
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, modifier);
        AttributeModifier speedMod = new AttributeModifier(
                UUID.randomUUID(),
                "hs_speed_" + keyName,
                SPEED_MOD,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlot.OFF_HAND);
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, speedMod);
        itemStack.setItemMeta(meta);

        // Recipe
        NamespacedKey nameKey = new NamespacedKey(TenLives.getPlugin(), "health_stone_" + keyName + "_" + health);
        Items.RECIPE_KEYS.add(nameKey);
        ShapedRecipe shapedRecipe = new ShapedRecipe(nameKey, itemStack);
        shapedRecipe.shape(" e ", "ded", " d ");
        shapedRecipe.setIngredient('e', result);
        shapedRecipe.setIngredient('d', modMaterial);
        Bukkit.addRecipe(shapedRecipe);

        Items.ITEMS.add(this);
    }
}
