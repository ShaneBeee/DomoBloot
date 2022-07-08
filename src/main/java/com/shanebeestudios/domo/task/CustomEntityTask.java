package com.shanebeestudios.domo.task;

import com.shanebeestudios.domo.DomoBloot;
import com.shanebeestudios.domo.entity.CustomEntities;
import com.shanebeestudios.domo.entity.CustomEntity;
import com.shanebeestudios.domo.entity.Trap;
import com.shanebeestudios.domo.util.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

public class CustomEntityTask implements Runnable {

    private final int id;

    public CustomEntityTask(DomoBloot plugin) {
        this.id = Bukkit.getScheduler().runTaskTimer(plugin, this, 20, 20).getTaskId();
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.getGameMode() != GameMode.SURVIVAL) return;

            player.getNearbyEntities(10, 10, 10).forEach(entity -> {
                if (entity instanceof Player) return;

                if (entity instanceof Wolf wolf) {
                    if (!wolf.isTamed() && !PlayerUtils.isDay(player)) {
                        wolf.setAngry(true);
                        wolf.setTarget(player);
                    }
                } else {
                    CustomEntity customEntity = CustomEntities.getCustom(entity);
                    if (customEntity instanceof Trap trap) {
                        trap.trap(entity, player);
                    }
                }
            });
        });
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(id);
    }

}
