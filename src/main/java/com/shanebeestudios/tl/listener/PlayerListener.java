package com.shanebeestudios.tl.listener;

import com.shanebeestudios.tl.TenLives;
import com.shanebeestudios.tl.data.PlayerData;
import com.shanebeestudios.tl.item.Items;
import com.shanebeestudios.tl.manager.PlayerManager;
import com.shanebeestudios.tl.util.PlayerUtils;
import com.shanebeestudios.tl.util.Util;
import org.bukkit.Statistic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

@SuppressWarnings("FieldCanBeLocal")
public class PlayerListener implements Listener {

    private final TenLives plugin;
    private final PlayerManager playerManager;
    private final Random random = new Random();
    private final int phantomChance = 10;
    private final double playerRespawnHealth = 30.0;

    public PlayerListener(TenLives plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
    }

    // When a player takes damage, set their max health to their current health
    @EventHandler
    private void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = ((Player) entity);
            double health = PlayerUtils.getMaxHealth(player);
            double finalDamage = event.getFinalDamage();
            double newHealth = health - finalDamage;
            if (newHealth > 0) {
                PlayerUtils.setMaxHealth(player, newHealth);
            }

            PlayerData playerData = playerManager.getPlayerData(player);
            playerData.increaseEnergy(-(finalDamage / 3));

        }
    }

    @EventHandler
    private void onSaturationReached(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = ((Player) event.getEntity());

            // If the food level is rising, we get out of here
            if (event.getFoodLevel() > player.getFoodLevel()) return;

            PlayerData playerData = playerManager.getPlayerData(player);
            playerData.increaseEnergy(-0.5);
        }
    }

    //// Decrease energy when player does exhaustive tasks
    //	@EventHandler
    //    private void onSaturationReached(FoodLevelChangeEvent event) {
    //        double modifier = config.MECHANICS_ENERGY_EXHAUSTION;
    //        if (modifier <= 0) return;
    //	    if (!(event.getEntity() instanceof Player)) return;
    //	    Player player = (Player) event.getEntity();
    //	    if (event.getFoodLevel() > player.getFoodLevel()) return;
    //        GameMode mode = player.getGameMode();
    //	    if (mode == GameMode.SURVIVAL || mode == GameMode.ADVENTURE) {
    //            PlayerData playerData = playerManager.getPlayerData(player);
    //            EnergyLevelChangeEvent energyEvent = new EnergyLevelChangeEvent(player, -modifier, playerData.getEnergy() - modifier);
    //            Bukkit.getPluginManager().callEvent(energyEvent);
    //            if (energyEvent.isCancelled()) return;
    //            playerData.increaseEnergy(-modifier);
    //        }
    //    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // When joining, set the player's max health
        if (!player.hasPlayedBefore()) {
            PlayerUtils.setMaxHealth(player, 30);
            player.setHealth(30);
        }
        // Unlock all custom recipes when player joins
        Items.unlockAllCustomRecipes(player);
        // Load PlayerData for player
        plugin.getPlayerManager().getPlayerData(player);
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Save and unload PlayerData for player
        plugin.getPlayerManager().unloadPlayerData(player);
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent event) {
        String deathMessage = event.getDeathMessage();
        event.setDeathMessage(null);

        BukkitRunnable runnable = new BukkitRunnable() {
            String message = deathMessage;

            @Override
            public void run() {
                // When a player dies reset their max health
                Player player = event.getEntity();
                PlayerUtils.setMaxHealth(player, playerRespawnHealth);
                player.setHealth(playerRespawnHealth);

                PlayerData playerData = playerManager.getPlayerData(player);
                playerData.setEnergy(20.0);

                // Play a sound to all players
                Util.deathSound();

                // Make the death message a little fancier
                message = "&d" + message.replace(player.getDisplayName(), "&b" + player.getDisplayName() + "&d");
                Util.broadcast(message);
                assert deathMessage != null;
                Util.sendTitle(player, "&cGAME OVER", "&e" + deathMessage.replace(player.getName(), "You"));
            }
        };
        runnable.runTaskLater(plugin, 2);
    }

    // Chance of phantomChance% a player will end up with phantoms spawning
    @EventHandler
    private void onLeaveBed(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        int ticksSinceSleep = player.getStatistic(Statistic.TIME_SINCE_REST);

        if (ticksSinceSleep < 1000000 || ticksSinceSleep > 1010000) {
            int chance = random.nextInt(100);
            if (chance < phantomChance) {
                player.setStatistic(Statistic.TIME_SINCE_REST, 1000000);
            }
        } else {
            player.setStatistic(Statistic.TIME_SINCE_REST, 1000000);
        }
    }

    // Format chat
    @EventHandler
    private void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String msg = event.getMessage();
        event.setFormat(Util.getColString("&7[&a" + player.getName() + "&7] &c» &r" + msg));
    }

}
