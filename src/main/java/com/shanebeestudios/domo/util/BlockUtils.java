package com.shanebeestudios.domo.util;

import com.google.common.collect.ImmutableSet;
import com.shanebeestudios.domo.DomoBloot;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class BlockUtils {

    private static final NamespacedKey HOLDERS;
    public static final ImmutableSet<Material> CONTAINERS;
    public static final ImmutableSet<Material> SPAWN_AT;
    public static final ImmutableSet<Material> SPAWN_ON;

    //<editor-fold desc="fold">
    static {
        CONTAINERS = ImmutableSet.<Material>builder()
                .add(Material.CHEST)
                .add(Material.TRAPPED_CHEST)
                .add(Material.BARREL)
                .add(Material.FURNACE)
                .add(Material.SMOKER)
                .add(Material.BLAST_FURNACE)
                .add(Material.DISPENSER)
                .add(Material.DROPPER)
                .add(Material.HOPPER)
                .build();
        HOLDERS = new NamespacedKey(DomoBloot.getPlugin(), "holders");
        SPAWN_AT = ImmutableSet.<Material>builder()
                .add(Material.AIR)
                .add(Material.GRASS)
                .add(Material.TALL_GRASS)
                .add(Material.FERN)
                .add(Material.LARGE_FERN)
                .build();
        SPAWN_ON = ImmutableSet.<Material>builder()
                .add(Material.DIRT)
                .add(Material.GRASS_BLOCK)
                .add(Material.PODZOL)
                .add(Material.MYCELIUM)
                .add(Material.STONE)
                .add(Material.GRAVEL)
                .add(Material.SNOW)
                .add(Material.SAND)
                .add(Material.RED_SAND)
                .add(Material.SANDSTONE)
                .add(Material.RED_SANDSTONE)
                .add(Material.TERRACOTTA)
                .add(Material.BLACK_TERRACOTTA)
                .add(Material.BLUE_TERRACOTTA)
                .add(Material.BROWN_TERRACOTTA)
                .add(Material.CYAN_TERRACOTTA)
                .add(Material.GRAY_TERRACOTTA)
                .add(Material.GREEN_TERRACOTTA)
                .add(Material.LIGHT_BLUE_TERRACOTTA)
                .add(Material.LIGHT_GRAY_TERRACOTTA)
                .add(Material.LIME_TERRACOTTA)
                .add(Material.MAGENTA_TERRACOTTA)
                .add(Material.ORANGE_TERRACOTTA)
                .add(Material.PINK_TERRACOTTA)
                .add(Material.PURPLE_TERRACOTTA)
                .add(Material.RED_TERRACOTTA)
                .add(Material.WHITE_TERRACOTTA)
                .add(Material.YELLOW_TERRACOTTA)
                .build();
    }
    //</editor-fold>

    public static boolean isContainer(Material material) {
        if (Tag.SHULKER_BOXES.isTagged(material)) {
            return true;
        }
        return CONTAINERS.contains(material);
    }

    public static boolean isSpawnableAt(Material material) {
        return SPAWN_AT.contains(material) || Tag.FLOWERS.isTagged(material);
    }

    public static boolean canSpawnOn(Material material) {
        return SPAWN_ON.contains(material);
    }

    public static boolean hasHolders(BlockState state) {
        return state instanceof PersistentDataHolder &&
                ((PersistentDataHolder) state).getPersistentDataContainer().has(HOLDERS, PersistentDataType.STRING);
    }

    public static boolean hasHolder(UUID uuid, BlockState state) {
        if (hasHolders(state)) {
            return getHolderList(state).contains(uuid.toString());
        }
        return false;
    }

    public static void addHolder(UUID uuid, BlockState state) {
        //if (hasHolders(state)) {
        List<String> holders = new ArrayList<>(getHolderList(state));
        if (!holders.contains(uuid.toString())) {
            holders.add(uuid.toString());
            ((PersistentDataHolder) state).getPersistentDataContainer().set(HOLDERS, PersistentDataType.STRING, listToString(holders));
            state.update(true);
        }
        //}
    }

    public static void removeHolder(Player player, BlockState state) {
        String uuid = player.getUniqueId().toString();
        if (hasHolders(state)) {
            List<String> holders = Arrays.asList(((PersistentDataHolder) state).getPersistentDataContainer().get(HOLDERS,
                    PersistentDataType.STRING).split(";"));
            holders.removeIf(holder -> holder.equalsIgnoreCase(uuid));

            StringBuilder newHolders = new StringBuilder();
            for (String holder : holders) {
                newHolders.append(holder).append(";");
            }
            newHolders.deleteCharAt(newHolders.lastIndexOf(";"));
            String holds = newHolders.toString();
            ((PersistentDataHolder) state).getPersistentDataContainer().set(HOLDERS, PersistentDataType.STRING, holds);
            state.update(true);
        }
    }

    public static void removeHolders(BlockState state) {
        if (hasHolders(state)) {
            ((PersistentDataHolder) state).getPersistentDataContainer().remove(HOLDERS);
            state.update(true);
        }
    }

    public static List<String> getHolderList(BlockState state) {
        if (hasHolders(state)) {
            return Arrays.asList(((PersistentDataHolder) state).getPersistentDataContainer().get(HOLDERS,
                    PersistentDataType.STRING).split(";"));
        }
        return new ArrayList<>();
    }

    private static String listToString(List<String> list) {
        StringBuilder newHolders = new StringBuilder();
        for (String holder : list) {
            newHolders.append(holder).append(";");
        }
        newHolders.deleteCharAt(newHolders.lastIndexOf(";"));
        return newHolders.toString();
    }
}
