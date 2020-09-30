package com.shanebeestudios.domo.util;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
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

    private static final String[] fatColors = new String[]{"5EF52E", "92F52E", "CEF52E", "F5EF2E", "F5CB2E",
            "F5B02E", "F58E2E", "F5792E", "F5642E", "F5372E", "F5372E"};

    public static String getFatigueString(double fatigue) {
        StringBuilder stringBuilder = new StringBuilder();
        int fat = (int) Math.floor(fatigue * 2);

        if (fat > 0) {
            int c = (int) fatigue;
            String color = Util.getColString("<#" + fatColors[c] + ">");
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

}
