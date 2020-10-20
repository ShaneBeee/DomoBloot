package com.shanebeestudios.domo.entity;

import com.shanebeestudios.domo.util.Pair;
import com.shanebeestudios.domo.util.Range;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@SuppressWarnings({"unused", "SameParameterValue"})
public class EntityDefault {

    private static final Map<String, EntityDefault> ENTITY_DEFAULT_MAP = new HashMap<>();

    // MONSTERS
    public static final EntityDefault ZOMBIE = register("zombie", EntityType.ZOMBIE)
            .requiresAdult()
            .knockback(1.0f)
            .knockbackResistance(0.5f)
            .walkSpeed(0.24f, 0.35f) // default = 0.23000000417232513
            .maxHealth(30.0f) // default = 20
            .alternate(5, EntityType.PILLAGER, EntityType.ZOMBIE_VILLAGER);

    public static final EntityDefault ZOMBIE_BABY = register("zombie_baby", EntityType.ZOMBIE)
            .requiresBaby()
            .knockback(0.5f)
            .knockbackResistance(0.25f);

    public static final EntityDefault PILLAGER = register("pillager", EntityType.PILLAGER)
            .knockback(1.0f)
            .knockbackResistance(0.5f)
            .walkSpeed(0.39f);

    public static final EntityDefault CREEPER = register("creeper", EntityType.CREEPER)
            .alternate(5, EntityType.ILLUSIONER);

    public static final EntityDefault SKELETON = register("skeleton", EntityType.SKELETON)
            .knockbackResistance(0.5f)
            .maxHealth(30.0f) // default = 20
            .alternate(3, CustomEntities.SKELETON_HORSE_TRAP);

    // PASSIVE
    public static final EntityDefault BAT = register("bat", EntityType.BAT)
            .addDrop(Material.GOLD_NUGGET, 5)
            .alternate(5, EntityType.VEX, EntityType.SILVERFISH);

    public static final EntityDefault RABBIT = register("rabbit", EntityType.RABBIT)
            .alternate(3, CustomEntities.KILLER_BUNNY);

    public static final EntityDefault HORSE = register("horse", EntityType.HORSE)
            .alternate(5, CustomEntities.PILLAGER_HORSEMEN);

    private static EntityDefault register(String key, EntityType entityType) {
        EntityDefault entityDefault = new EntityDefault(entityType);
        ENTITY_DEFAULT_MAP.put(key, entityDefault);
        return entityDefault;
    }

    @NotNull
    public static Collection<EntityDefault> getByType(@NotNull EntityType entityType) {
        Collection<EntityDefault> collection = new ArrayList<>();

        for (EntityDefault value : ENTITY_DEFAULT_MAP.values()) {
            if (value.entityType == entityType) {
                collection.add(value);
            }
        }
        return collection;
    }

    public static void init(){}
    private static final Random RANDOM = new Random();

    private final EntityType entityType;
    private float walkSpeed = -1f;
    private Range walkRange = null;
    private boolean requiresAdult = false;
    private boolean requiresBaby = false;
    private float flySpeed = -1f;
    private float attackSpeed = -1f;
    private float knockback = -1f;
    private float knockbackResistance = -1f;
    private float maxHealth = -1f;

    private final List<Pair<ItemStack, Integer>> drops = new ArrayList<>();
    private boolean cancelVanillaDrops = false;
    private EntityType[] alternates = null;
    private CustomEntity[] customAlternates = null;
    private int altChance;

    private EntityDefault(EntityType entityType) {
        this.entityType = entityType;
    }

    private EntityDefault walkSpeed(float value) {
        this.walkSpeed = value;
        return this;
    }

    private EntityDefault walkSpeed(float min, float max) {
        this.walkRange = new Range(min, max);
        return this;
    }

    public EntityDefault requiresAdult() {
        this.requiresAdult = true;
        this.requiresBaby = false;
        return this;
    }

    public EntityDefault requiresBaby() {
        this.requiresBaby = true;
        this.requiresAdult = false;
        return this;
    }

    private EntityDefault flySpeed(float value) {
        this.flySpeed = value;
        return this;
    }

    private EntityDefault attackSpeed(float value) {
        this.attackSpeed = value;
        return this;
    }

    private EntityDefault knockback(float value) {
        this.knockback = value;
        return this;
    }

    private EntityDefault knockbackResistance(float value) {
        this.knockbackResistance = value;
        return this;
    }

    private EntityDefault maxHealth(float max) {
        this.maxHealth = max;
        return this;
    }

    private EntityDefault addDrop(Material material, int chance) {
        return addDrop(material, 1, chance);
    }

    private EntityDefault addDrop(Material material, int amount, int chance) {
        this.drops.add(new Pair<>(new ItemStack(material, amount), chance));
        return this;
    }

    private EntityDefault cancelVanillaDrops() {
        this.cancelVanillaDrops = true;
        return this;
    }

    private EntityDefault alternate(int chance, EntityType... alt) {
        this.altChance = chance;
        this.alternates = alt;
        return this;
    }

    private EntityDefault alternate(int chance, CustomEntity... alt) {
        this.altChance = chance;
        this.customAlternates = alt;
        return this;
    }

    public void drop(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        Location location = entity.getLocation();
        World world = entity.getWorld();

        if (this.cancelVanillaDrops) {
            event.getDrops().clear();
        }

        drops.forEach(drop -> {
            if (RANDOM.nextInt(100) <= drop.getSecond()) {
                ItemStack item = drop.getFirst().clone();
                world.dropItemNaturally(location, item);
            }
        });
    }

    public boolean spawnAlternate(CreatureSpawnEvent event) {
        Location location = event.getLocation();
        World world = event.getEntity().getWorld();

        if (alternates != null && RANDOM.nextInt(100) < altChance) {
            event.setCancelled(true);
            int r = RANDOM.nextInt(alternates.length);
            EntityType entityType = alternates[r];
            world.spawnEntity(location, entityType);
            return true;
        }
        if (customAlternates != null && RANDOM.nextInt(100) < altChance) {
            event.setCancelled(true);
            int r = RANDOM.nextInt(customAlternates.length);
            CustomEntity customEntity = customAlternates[r];
            customEntity.spawn(location);
            return true;
        }
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    public void set(LivingEntity entity) {
        if (entity.getType() != entityType) return;

        if (walkSpeed >= 0) {
            if (requiresAdult && entity instanceof Ageable && !((Ageable) entity).isAdult()) {
                return;
            }
            entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(walkSpeed);
        }
        if (walkRange != null) {
            if (requiresAdult && entity instanceof Ageable && !((Ageable) entity).isAdult()) {
                return;
            }
            entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(walkRange.get());
        }
        if (flySpeed >= 0) {
            entity.getAttribute(Attribute.GENERIC_FLYING_SPEED).setBaseValue(flySpeed);
        }
        if (attackSpeed >= 0) {
            entity.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(attackSpeed);
        }
        if (knockback >= 0) {
            entity.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(knockback);
        }
        if (knockbackResistance >= 0) {
            entity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(knockbackResistance);
        }
        if (maxHealth >= 0) {
            entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
            entity.setHealth(maxHealth);
        }
    }

}
