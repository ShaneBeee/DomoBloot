package com.shanebeestudios.domo.listener;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.shanebeestudios.domo.util.Util;
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
    @EventHandler
    private void onPing(PaperServerListPingEvent event) {
        event.setVersion("BeeServer 1.15.2");
        event.setMotd(Util.getColString("&7[&b&lDomo&3&lBloot&7]"));
    }

}
