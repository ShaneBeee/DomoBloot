package com.shanebeestudios.domo.command;

import com.shanebeestudios.domo.DomoBloot;
import com.shanebeestudios.domo.entity.CustomEntities;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TestCmd extends BaseCmd {

    public TestCmd(@NotNull DomoBloot plugin) {
        super(plugin, "test");
    }

    @Override
    protected boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Player only command");
            return true;
        }
        Location location = player.getTargetBlock(32).getLocation().clone().add(0, 1, 0);
        CustomEntities.PILLAGER_HORSEMEN.spawn(location);
        return true;
    }

    @Override
    protected List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        return null;
    }
}
