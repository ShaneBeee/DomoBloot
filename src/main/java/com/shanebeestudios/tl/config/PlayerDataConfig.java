package com.shanebeestudios.tl.config;

import com.shanebeestudios.tl.DomoBloot;
import com.shanebeestudios.tl.data.PlayerData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class PlayerDataConfig {

    private final DomoBloot plugin;

    public PlayerDataConfig(DomoBloot plugin) {
        this.plugin = plugin;
    }

    @Nullable
    public PlayerData loadDataForPlayer(Player player) {
        File file = new File(plugin.getDataFolder(), "playerdata/" + player.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (config.contains("data")) {
            return config.getObject("data", PlayerData.class);
        }
        return null;
    }

    public void saveData(PlayerData playerData) {
        File file = new File(plugin.getDataFolder(), "playerdata/" + playerData.getUuid().toString() + ".yml");
        FileConfiguration config = new YamlConfiguration();
        config.set("data", playerData);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
