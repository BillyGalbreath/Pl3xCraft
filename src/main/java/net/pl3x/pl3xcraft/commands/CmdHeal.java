package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.configuration.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdHeal implements TabExecutor {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("command.heal")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                Lang.send(sender, Lang.PLAYER_COMMAND);
                return true;
            }

            ((Player) sender).setHealth(20);

            Lang.send(sender, Lang.HEAL_SELF);
            return true;
        }

        if (!sender.hasPermission("command.heal.other")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            Lang.send(sender, Lang.PLAYER_NOT_ONLINE);
            return true;
        }

        target.setHealth(20);

        Lang.send(sender, Lang.HEAL_OTHER
                .replace("{player}", target.getName()));
        Lang.send(target, Lang.HEAL_NOTICE
                .replace("{player}", sender.getName()));
        return true;
    }
}
