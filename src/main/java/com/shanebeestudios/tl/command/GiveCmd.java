package com.shanebeestudios.tl.command;

import com.google.common.collect.ImmutableList;
import com.shanebeestudios.tl.DomoBloot;
import com.shanebeestudios.tl.item.Item;
import com.shanebeestudios.tl.item.Items;
import com.shanebeestudios.tl.util.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GiveCmd extends BaseCmd {

    public GiveCmd(@NotNull DomoBloot plugin) {
        super(plugin, "giveitem");
    }

    @Override
    protected boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            Util.error("Player command only!");
            return true;
        }
        Player player = ((Player) sender);
        if (args.length == 2 || args.length == 3) {
            int amount = 1;
            if (args.length == 3) {
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException ignore) {
                }
            }
            for (Item item : Items.getItems()) {
                if (item.getKey().getKey().equalsIgnoreCase(args[1])) {
                    ItemStack itemStack = item.getItemStack();
                    itemStack.setAmount(amount);
                    player.getInventory().addItem(itemStack);
                    return true;
                }
            }
            Util.sendColMsg(player, "&cItem &b" + args[1] + " &cnot found!", true);
        }
        return true;
    }

    @Override
    protected List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length <= 1) {
            return null;
        } else if (args.length == 2) {
            List<String> matches = new ArrayList<>();
            for (Item item : Items.getItems()) {
                String name = item.getKey().getKey().toUpperCase();
                if (StringUtil.startsWithIgnoreCase(name, args[1])) {
                    matches.add(name);
                }
            }
            return matches;
        } else if (args.length == 3) {
            return Collections.singletonList("<amount>");
        } else {
            return ImmutableList.of();
        }
    }

}
