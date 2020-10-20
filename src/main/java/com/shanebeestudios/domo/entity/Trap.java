package com.shanebeestudios.domo.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class Trap extends CustomEntity {

    public abstract void trap(Entity trap, Player target);

}
