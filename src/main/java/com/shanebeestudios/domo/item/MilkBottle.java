package com.shanebeestudios.domo.item;

import com.shanebeestudios.domo.util.Util;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

public class MilkBottle extends Consumable {

    @SuppressWarnings("deprecation")
    MilkBottle(@NotNull String key) {
        super(key, Material.POTION);
        PotionMeta potionMeta = ((PotionMeta) itemStack.getItemMeta());
        potionMeta.setBasePotionData(new PotionData(PotionType.WATER));
        potionMeta.setColor(Color.WHITE);
        potionMeta.setDisplayName(Util.getColString("&rMilk Bottle"));
        potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemStack.setItemMeta(potionMeta);
    }

    @Override
    public void consume(Player player) {
    }

}
