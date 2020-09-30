package com.shanebeestudios.domo.util;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Util {

    public static final String PREFIX = "&7[&3Ten&bLives&7] ";
    public static final World WORLD = Bukkit.getWorlds().get(0);
    private static final Pattern HEX_PATTERN = Pattern.compile("<#([A-Fa-f0-9]){6}>");

    public static String getColString(String string) {
        Matcher matcher = HEX_PATTERN.matcher(string);
        while (matcher.find()) {
            final net.md_5.bungee.api.ChatColor hexColor = net.md_5.bungee.api.ChatColor.of(matcher.group().substring(1, matcher.group().length() - 1));
            final String before = string.substring(0, matcher.start());
            final String after = string.substring(matcher.end());
            string = before + hexColor + after;
            matcher = HEX_PATTERN.matcher(string);
        }
        return ChatColor.translateAlternateColorCodes('&', string);
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
     * Setup the game rules for all worlds
     */
    public static void setGameRules() {
        for (World world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            world.setGameRule(GameRule.REDUCED_DEBUG_INFO, true);
            world.setDifficulty(Difficulty.HARD);
        }
    }

}
