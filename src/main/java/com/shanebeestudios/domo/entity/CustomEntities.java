package com.shanebeestudios.domo.entity;

import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class CustomEntities {

    private static final Map<String, CustomEntity> CUSTOM_ENTITY_MAP = new HashMap<>();

    public static CustomEntity SKELETON_HORSE_TRAP = register("skeleton_horse_trap", new SkeletonHorseTrap());
    public static CustomEntity KILLER_BUNNY = register("killer_bunny", new KillerBunny());
    public static CustomEntity PILLAGER_HORSEMEN = register("pillager_horsemen", new PillagerHorsemen());

    private static CustomEntity register(String key, CustomEntity customEntity) {
        CUSTOM_ENTITY_MAP.put(key, customEntity);
        return customEntity;
    }

    public static CustomEntity getByKey(String key) {
        if (CUSTOM_ENTITY_MAP.containsKey(key)) {
            return CUSTOM_ENTITY_MAP.get(key);
        }
        return null;
    }

    public static CustomEntity getCustom(Entity entity) {
        PersistentDataContainer container = entity.getPersistentDataContainer();
        if (container.has(CustomEntity.KEY, PersistentDataType.STRING)) {
            String key = container.get(CustomEntity.KEY, PersistentDataType.STRING);
            return getByKey(key);
        }
        return null;
    }

}
