package com.shanebeestudios.domo.entity;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;

public abstract class CustomEntity {

    NPC npc;

    public CustomEntity(NPC npc) {
        this.npc = npc;
    }

    public void spawn(Location location) {
        npc.spawn(location);
    }

    public NPC getNpc() {
        return npc;
    }

}
