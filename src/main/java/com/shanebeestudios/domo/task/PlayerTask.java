package com.shanebeestudios.domo.task;

import com.shanebeestudios.domo.DomoBloot;
import com.shanebeestudios.domo.data.PlayerData;
import com.shanebeestudios.domo.manager.PlayerManager;
import com.shanebeestudios.domo.util.BlockUtils;
import com.shanebeestudios.domo.util.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerTask implements Runnable {

    private final PlayerManager playerManager;
    private final int id;

    public PlayerTask(DomoBloot plugin) {
        this.playerManager = plugin.getPlayerManager();
        this.id = Bukkit.getScheduler().runTaskTimer(plugin, this, 20, 20).getTaskId();
    }

    @Override
    public void run() {
        for (PlayerData playerData : playerManager.getPlayerDatas()) {
            Player player = playerData.getPlayer();
            if (player.getGameMode() != GameMode.SURVIVAL) {
                playerData.getBoard().hide();
                continue;
            } else {
                playerData.getBoard().show();
            }
            if (player.isSleeping()) {
                // During the day, increase the player's energy - Should fill full in about 6 minutes
                // At night, increase at a faster rate - Should fill full in about 3 minutes
                playerData.increaseEnergy(playerData.isDay() ? 0.056 : 0.112);

                if (playerData.getEnergy() > 17.5) {
                    // If player's energy is high, and they're still in bed
                    // Decrease their fatigue level
                    // Decreases faster at night
                    playerData.increaseFatigue(playerData.isDay() ? -0.05 : -0.2);
                }
            } else {
                // Slowly drain the player's energy
                // -0.008333 = about 40 minutes to fully deplete energy
                // Higher fatigue levels will modify this value
                // Max modifier will take about 25 minutes
                playerData.increaseEnergy(-0.008333 * getEnergyModifier(playerData.getFatigue()));

                // If the player's energy is extremely low, raise their fatigue slowly
                if (playerData.getEnergy() < 3.5) {
                    // Would take about 5 minutes for 0->10 fatigue
                    playerData.increaseFatigue(0.0334);
                }
            }
            // Manage energy effects
            energyEffects(player, playerData.getEnergy());
            // Manage fatigue effects
            fatigueEffects(player, playerData.getFatigue());
            // Manage oxygen in cave
            manageOxygen(player, playerData);
        }
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(id);
    }

    private final double OXYGEN_AMOUNT = 0.0667;

    private void manageOxygen(Player player, PlayerData playerData) {
        int air = player.getRemainingAir();
        double oxygen = playerData.getOxygen();
        if (air < 300) {
            double a = (double) air / 15;
            if (a < oxygen) {
                playerData.setOxygen((double) air / 15);
            }
        } else if (isInCave(player)) {
            int airHelmet = PlayerUtils.getOxygenHelmetLevel(player);
            double change = airHelmet == 0 ? -OXYGEN_AMOUNT : -(OXYGEN_AMOUNT / (1 + airHelmet)); // TODO eval
            playerData.increaseOxygen(change);
        } else {
            playerData.increaseOxygen(2.0);
        }
        if (oxygen < 0.5 && player.getGameMode() == GameMode.SURVIVAL) {
            player.damage(1.0);
        }
    }

    private boolean isInCave(Player player) {
        Location location = player.getEyeLocation();
        Block block = location.getBlock();
        Material material = block.getType();
        if (material == Material.AIR) {
            return false;
        }
        if (material == Material.CAVE_AIR) {
            return true;
        }
        if (BlockUtils.isBlockInCave(block)) {
            return true;
        }
        return false;
    }

    private double getEnergyModifier(double fatigue) {
        double mod = ((fatigue - 3) * 0.085711) + 1;
        return fatigue > 3.0 ? mod : 1;
    }

    // Cached potion effects
    private final PotionEffect SLOW_DIG_1 = new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 0, false, false);
    private final PotionEffect SLOW_DIG_2 = new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 1, false, false);
    private final PotionEffect SLOW_DIG_3 = new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 2, false, false);
    private final PotionEffect BLIND_1 = new PotionEffect(PotionEffectType.BLINDNESS, 10, 0, false, false);
    private final PotionEffect BLIND_2 = new PotionEffect(PotionEffectType.BLINDNESS, 20, 0, false, false);
    private final PotionEffect BLIND_3 = new PotionEffect(PotionEffectType.BLINDNESS, 30, 0, false, false);
    private final PotionEffect BLIND_4 = new PotionEffect(PotionEffectType.BLINDNESS, 100, 0, false, false);

    private void energyEffects(Player player, double energy) {
        float walkSpeed = 0.2f; // default walk speed
        if (energy < 1.5) {
            walkSpeed = 0.05f;
            player.addPotionEffect(SLOW_DIG_3);
        } else if (energy < 3) {
            walkSpeed = 0.10f;
            player.addPotionEffect(SLOW_DIG_2);
        } else if (energy < 5) {
            walkSpeed = 0.14f;
            player.addPotionEffect(SLOW_DIG_1);
        } else if (energy < 8) {
            walkSpeed = 0.16f;
        } else {
            player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
        }
        player.setWalkSpeed(walkSpeed);
    }

    private void fatigueEffects(Player player, double fatigue) {
        if (player.isSleeping()) {
            player.addPotionEffect(BLIND_4);
        } else if (fatigue > 8.5) {
            player.addPotionEffect(BLIND_3);
        } else if (fatigue > 8) {
            player.addPotionEffect(BLIND_2);
        } else if (fatigue > 7) {
            player.addPotionEffect(BLIND_1);
        } else {
            player.removePotionEffect(PotionEffectType.BLINDNESS);
        }
    }

}
