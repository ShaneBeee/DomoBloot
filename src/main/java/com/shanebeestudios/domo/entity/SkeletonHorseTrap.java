package com.shanebeestudios.domo.entity;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class SkeletonHorseTrap extends CustomEntity {

    @Override
    public void spawn(Location location) {
        World world = location.getWorld();
        assert world != null;

        world.spawn(location, SkeletonHorse.class, SpawnReason.CUSTOM, skeletonHorse -> skeletonHorse.setTrap(true));
    }

}
