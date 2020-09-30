package com.shanebeestudios.domo.listener;

import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import com.shanebeestudios.domo.DomoBloot;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

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
        LivingEntity entity = event.getEntity();
        EntityType entityType = event.getEntityType();
        Location location = event.getLocation();

        switch (event.getEntityType()) {
            case ZOMBIE:
                // Random chance to spawn a pillager instead of a zombie
                if (location.getY() > 61 && random.nextInt(100) < 5) {
                    event.setCancelled(true);
                    location.getWorld().spawnEntity(location, EntityType.PILLAGER);
                    return;
                }
                // Make adult zombies a lil bit faster (random speeds)
                if (((Zombie) entity).isAdult()) {
                    AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
                    // Base = 0.23000000417232513
                    assert attribute != null;
                    attribute.setBaseValue((double) (random.nextInt(22) + 20) / 100);
                }
            break;

            // Randomly spawn illusioners once in a while
            case CREEPER:
                if (random.nextInt(100) < 5) {
                    event.setCancelled(true);
                    location.getWorld().spawnEntity(location, EntityType.ILLUSIONER);
                }
            break;
        }
    }

    // Prevent arrows hitting skeletons from the neck down
    @EventHandler
    private void onArrowHit(ProjectileCollideEvent event) {
        switch (event.getCollidedWith().getType()) {
            case SKELETON:
            case WITHER_SKELETON:
            case STRAY:
                double projectile = event.getEntity().getLocation().getY();
                double entityEye = ((LivingEntity) event.getCollidedWith()).getEyeLocation().getY() - 0.10;
                if (projectile < entityEye) {
                    event.setCancelled(true);
                }
        }
    }

}
