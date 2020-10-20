package com.shanebeestudios.domo.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ConstantConditions")
public class PlayerUtils {

    public static void setMaxHealth(@NotNull Player player, double max) {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(max);
    }

    public static double getMaxHealth(@NotNull Player player) {
        return player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
    }

    public static String getEnergyString(double energy) {
        StringBuilder stringBuilder = new StringBuilder();
        int e = (int) Math.ceil(energy);

        ChatColor color = ChatColor.RED;
        if (e > 12) {
            color = ChatColor.GREEN;
        } else if (e > 7) {
            color = ChatColor.GOLD;
        }
        stringBuilder.append(color);

        for (int i = 0; i < e; i++) {
            stringBuilder.append("|");
        }

        stringBuilder.append(ChatColor.GRAY);
        for (int i = e; i < 20; i++) {
            stringBuilder.append("|");
        }
        return stringBuilder.toString();
    }

    private static final String[] fatColors = new String[]{"5EF52E", "5EF52E", "92F52E", "CEF52E", "F5EF2E", "F5CB2E",
            "F5B02E", "F58E2E", "F5792E", "F5642E", "F5372E"};

    public static String getFatigueString(double fatigue) {
        StringBuilder stringBuilder = new StringBuilder();
        int fat = (int) Math.floor(fatigue * 2);

        if (fat > 0) {
            String color = Util.getColString("<#" + fatColors[(int) fatigue] + ">");
            stringBuilder.append(color);

            for (int i = 0; i < fat; i++) {
                stringBuilder.append("|");
            }
        }

        stringBuilder.append(ChatColor.GRAY);
        for (int i = fat; i < 20; i++) {
            stringBuilder.append("|");
        }
        return stringBuilder.toString();
    }

    public static String getOxygenString(double air) {
        StringBuilder stringBuilder = new StringBuilder();
        int round = (int) Math.floor(air);

        if (round > 0) {
            int c = round / 2;
            String color = Util.getColString("<#" + fatColors[10 - c] + ">");
            stringBuilder.append(color);

            for (int i = 0; i < round; i++) {
                stringBuilder.append("|");
            }
        }

        stringBuilder.append(ChatColor.GRAY);
        for (int i = round; i < 20; i++) {
            stringBuilder.append("|");
        }
        return stringBuilder.toString();
    }

    public static int getOxygenHelmetLevel(Player player) {
        int waterBreathing = hasWaterBreathing(player);
        if (waterBreathing > 0) {
            return waterBreathing;
        }
        ItemStack helmet = player.getInventory().getHelmet();
        if (helmet == null) return 0;
        if (helmet.getType() == Material.TURTLE_HELMET) {
            return 1;
        }
        // TODO custom cave helmet
        return 0;
    }

    public static int hasWaterBreathing(Player player) {
        for (PotionEffect activePotionEffect : player.getActivePotionEffects()) {
            if (activePotionEffect.getType().equals(PotionEffectType.WATER_BREATHING)) {
                return activePotionEffect.getAmplifier() + 1;
            }
        }
        return 0;
    }

    public static boolean isDay(Player player) {
        long time = player.getWorld().getTime();
        return time < 12542 || time > 23460;
    }

}
