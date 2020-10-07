package com.shanebeestudios.domo.data;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class BlockFaceBlock {

    private final Location location;
    private final BlockFace blockFace;

    public BlockFaceBlock(Location location, BlockFace blockFace) {
        this.location = location;
        this.blockFace = blockFace;
    }

    public Location getLocation() {
        return location;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

}
