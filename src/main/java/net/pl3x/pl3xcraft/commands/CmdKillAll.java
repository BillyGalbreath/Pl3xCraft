package net.pl3x.pl3xcraft.commands;

import java.util.List;
import net.pl3x.pl3xcraft.configuration.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class CmdKillAll implements TabExecutor {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("command.killall")){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission("command.killall.exempt"))
                .forEach(player ->
                        Lang.send(sender, Lang.PLAYER_EXEMPT
                                .replace("{getCommand}", cmd.getName() )
                                .replace("{getPlayer}", player.getDisplayName() ))
                        );

        Bukkit.getOnlinePlayers().stream()
                .filter(player -> !player.hasPermission("command.killall.exempt"))
                .forEach(player -> {
                    player.setHealth(0);
                    Lang.send(sender, Lang.KILLED_ALL_PLAYERS);
                    Lang.send(player, Lang.PLAYER_KILLED_YOU
                            .replace("{getPlayer}", sender.getName() ));
                });
        return true;
    }
}
