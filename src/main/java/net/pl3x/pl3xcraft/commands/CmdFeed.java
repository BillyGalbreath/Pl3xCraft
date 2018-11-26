package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.configuration.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdFeed implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        // tab complete player names only if we have permission
        if (args.length == 1 && sender.hasPermission("command.feed.other")) {
            return Bukkit.getOnlinePlayers().stream()
                    .filter(player -> player.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    .map(HumanEntity::getName).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("command.feed")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                Lang.send(sender, Lang.PLAYER_COMMAND);
                return true;
            }

            Player player = (Player) sender;

            player.setFoodLevel(20);
            player.setSaturation(20);

            Lang.send(sender, Lang.FEED_SELF);
            return true;
        }

        if (!sender.hasPermission("command.feed.other")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            Lang.send(sender, Lang.PLAYER_NOT_ONLINE);
            return true;
        }

        target.setFoodLevel(20);
        target.setSaturation(20);

        Lang.send(sender, Lang.FEED_OTHER
                .replace("{player}", target.getName()));
        Lang.send(target, Lang.FEED_NOTICE
                .replace("{player}", sender.getName()));
        return true;
    }
}
