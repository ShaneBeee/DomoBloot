package com.shanebeestudios.domo.command;

import com.google.common.collect.ImmutableList;
import com.shanebeestudios.domo.DomoBloot;
import com.shanebeestudios.domo.item.Item;
import com.shanebeestudios.domo.item.Items;
import com.shanebeestudios.domo.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
        if (args.length == 2 || args.length == 3) {
            int amount = 1;
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                Util.sendColMsg(sender, "&cPlayer <" + args[0] + "> not found!");
                return true;
            }
            if (args.length == 3) {
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException ignore) {
                }
            }
            for (Item item : Items.getItems()) {
                if (item.getKey().getKey().equalsIgnoreCase(args[1])) {
                    item.drop(player, amount);
                    if (sender instanceof Player) {
                        if (sender == player) {
                            Util.sendItemMsg(((Player) sender), "&aGave yourself &b" + amount + "&a of &b<item>", item);
                        } else {
                            Util.sendItemMsg(((Player) sender), "&aGave &b" + amount + "&a of &b<item>&a to &b" + player.getName(), item);
                            Util.sendItemMsg(player, "&aYou received &b" + amount + "&a of &b<item> &afrom &b" + sender.getName(), item);
                        }
                    } else {
                        Util.sendColMsg(sender, "&aGave &b" + amount + "&a of &b" + item.getName() + "&a to &b" + player.getName());
                        Util.sendItemMsg(player, "&aYou received &b" + amount + "&a of &b<item> &afrom &3Domo&bBloot", item);

                    }
                    return true;
                }
            }
            Util.sendColMsg(player, "&cItem &b" + args[1] + " &cnot found!");
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
