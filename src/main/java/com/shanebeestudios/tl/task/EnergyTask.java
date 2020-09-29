package com.shanebeestudios.tl.task;

import com.shanebeestudios.tl.TenLives;
import com.shanebeestudios.tl.data.PlayerData;
import com.shanebeestudios.tl.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class EnergyTask implements Runnable {

    private final PlayerManager playerManager;
    private final int id;

    public EnergyTask(TenLives plugin) {
        this.playerManager = plugin.getPlayerManager();
        this.id = Bukkit.getScheduler().runTaskTimer(plugin, this, 20, 20).getTaskId();
    }

    @Override
    public void run() {
        for (PlayerData playerData : playerManager.getPlayerDatas()) {
            Player player = playerData.getPlayer();
            if (player != null) {
                if (player.isSleeping()) {
                    playerData.increaseEnergy(0.067); // Should fill full in about 5 minutes
                } else {
                    playerData.increaseEnergy(-0.009); // Works out to 40 minutes to fully drain
                }
            }
        }
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(id);
    }

}
