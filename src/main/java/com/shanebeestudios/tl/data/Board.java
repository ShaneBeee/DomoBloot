package com.shanebeestudios.tl.data;

import com.shanebeestudios.tl.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a team based scoreboard for a player
 * <p>This class also has a map that holds all player scoreboards</p>
 */
public class Board {

    private static final boolean LEGACY = false;
    private static final int MAX = 128;

    private final Player player;
    private final Scoreboard oldScoreboard;
    private final Scoreboard scoreboard;
    private final Objective board;
    private final Team[] lines = new Team[15];
    private final String[] entries = new String[]{"&1&r", "&2&r", "&3&r", "&4&r", "&5&r", "&6&r", "&7&r", "&8&r", "&9&r", "&0&r", "&a&r", "&b&r", "&c&r", "&d&r", "&e&r"};
    private boolean on;

    public Board(Player player) {
        this.player = player;
        this.on = true;
        oldScoreboard = player.getScoreboard();
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.player.setScoreboard(scoreboard);
        if (LEGACY)
            //noinspection deprecation
            board = scoreboard.registerNewObjective("Board", "dummy");
        else
            board = scoreboard.registerNewObjective("Board", "dummy", "Board");
        board.setDisplaySlot(DisplaySlot.SIDEBAR);
        board.setDisplayName(" ");

        for (int i = 0; i < 15; i++) {
            lines[i] = scoreboard.registerNewTeam("line" + (i + 1));
        }

        for (int i = 0; i < 15; i++) {
            lines[i].addEntry(getColString(entries[i]));
        }
    }

    /**
     * Set the title of this scoreboard
     *
     * @param title Title to set
     */
    public void setTitle(String title) {
        board.setDisplayName(getColString(title));
    }

    /**
     * Set a specific line for this scoreboard
     * <p>Lines 1 - 15</p>
     *
     * @param line Line to set (1 - 15)
     * @param text Text to put in line
     */
    public void setLine(int line, String text) {
        Validate.isBetween(line, 1, 15);
        Team t = lines[line - 1];
        if (text.length() > (MAX / 2)) {
            String prefix = getColString(text.substring(0, (MAX / 2)));
            String lastColor = ChatColor.getLastColors(prefix);
            int splitMax = Math.min(text.length(), MAX - lastColor.length());
            String suffix = getColString(lastColor + text.substring((MAX / 2), splitMax));

            // Fix for split issues splitting between § and color code
            if (prefix.substring(((MAX / 2) - 1), MAX / 2).equalsIgnoreCase("§")) {
                prefix = prefix.substring(0, (MAX / 2) - 1);
                int length = text.length() > (MAX - 2) ? (MAX - 1) : text.length();
                suffix = getColString(text.substring((MAX / 2) - 1, length));
            }
            t.setPrefix(prefix);
            t.setSuffix(suffix);
        } else {
            String prefix = getColString(text);
            t.setPrefix(prefix);
            t.setSuffix("");
        }
        board.getScore(getColString(entries[line - 1])).setScore(line);
    }

    /**
     * Delete a line in this scoreboard
     * <p>Lines 1 - 15</p>
     *
     * @param line Line to delete (1 - 15)
     */
    public void deleteLine(int line) {
        Validate.isBetween(line, 1, 15);
        scoreboard.resetScores(getColString(entries[line - 1]));
    }

    /**
     * Clear all lines of this scoreboard
     */
    public void clearBoard() {
        for (int i = 1; i < 16; i++) {
            deleteLine(i);
        }
    }

    /**
     * Toggle this scoreboard on or off
     * <p>When off, will not be visible to player, but can still update</p>
     *
     * @param on Whether on or off
     */
    public void toggle(boolean on) {
        if (on) {
            player.setScoreboard(this.scoreboard);
            this.on = true;
        } else {
            player.setScoreboard(this.oldScoreboard);
            this.on = false;
        }
    }

    /**
     * Check if this scoreboard is on or off
     *
     * @return True if on else off
     */
    public boolean isOn() {
        return this.on;
    }

    private String getColString(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
