package com.shanebeestudios.domo.item;

import com.shanebeestudios.domo.DomoBloot;
import com.shanebeestudios.domo.data.PlayerData;
import com.shanebeestudios.domo.util.Util;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EnergyDrink extends Consumable {

    private final double energy;
    private final double fatigue;

    @SuppressWarnings("deprecation") // Paper deprecation
    EnergyDrink(@NotNull String key, @NotNull String name, double energy, double fatigue, Color color) {
        super(key, Material.POTION);
        this.energy = energy;
        this.fatigue = fatigue;

        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setDisplayName(Util.getColString(name));
        List<String> lore = new ArrayList<>();
        lore.add(Util.getColString("<#9EACCF>&oDrinking a revitalizing drink will increase energy,"));
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
