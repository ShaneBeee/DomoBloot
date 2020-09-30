package com.shanebeestudios.tl.manager;

import com.shanebeestudios.tl.DomoBloot;
import com.shanebeestudios.tl.item.Item;
import com.shanebeestudios.tl.item.Items;
import com.shanebeestudios.tl.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class RecipeManager {

    private final DomoBloot plugin;
    final List<NamespacedKey> RECIPE_KEYS = new ArrayList<>();

    public RecipeManager(DomoBloot plugin) {
        this.plugin = plugin;
        loadRecipes();
    }

    private void loadRecipes() {
        registerHealthStoneRecipe(Items.IRON_HEALTH_STONE, Material.GOLD_NUGGET);
        registerHealthStoneRecipe(Items.DIAMOND_HEALTH_STONE, Material.IRON_NUGGET);
        registerHealthStoneRecipe(Items.STRONG_DIAMOND_HEALTH_STONE, Material.DIAMOND);
        registerHealthStoneRecipe(Items.EMERALD_HEALTH_STONE, Material.PRISMARINE_SHARD);

        registerHealthSuitRecipe(Items.GOLD_PLATED_DIAMOND_HELMET);
        registerHealthSuitRecipe(Items.GOLD_PLATED_DIAMOND_CHESTPLATE);
        registerHealthSuitRecipe(Items.GOLD_PLATED_DIAMOND_LEGGINGS);
        registerHealthSuitRecipe(Items.GOLD_PLATED_DIAMOND_BOOTS);

        registerEnergyDrinkRecipe(Items.ENERGY_DRINK_LITE, Material.CACTUS);
        registerEnergyDrinkRecipe(Items.ENERGY_DRINK, Material.ROTTEN_FLESH);
        registerEnergyDrinkRecipe(Items.ENERGY_DRINK_STRONG, Material.HONEY_BOTTLE);
        Util.log("Registered &b" + RECIPE_KEYS.size() + "&7 recipes");
    }

    //<editor-fold desc="keep-folded">
    private void registerHealthStoneRecipe(Item stone, Material modMaterial) {
        ItemStack itemStack = stone.getItemStack();
        Material material = itemStack.getType();
        NamespacedKey nameKey = stone.getKey();
        RECIPE_KEYS.add(nameKey);

        ShapedRecipe shapedRecipe = new ShapedRecipe(nameKey, itemStack);
        shapedRecipe.shape(" e ", "ded", " d ");
        shapedRecipe.setIngredient('e', material);
        shapedRecipe.setIngredient('d', modMaterial);
        Bukkit.addRecipe(shapedRecipe);
    }

    private void registerHealthSuitRecipe(Item suit) {
        ItemStack itemStack = suit.getItemStack();
        NamespacedKey key = suit.getKey();
        RECIPE_KEYS.add(key);

        ShapedRecipe shapedRecipe = new ShapedRecipe(key, itemStack);
        shapedRecipe.shape("ggg", "gcg", "ggg");
        shapedRecipe.setIngredient('g', Material.GOLD_NUGGET);
        shapedRecipe.setIngredient('c', itemStack.getType());
        Bukkit.addRecipe(shapedRecipe);
    }

    private void registerEnergyDrinkRecipe(Item drink, Material modMaterial) {
        ItemStack itemStack = drink.getItemStack();
        NamespacedKey key = drink.getKey();
        RECIPE_KEYS.add(key);

        ItemStack waterBottle = new ItemStack(Material.POTION);
        PotionMeta meta = ((PotionMeta) waterBottle.getItemMeta());
        meta.setBasePotionData(new PotionData(PotionType.WATER));
        waterBottle.setItemMeta(meta);

        ShapedRecipe shapedRecipe = new ShapedRecipe(key, itemStack);
        shapedRecipe.shape(" a ", " b ", " c ");
        shapedRecipe.setIngredient('a', waterBottle);
        shapedRecipe.setIngredient('b', Material.SUGAR);
        shapedRecipe.setIngredient('c', modMaterial);
        Bukkit.addRecipe(shapedRecipe);
    }
    //</editor-fold>

    public void unlockAllCustomRecipes(@NotNull Player player) {
        for (NamespacedKey key : RECIPE_KEYS) {
            player.discoverRecipe(key);
        }
    }

}
