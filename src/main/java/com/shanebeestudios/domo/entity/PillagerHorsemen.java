package com.shanebeestudios.domo.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Player;
import org.bukkit.entity.ZombieHorse;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

@SuppressWarnings("CodeBlock2Expr")
public class PillagerHorsemen extends CustomEntity implements Trap {

    private final Random RANDOM = new Random();
    private final ItemStack CROSSBOW = new ItemStack(Material.CROSSBOW);

    @Override
    public void spawn(Location location) {
        World world = location.getWorld();
        assert world != null;

        world.spawn(location, ZombieHorse.class, zombieHorse -> {
            zombieHorse.getPersistentDataContainer().set(CustomEntity.KEY, PersistentDataType.STRING, "pillager_horsemen");
        });
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void trap(Entity trap, Player target) {
        if (!(trap instanceof ZombieHorse zombieHorse)) return;

        Location location = zombieHorse.getLocation().clone();
        World world = zombieHorse.getWorld();

        // After we initiate our trap, we make this entity no longer a trap
        zombieHorse.getPersistentDataContainer().remove(CustomEntity.KEY);

        world.strikeLightningEffect(location);

        zombieHorse.addPassenger(world.spawn(location, Pillager.class, pillager -> {
            pillager.getEquipment().setItemInMainHand(CROSSBOW.clone());
            pillager.setPatrolLeader(true);
        }));
        zombieHorse.setTamed(true);
        zombieHorse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);

        for (int i = 0; i < 3; i++) {
            Location loc = location.clone().add(RANDOM.nextInt(3) + 1, 0, RANDOM.nextInt(3) + 1);
            loc = world.getHighestBlockAt(loc).getLocation().add(0, 1, 0);
            world.spawn(loc, ZombieHorse.class, horse -> {
                horse.addPassenger(world.spawn(location, Pillager.class, pillager -> {
                    pillager.getEquipment().setItemInMainHand(CROSSBOW.clone());
                    pillager.setPatrolLeader(false);
                }));
                horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);
                horse.setTamed(true);
            });
        }
    }

}
