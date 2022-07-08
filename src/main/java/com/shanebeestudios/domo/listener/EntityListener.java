package com.shanebeestudios.domo.listener;

import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import com.shanebeestudios.domo.DomoBloot;
import com.shanebeestudios.domo.entity.EntityDefault;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkPopulateEvent;

import java.util.Random;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class EntityListener implements Listener {

    private final DomoBloot plugin;
    private final Random random = new Random();

    public EntityListener(DomoBloot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onSpawn(CreatureSpawnEvent event) {
        // If we are spawning an entity, we are going to ignore all of this
        switch (event.getSpawnReason()) {
            case CUSTOM:
            case BREEDING:
            case EGG:
                return;
        }

        LivingEntity entity = event.getEntity();
        Location location = event.getLocation();
        World world = entity.getWorld();

        EntityDefault.getByType(event.getEntityType()).forEach(def -> {
            if (!def.spawnAlternate(event)) {
                def.set(entity);
            }
        });
    }

    @EventHandler
    private void onDeath(EntityDeathEvent event) {
        EntityDefault.getByType(event.getEntityType()).forEach( def -> def.drop(event));
    }

    // CreatureSpawnEvent is no longer called for chunk gen
    // So we are going to manually call it
    @SuppressWarnings("deprecation")
    @EventHandler
    private void onEntityGenerate(ChunkPopulateEvent event) {
        Chunk chunk = event.getChunk();
        World world = event.getWorld();
        for (Entity entity : chunk.getEntities()) {
            if (entity instanceof LivingEntity livingEntity) {
                CreatureSpawnEvent creatureSpawnEvent = new CreatureSpawnEvent(livingEntity, SpawnReason.CHUNK_GEN);
                Bukkit.getPluginManager().callEvent(creatureSpawnEvent);
                if (creatureSpawnEvent.isCancelled()) {
                    entity.remove();
                }
            }
        }
    }

    // Prevent arrows hitting skeletons from the neck down
    @EventHandler
    private void onArrowHit(ProjectileCollideEvent event) {
        switch (event.getCollidedWith().getType()) {
            case SKELETON, WITHER_SKELETON, STRAY -> {
                double projectile = event.getEntity().getLocation().getY();
                double entityEye = ((LivingEntity) event.getCollidedWith()).getEyeLocation().getY() - 0.10;
                if (projectile < entityEye) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
