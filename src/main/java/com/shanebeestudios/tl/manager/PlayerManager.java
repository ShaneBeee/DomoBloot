package com.shanebeestudios.tl.manager;

import com.shanebeestudios.tl.DomoBloot;
import com.shanebeestudios.tl.data.PlayerData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class PlayerManager {

    private final DomoBloot plugin;
    private final Map<UUID, PlayerData> PLAYER_DATA_MAP = new HashMap<>();

    public PlayerManager(DomoBloot plugin) {
        this.plugin = plugin;
    }

    public void addPlayerData(PlayerData playerData) {
        PLAYER_DATA_MAP.put(playerData.getUuid(), playerData);
    }

    @NotNull
    public PlayerData getPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        if (PLAYER_DATA_MAP.containsKey(uuid)) {
            return PLAYER_DATA_MAP.get(uuid);
        }
        PlayerData playerData = plugin.getPlayerDataConfig().loadDataForPlayer(player);
        if (playerData == null) {
            playerData = new PlayerData(player);
        }
        PLAYER_DATA_MAP.put(uuid, playerData);
        return playerData;
    }

    public void unloadPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        if (PLAYER_DATA_MAP.containsKey(uuid)) {
            plugin.getPlayerDataConfig().saveData(PLAYER_DATA_MAP.get(uuid));
            PLAYER_DATA_MAP.remove(uuid);
        }
    }

    public Collection<PlayerData> getPlayerDatas() {
        return PLAYER_DATA_MAP.values();
    }

}
