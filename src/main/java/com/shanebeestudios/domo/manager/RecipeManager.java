package com.shanebeestudios.domo.manager;

import com.shanebeestudios.domo.DomoBloot;
import com.shanebeestudios.domo.item.HealthStone;
import com.shanebeestudios.domo.item.Item;
import com.shanebeestudios.domo.item.Items;
import com.shanebeestudios.domo.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.SmokingRecipe;
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
        registerHealthStoneRecipe(Items.IRON_HEALTH_STONE);
        registerHealthStoneRecipe(Items.DIAMOND_HEALTH_STONE);
        registerHealthStoneRecipe(Items.STRONG_DIAMOND_HEALTH_STONE);
        registerHealthStoneRecipe(Items.EMERALD_HEALTH_STONE);

        registerHealthSuitRecipe(Items.GOLD_PLATED_DIAMOND_HELMET);
        registerHealthSuitRecipe(Items.GOLD_PLATED_DIAMOND_CHESTPLATE);
        registerHealthSuitRecipe(Items.GOLD_PLATED_DIAMOND_LEGGINGS);
        registerHealthSuitRecipe(Items.GOLD_PLATED_DIAMOND_BOOTS);

        registerEnergyDrinkRecipe(Items.ENERGY_DRINK_LITE, Material.CACTUS);
        registerEnergyDrinkRecipe(Items.ENERGY_DRINK, Material.ROTTEN_FLESH);
        registerEnergyDrinkRecipe(Items.ENERGY_DRINK_STRONG, Material.HONEY_BOTTLE);

        registerOtherRecipes();
        Util.log("Registered &b" + RECIPE_KEYS.size() + "&7 recipes");
    }

    //<editor-fold desc="keep-folded">
    private void registerHealthStoneRecipe(HealthStone stone) {
        ItemStack itemStack = stone.getItemStack();
        Material material = itemStack.getType();
        NamespacedKey nameKey = stone.getKey();
        RECIPE_KEYS.add(nameKey);

        ShapedRecipe shapedRecipe = new ShapedRecipe(nameKey, itemStack);
        shapedRecipe.shape(" e ", "ded", " d ");
        shapedRecipe.setIngredient('e', material);
        shapedRecipe.setIngredient('d', stone.getModMaterial());
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

    private void registerOtherRecipes() {
        // Coffee Bean Recipe
        NamespacedKey coffeeBeanKey = Items.COFFEE_BEAN.getKey();
        SmokingRecipe coffeeBeanRecipe = new SmokingRecipe(coffeeBeanKey, Items.COFFEE_BEAN.getItemStack(), Material.COCOA_BEANS, 0.0f, 300);
        Bukkit.addRecipe(coffeeBeanRecipe);
        RECIPE_KEYS.add(coffeeBeanKey);

        // Coffee Recipe
        NamespacedKey coffeeKey = Items.COFFEE.getKey();
        ShapedRecipe coffeeRecipe = new ShapedRecipe(coffeeKey, Items.COFFEE.getItemStack());
        coffeeRecipe.shape(" a ", " c ", " b ");
        coffeeRecipe.setIngredient('a', Items.MILK_BOTTLE.getItemStack());
        coffeeRecipe.setIngredient('b', Items.COFFEE_BEAN.getItemStack());
        coffeeRecipe.setIngredient('c', Material.SUGAR);
        Bukkit.addRecipe(coffeeRecipe);
        RECIPE_KEYS.add(coffeeKey);

        // Strong Coffee Recipe
        NamespacedKey strongCoffeeKey = Items.COFFEE_STRONG.getKey();
        ShapedRecipe strongCoffeeRecipe = new ShapedRecipe(strongCoffeeKey, Items.COFFEE_STRONG.getItemStack());
        strongCoffeeRecipe.shape(" a ", " cc", " bb");
        strongCoffeeRecipe.setIngredient('a', Items.MILK_BOTTLE.getItemStack());
        strongCoffeeRecipe.setIngredient('b', Items.COFFEE_BEAN.getItemStack());
        strongCoffeeRecipe.setIngredient('c', Material.SUGAR);
        Bukkit.addRecipe(strongCoffeeRecipe);
        RECIPE_KEYS.add(strongCoffeeKey);
    }
    //</editor-fold>

    public void unlockAllCustomRecipes(@NotNull Player player) {
        for (NamespacedKey key : RECIPE_KEYS) {
            player.discoverRecipe(key);
        }
    }

}
