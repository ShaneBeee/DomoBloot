package com.shanebeestudios.domo.entity;

import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class TomBloot extends CustomEntity {

    public TomBloot() {
        super(CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "TomNook"));
    }

    @Override
    public void spawn(Location location) {
        super.spawn(location);
        npc.setName("TomBloot");
    }

}
