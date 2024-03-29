package com.shanebeestudios.domo.item;

import com.shanebeestudios.domo.util.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class CustomModelItem extends Item {

    @SuppressWarnings("deprecation") // Paper deprecation
    CustomModelItem(@NotNull String key, @NotNull String name, @NotNull Material material, int model) {
        super(key, material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(model);

        itemMeta.setDisplayName(Util.getColString(name));
        itemStack.setItemMeta(itemMeta);
    }

}
