package com.shanebeestudios.domo.listener;

import com.shanebeestudios.domo.DomoBloot;
import com.shanebeestudios.domo.data.BlockFaceBlock;
import com.shanebeestudios.domo.data.PlayerData;
import com.shanebeestudios.domo.item.Consumable;
import com.shanebeestudios.domo.item.Item;
import com.shanebeestudios.domo.item.Items;
import com.shanebeestudios.domo.manager.PlayerManager;
import com.shanebeestudios.domo.manager.RecipeManager;
import com.shanebeestudios.domo.util.PlayerUtils;
import com.shanebeestudios.domo.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SuppressWarnings("FieldCanBeLocal")
public class PlayerListener implements Listener {

    private final DomoBloot plugin;
    private final PlayerManager playerManager;
    private final RecipeManager recipeManager;
    private final Random random = new Random();
    private final int PHANTOM_CHANCE = 10;
    private final double RESPAWN_HEALTH = 30.0;

    public PlayerListener(DomoBloot plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
        this.recipeManager = plugin.getRecipeManager();
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

    // When a player consumes an Item, use it
    @EventHandler
    private void onDrink(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();
        if (Items.isItem(itemStack)) {
            Item item = Items.getItemFromItemStack(itemStack);
            if (item instanceof Consumable consumable) {
                consumable.consume(player);
            }
        }
    }

    @EventHandler
    private void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (event.getHand() == EquipmentSlot.HAND) {
            if (entity instanceof Cow) { // Player clicks a cow with a glass bottle to get a bottle of milk
                ItemStack hand = player.getInventory().getItemInMainHand();
                if (hand.getType() == Material.GLASS_BOTTLE) {
                    event.setCancelled(true);
                    hand.setAmount(hand.getAmount() - 1);
                    player.getWorld().dropItem(player.getLocation(), Items.MILK_BOTTLE.getItemStack());
                }
            }
        }
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Update join message
        String message = event.getJoinMessage();
        if (message != null) {
            message = message.replace(player.getName(), "<#D059D2>" + player.getName() + "<#05CB74>");
            event.setJoinMessage(null);

            // Send HEX colored to players
            Util.broadcastToPlayers(message);
            // Send legacy colors to console
            Util.log("&b" + player.getName() + "&a joined the game");
        }

        // When joining, set the player's max health
        if (!player.hasPlayedBefore()) {
            PlayerUtils.setMaxHealth(player, 30);
        }
        player.setHealth(PlayerUtils.getMaxHealth(player));
        // Unlock all custom recipes when player joins
        recipeManager.unlockAllCustomRecipes(player);
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
        event.getDrops().clear();

        BukkitRunnable runnable = new BukkitRunnable() {
            String message = deathMessage;

            @Override
            public void run() {
                // When a player dies reset their max health
                Player player = event.getEntity();
                PlayerUtils.setMaxHealth(player, RESPAWN_HEALTH);
                player.setHealth(RESPAWN_HEALTH);

                PlayerData playerData = playerManager.getPlayerData(player);
                playerData.reset();

                // Play a sound to all players
                Util.deathSound();

                // Make the death message a little fancier
                // Send legacy colors to console
                Util.log("&c" + message);
                // Send HEX colored to players
                message = "&d" + message.replace(player.getDisplayName(), "<#12F3D4>" + player.getDisplayName() + "<#F75FF3>");
                Util.broadcastToPlayers(message);

                assert deathMessage != null;
                Util.sendTitle(player, "<#8A0303>&lGAME OVER", "<#E46E22>&l" + deathMessage.replace(player.getName(), "You"));
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
            if (chance < PHANTOM_CHANCE) {
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
        if (msg.startsWith("!")) return; // Skript effect commands

        event.setFormat(Util.getColString("&7[&a" + player.getName() + "&7] &c» &r" + msg));
    }

    private final Map<Player, BlockFaceBlock> BLOCK_FACE_MAP = new HashMap<>();

    // Cache the blockFace of the last touched block
    @EventHandler
    private void onTouch(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block != null) {
                Player player = event.getPlayer();
                BlockFace blockFace = event.getBlockFace();
                BLOCK_FACE_MAP.put(player, new BlockFaceBlock(block.getLocation(), blockFace));
            }
        }
    }

    // When a block is broken in a cave, make it cave-air not air
    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (BLOCK_FACE_MAP.containsKey(player)) {
            Block block = event.getBlock();
            BlockFaceBlock blockFaceBlock = BLOCK_FACE_MAP.get(player);
            if (blockFaceBlock.getLocation().equals(block.getLocation())) {
                BlockFace blockFace = blockFaceBlock.getBlockFace();
                Block relative = block.getRelative(blockFace);
                if (relative.getType() == Material.CAVE_AIR) {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> block.setType(Material.CAVE_AIR), 1);
                }
            }
        }
    }

}
