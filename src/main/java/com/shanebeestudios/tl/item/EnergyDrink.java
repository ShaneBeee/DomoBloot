package com.shanebeestudios.tl.item;

import com.shanebeestudios.tl.DomoBloot;
import com.shanebeestudios.tl.data.PlayerData;
import com.shanebeestudios.tl.util.Util;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EnergyDrink extends Consumable {

    private final double energy;
    private final double fatigue;

    EnergyDrink(@NotNull String key, @NotNull String name, @NotNull Material material, double energy, double fatigue, Color color) {
        super(key, new ItemStack(material));
        this.energy = energy;
        this.fatigue = fatigue;

        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setDisplayName(Util.getColString(name));
        List<String> lore = new ArrayList<>();
        lore.add(Util.getColString("<#9EACCF>&oDrinking an energy drink will increase energy,"));
        lore.add(Util.getColString("<#9EACCF>&obut it will also increase your fatigue"));
        lore.add(" ");
        lore.add(Util.getColString("<#9ACD61>&lEnergy Change: &a+" + energy));
        lore.add(Util.getColString("<#52C6E2>&lFatigue Change: &c+" + fatigue));
        meta.setLore(lore);
        meta.setColor(color);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemStack.setItemMeta(meta);
    }

    @Override
    public void consume(Player player) {
        PlayerData playerData = DomoBloot.getPlugin().getPlayerManager().getPlayerData(player);
        playerData.increaseEnergy(energy);
        if (playerData.getFatigue() + fatigue > 10) {
            player.damage(1.0);
        }
        playerData.increaseFatigue(fatigue);
    }

}
