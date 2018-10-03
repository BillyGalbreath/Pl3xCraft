package net.pl3x.pl3xcraft.commands;

import java.util.List;
import net.pl3x.pl3xcraft.configuration.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class CmdHeal implements TabExecutor {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player target = args.length > 0 ? Bukkit.getPlayer(args[0]) : (Player) sender;
        if (!sender.hasPermission("command.heal" +(target != sender ? ".other" : ""))){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (target == null){
            Lang.send(sender, Lang.PLAYER_NOT_FOUND);
            return true;
        }

        target.setHealth(20);
        Lang.send(sender, (target == sender ? Lang.HEALTH_SET : Lang.HEALTH_SET_OTHER)
                .replace("{target}", target.getDisplayName()));
        return true;
    }
}
