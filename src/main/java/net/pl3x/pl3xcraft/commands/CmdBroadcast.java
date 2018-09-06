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

public class CmdBroadcast implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        return Bukkit.getOnlinePlayers().stream() // stream over all online players
                .filter(p -> p.getName().toLowerCase().startsWith(args[args.length - 1].toLowerCase())) // filter only the ones with matching names
                .map(HumanEntity::getName) // collect their names into map
                .collect(Collectors.toList()); // collect to List<String>
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("command.broadcast")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (args.length > 0) {
            Bukkit.getOnlinePlayers().forEach(p ->
                    Lang.send(p, Lang.BROADCAST_TITLE
                            .replace("{message}", String.join(" ", args))));

            return true;
        }

        return false; // show command usage
    }
}
