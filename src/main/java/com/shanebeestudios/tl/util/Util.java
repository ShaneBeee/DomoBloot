package com.shanebeestudios.tl.util;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Util {

    public final static String PREFIX = "&7[&3Ten&bLives&7] ";
    public final static World WORLD = Bukkit.getWorlds().get(0);

    public static String getColString(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String getHexString(String hex, String string) {
        String start = hex.startsWith("#") ? "" : "#";
        String color = net.md_5.bungee.api.ChatColor.of(start + hex).toString();
        return color + string;
    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(getColString(PREFIX + message));
    }

    public static void warn(String warning) {
        log("&e" + warning);
    }

    public static void error(String error) {
        log("&c" + error);
    }

    public static void sendColMsg(CommandSender receiver, String message, boolean prefix) {
        receiver.sendMessage(getColString((prefix ? PREFIX : "") + message));
    }

    public static String caps(String string) {
        return WordUtils.capitalize(string.toLowerCase().replace("_", " "));
    }

    public static void broadcast(String message) {
        Bukkit.broadcastMessage(getColString(PREFIX + message));
    }

    public static void sendTitle(Player player, String title, String subtitle, int in, int stay, int out) {
        String t = getColString(title);
        String st = getColString(subtitle);
        player.sendTitle(t, st, in, stay, out);
    }

    public static void sendTitle(Player player, String title, String subtitle) {
        sendTitle(player, title, subtitle, 10, 100, 10);
    }

    public static void deathSound() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 5, 1);
        }
    }

    public static void resetWorldTime() {
        WORLD.setTime(0);
        WORLD.setStorm(false);
        WORLD.setThundering(false);
    }

    /**
     * Setup the immediate respawn gamerule for all worlds
     */
    public static void setGameRules() {
        for (World world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            world.setGameRule(GameRule.REDUCED_DEBUG_INFO, true);
            world.setDifficulty(Difficulty.HARD);
        }
    }

}
