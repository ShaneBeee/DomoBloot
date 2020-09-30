package com.shanebeestudios.domo.task;

import com.shanebeestudios.domo.DomoBloot;
import com.shanebeestudios.domo.data.PlayerData;
import com.shanebeestudios.domo.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class EnergyTask implements Runnable {

    private final PlayerManager playerManager;
    private final int id;

    public EnergyTask(DomoBloot plugin) {
        this.playerManager = plugin.getPlayerManager();
        this.id = Bukkit.getScheduler().runTaskTimer(plugin, this, 40, 20).getTaskId();
    }

    @Override
    public void run() {
        for (PlayerData playerData : playerManager.getPlayerDatas()) {
            Player player = playerData.getPlayer();
            if (player.isSleeping()) {
                if (playerData.isDay()) {
                    // During the day, increase the player's energy
                    playerData.increaseEnergy(0.056); // Should fill full in about 6 minutes
                } else {
                    // At night, increase the player's energy at a faster rate
                    playerData.increaseEnergy(0.112); // Should fill full in about 3 minutes
                }
                if (playerData.getEnergy() > 17.5) {
                    // If player's energy is high, and they're still in bed
                    // Decrease their fatigue level
                    // Decreases faster at night
                    playerData.increaseFatigue(playerData.isDay() ? -0.05 : -0.2);
                }
            } else {
                // Slowly drain the players energy
                // -0.008333 = about 40 minutes to fully deplete energy
                // Higher fatigue levels will modify this value
                // Max modifier will take about 25 minutes
                playerData.increaseEnergy(-0.008333 * getEnergyModifier(playerData.getFatigue()));
            }
        }
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(id);
    }

    private double getEnergyModifier(double fatigue) {
        double mod = ((fatigue - 3) * 0.085711) + 1;
        return fatigue > 3.0 ? mod : 1;
    }

}
