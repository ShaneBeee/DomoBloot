package com.shanebeestudios.tl.item;

import com.shanebeestudios.tl.util.Util;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SameParameterValue")
public class Items {

    static final List<NamespacedKey> RECIPE_KEYS = new ArrayList<>();
    static final List<Item> ITEMS = new ArrayList<>();

    public static final Item IRON_HEALTH_STONE;
    public static final Item DIAMOND_HEALTH_STONE;
    public static final Item STRONG_DIAMOND_HEALTH_STONE;
    public static final Item EMERALD_HEALTH_STONE;
    public static final Item GOLD_PLATED_DIAMOND_HELMET;
    public static final Item GOLD_PLATED_DIAMOND_CHESTPLATE;
    public static final Item GOLD_PLATED_DIAMOND_LEGGINGS;
    public static final Item GOLD_PLATED_DIAMOND_BOOTS;

    static {
        IRON_HEALTH_STONE = new HealthStone("IRON", Material.IRON_INGOT, Material.GOLD_NUGGET, 2, "&eHealth &6Stone", true);
        DIAMOND_HEALTH_STONE = new HealthStone("DIAMOND", Material.DIAMOND, Material.IRON_NUGGET, 8, "&aHealth &2Stone", true);
        STRONG_DIAMOND_HEALTH_STONE = new HealthStone("STRONG_DIAMOND", Material.EMERALD, Material.DIAMOND, 14, "&bHealth &3Stone", true);
        EMERALD_HEALTH_STONE = new HealthStone("EMERALD", Material.EMERALD, Material.PRISMARINE_SHARD, 20, "&bHealth &3Stone", true);
        GOLD_PLATED_DIAMOND_HELMET = new HealthSuit(Material.DIAMOND_HELMET, EquipmentSlot.HEAD);
        GOLD_PLATED_DIAMOND_CHESTPLATE = new HealthSuit(Material.DIAMOND_CHESTPLATE, EquipmentSlot.CHEST);
        GOLD_PLATED_DIAMOND_LEGGINGS = new HealthSuit(Material.DIAMOND_LEGGINGS, EquipmentSlot.LEGS);
        GOLD_PLATED_DIAMOND_BOOTS = new HealthSuit(Material.DIAMOND_BOOTS, EquipmentSlot.FEET);
    }

    public static List<Item> getItems() {
        return ITEMS;
    }

    public static void unlockAllCustomRecipes(@NotNull Player player) {
        for (NamespacedKey key : RECIPE_KEYS) {
            player.discoverRecipe(key);
        }
    }

    public static void registerRecipes() {
        Util.log("Registered &b" + RECIPE_KEYS.size() + "&7 recipes");
    }

}
