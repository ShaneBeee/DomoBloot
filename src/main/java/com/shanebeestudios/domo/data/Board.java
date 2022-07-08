package com.shanebeestudios.domo.data;

import com.shanebeestudios.domo.util.Util;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a client side scoreboard for a player
 */
public class Board {

    private final FastBoard fastBoard;
    private String title = "";
    private final String[] lines;
    private boolean visible = true;

    public Board(Player player) {
        this.fastBoard = new FastBoard(player);
        this.lines = new String[15];
    }

    public void setTitle(String title) {
        this.title = title;
        if (!visible) return;
        this.fastBoard.updateTitle(title);
    }

    public void setLine(int line, String value) {
        if (line > 15 || line < 1) return;
        this.lines[15 - line] = Util.getColString(value);
        if (!visible) return;
        updateLines();
    }

    public void hide() {
        this.fastBoard.updateTitle("");
        this.fastBoard.updateLines();
        this.visible = false;
    }

    public void show() {
        this.fastBoard.updateTitle(this.title);
        this.visible = true;
        updateLines();
    }

    private void updateLines() {
        List<String> lines = new ArrayList<>();
        for (String line : this.lines) {
            if (line != null) {
                lines.add(line);
            }
        }
        this.fastBoard.updateLines(lines);
    }

}
