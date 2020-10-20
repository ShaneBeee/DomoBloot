package com.shanebeestudios.domo.entity;

import com.shanebeestudios.domo.DomoBloot;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

public abstract class CustomEntity {

    public static NamespacedKey KEY = new NamespacedKey(DomoBloot.getPlugin(), "custom_entity");

    public abstract void spawn(Location location);
}
