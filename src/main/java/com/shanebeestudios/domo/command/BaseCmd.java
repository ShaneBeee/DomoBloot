package com.shanebeestudios.domo.command;

import com.shanebeestudios.domo.DomoBloot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public abstract class BaseCmd implements TabExecutor {

    private final DomoBloot plugin;

    BaseCmd(@NotNull DomoBloot plugin, @NotNull String commandString) {
        this.plugin = plugin;

        PluginCommand command = plugin.getCommand(commandString);
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    public DomoBloot getPlugin() {
        return plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return onCommand(sender, args);
    }

    protected abstract boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args);

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return onTabComplete(sender, args);
    }

    protected abstract List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args);
}
