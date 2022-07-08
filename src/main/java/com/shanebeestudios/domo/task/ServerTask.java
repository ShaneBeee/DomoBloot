package com.shanebeestudios.domo.task;

import com.shanebeestudios.domo.DomoBloot;
import com.shanebeestudios.domo.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings("StringBufferReplaceableByString")
public class ServerTask implements Runnable {

    private final String NEW_LINE = System.lineSeparator();
    private final int id;

    public ServerTask(DomoBloot plugin) {
        this.id = Bukkit.getScheduler().runTaskTimer(plugin, this, 20, 20).getTaskId();
    }


    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setPlayerListHeaderFooter(listHeader(player), listFooter(player));
        }
    }

    @SuppressWarnings("deprecation")
    private String listHeader(Player player) {
        StringBuilder builder = new StringBuilder();
        builder.append(Util.getColString(Util.PREFIX)).append(NEW_LINE);
        builder.append(Util.getColString("&aWelcome " + player.getDisplayName())).append(NEW_LINE);
        return builder.toString();
    }

    private String listFooter(Player player) {
        StringBuilder builder = new StringBuilder();
        builder.append(" ").append(NEW_LINE);
        String world = Util.caps(player.getWorld().getName().replace("_", " "));
        builder.append(Util.getColString("&bWorld: &7" + world)).append(NEW_LINE);
        return builder.toString();
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(id);
    }

}
