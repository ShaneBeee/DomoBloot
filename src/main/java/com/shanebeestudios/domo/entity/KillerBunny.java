package com.shanebeestudios.domo.entity;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Rabbit.Type;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class KillerBunny extends CustomEntity {

    @Override
    public void spawn(Location location) {
        World world = location.getWorld();
        assert world != null;

        world.spawn(location, Rabbit.class, SpawnReason.CUSTOM, entity -> entity.setRabbitType(Type.THE_KILLER_BUNNY));
    }

}
