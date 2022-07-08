package com.shanebeestudios.domo;

import com.shanebeestudios.domo.command.GiveCmd;
import com.shanebeestudios.domo.command.TestCmd;
import com.shanebeestudios.domo.config.PlayerDataConfig;
import com.shanebeestudios.domo.data.PlayerData;
import com.shanebeestudios.domo.entity.EntityDefault;
import com.shanebeestudios.domo.listener.EntityListener;
import com.shanebeestudios.domo.listener.PlayerListener;
import com.shanebeestudios.domo.listener.WorldListener;
import com.shanebeestudios.domo.manager.PlayerManager;
import com.shanebeestudios.domo.manager.RecipeManager;
import com.shanebeestudios.domo.task.CustomEntityTask;
import com.shanebeestudios.domo.task.PlayerTask;
import com.shanebeestudios.domo.task.ServerTask;
import com.shanebeestudios.domo.util.DomoLogger;
import com.shanebeestudios.domo.util.Util;
import de.tr7zw.changeme.nbtapi.utils.MinecraftVersion;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.N;

@SuppressWarnings("unused")
public class DomoBloot extends JavaPlugin {

    static {
        // Register PlayerData configuration serialization
        ConfigurationSerialization.registerClass(PlayerData.class, "PlayerData");
    }

    private static DomoBloot instance;
    private PlayerDataConfig playerDataConfig;
    private PlayerManager playerManager;
    private RecipeManager recipeManager;

    // Tasks
    private PlayerTask playerTask;
    private ServerTask serverTask;
    private CustomEntityTask customEntityTask;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        Util.log("Initializing loading...");
        MinecraftVersion.replaceLogger(DomoLogger.getLogger());

        this.playerDataConfig = new PlayerDataConfig(this);
        this.playerManager = new PlayerManager(this);
        this.recipeManager = new RecipeManager(this);

        loadListeners();
        loadCommands();
        loadTasks();
        Util.setGameRules();

        Util.log("Successfully enabled plugin in &b" + (System.currentTimeMillis() - start) + " &7milliseconds");
    }

    @Override
    public void onDisable() {
        this.playerManager.getPlayerDatas().forEach(playerData -> playerDataConfig.saveData(playerData));
        this.playerTask.cancel();
        this.playerTask = null;
        this.serverTask.cancel();
        this.serverTask = null;
        this.customEntityTask.cancel();
        this.customEntityTask = null;
        this.playerDataConfig = null;
        this.playerManager = null;
        instance = null;
    }

    private void loadListeners() {
        EntityDefault.init();
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new WorldListener(), this);
        pm.registerEvents(new EntityListener(this), this);
    }

    private void loadTasks() {
        this.playerTask = new PlayerTask(this);
        this.serverTask = new ServerTask(this);
        this.customEntityTask = new CustomEntityTask(this);
    }

    private void loadCommands() {
        new GiveCmd(this);
        new TestCmd(this);
    }

    public static DomoBloot getPlugin() {
        return instance;
    }

    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(instance, key);
    }

    public PlayerDataConfig getPlayerDataConfig() {
        return playerDataConfig;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public RecipeManager getRecipeManager() {
        return recipeManager;
    }

}
