package com.shanebeestudios.tl.item;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("SameParameterValue")
public class Items {

    static final Map<String, Item> ITEMS = new HashMap<>();

    // Health
    public static final Item IRON_HEALTH_STONE;
    public static final Item DIAMOND_HEALTH_STONE;
    public static final Item STRONG_DIAMOND_HEALTH_STONE;
    public static final Item EMERALD_HEALTH_STONE;

    // Gear
    public static final Item GOLD_PLATED_DIAMOND_HELMET;
    public static final Item GOLD_PLATED_DIAMOND_CHESTPLATE;
    public static final Item GOLD_PLATED_DIAMOND_LEGGINGS;
    public static final Item GOLD_PLATED_DIAMOND_BOOTS;

    // Consumables
    public static final Item ENERGY_DRINK;
    public static final Item ENERGY_DRINK_STRONG;
    public static final Item ENERGY_DRINK_LITE;
    public static final Item MILK_BOTTLE;

    // Misc
    public static final Item ROCK;

    static {
        IRON_HEALTH_STONE = new HealthStone("iron", Material.IRON_INGOT, Material.GOLD_NUGGET, 2, "&eHealth &6Stone", true);
        DIAMOND_HEALTH_STONE = new HealthStone("diamond", Material.DIAMOND, Material.IRON_NUGGET, 8, "&aHealth &2Stone", true);
        STRONG_DIAMOND_HEALTH_STONE = new HealthStone("strong_diamond", Material.EMERALD, Material.DIAMOND, 14, "&bHealth &3Stone", true);
        EMERALD_HEALTH_STONE = new HealthStone("emerald", Material.EMERALD, Material.PRISMARINE_SHARD, 20, "&bHealth &3Stone", true);
        GOLD_PLATED_DIAMOND_HELMET = new HealthSuit(Material.DIAMOND_HELMET, EquipmentSlot.HEAD);
        GOLD_PLATED_DIAMOND_CHESTPLATE = new HealthSuit(Material.DIAMOND_CHESTPLATE, EquipmentSlot.CHEST);
        GOLD_PLATED_DIAMOND_LEGGINGS = new HealthSuit(Material.DIAMOND_LEGGINGS, EquipmentSlot.LEGS);
        GOLD_PLATED_DIAMOND_BOOTS = new HealthSuit(Material.DIAMOND_BOOTS, EquipmentSlot.FEET);
        ENERGY_DRINK_LITE = new EnergyDrink("energy_drink_lite", "<#BF6FF4>&lLite Energy Drink", Material.POTION, 2.0, 0.5, Color.fromRGB(191, 111, 244));
        ENERGY_DRINK = new EnergyDrink("energy_drink", "<#05CA89>&lEnergy Drink", Material.POTION, 5.0, 1.25, Color.fromRGB(5, 202, 137));
        ENERGY_DRINK_STRONG = new EnergyDrink("energy_drink_strong", "<#C84911>&lStrong Energy Drink", Material.POTION, 10.0, 2.5, Color.fromRGB(200, 73, 17));
        MILK_BOTTLE = new MilkBottle("milk_bottle");
        ROCK = new CustomModelItem("rock", "<#8ABFBF>Rock", Material.STICK, 1);
    }

    public static Collection<Item> getItems() {
        return ITEMS.values();
    }

    public static boolean isItem(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.hasKey("key");
    }

    @Nullable
    public static Item getItemFromItemStack(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasKey("key")) {
            String key = nbtItem.getString("key");
            if (ITEMS.containsKey(key)) {
                return ITEMS.get(key);
            }
        }
        return null;
    }

}
