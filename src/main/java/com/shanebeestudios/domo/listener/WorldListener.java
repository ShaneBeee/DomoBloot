package com.shanebeestudios.domo.listener;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.shanebeestudios.domo.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.event.world.TimeSkipEvent.SkipReason;

public class WorldListener implements Listener {

    // Prevent the night from skipping when players sleep
    @EventHandler
    private void onNightSkip(TimeSkipEvent event) {
        if (event.getSkipReason() == SkipReason.NIGHT_SKIP) {
            event.setCancelled(true);
        }
    }

    // Change name in ping event
    @SuppressWarnings("deprecation")
    @EventHandler
    private void onPing(PaperServerListPingEvent event) {
        event.setVersion("DomoBloot " + Bukkit.getMinecraftVersion());
        event.setMotd(Util.getColString(Util.PREFIX));
    }

}
