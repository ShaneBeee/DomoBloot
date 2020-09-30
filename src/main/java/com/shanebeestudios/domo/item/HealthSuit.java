package com.shanebeestudios.domo.item;

import com.shanebeestudios.domo.util.Util;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class HealthSuit extends Item {

    private static final double HEALTH_MOD = 2;
    // This number puts a players run speed at .10 (same as vanilla walk speed)
    // when a player is wearing full armor
    private static final double SPEED_MOD = -0.00576923;

    HealthSuit(@NotNull Material material, @NotNull EquipmentSlot slot) {
        super("gold_plated_" + material.toString().toLowerCase(), new ItemStack(material));
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
    }

}
