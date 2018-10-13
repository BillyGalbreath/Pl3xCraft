package net.pl3x.pl3xcraft.commands;

import java.util.List;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class CmdNick implements TabExecutor {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)){
            Lang.send(sender, Lang.PLAYER_COMMAND);
            return true;
        }

        Player target = args.length > 1 ? Bukkit.getPlayer(args[0]) : (Player) sender;
        PlayerConfig config;

        if (!sender.hasPermission("command.nick" + (target != sender ? ".other" : ""))){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (target == null){
            Lang.send(sender, Lang.PLAYER_NOT_FOUND);
            return true;
        }

        config = PlayerConfig.getConfig(target);

        if (args.length > 1) {
            if (target.hasPermission("command.nick.exempt")){
                Lang.send(sender, Lang.PLAYER_EXEMPT
                        .replace("{getCommand}", cmd.getName())
                        .replace("{getPlayer}", target.getDisplayName()));
                return true;
            }

            if (!target.isOnline()){
                Lang.send(sender, Lang.PLAYER_NOT_ONLINE);
                return true;
            }

            if (args[1].equalsIgnoreCase("remove")){
                config.removeNick(target);
                // config.setNick(target,"");
                Lang.send(sender, Lang.NICK_REMOVED_OTHER
                        .replace("{getPlayer}", target.getDisplayName())
                        .replace("{possessive}", target.getDisplayName().toLowerCase().endsWith("s") ? "'" : "'s") );
                return true;
            } else {
                config.setNick(target, args[1]);
            }
        }


        if (args[0].equalsIgnoreCase("remove")) {
            config.removeNick(target);
            // config.setNick(target, "");
            Lang.send(sender, Lang.NICK_REMOVED);
            return true;
        } else {
            config.setNick(target, args[0]);
        }

        Lang.send(sender, Lang.NICK_SET);
        return true;
    }
}
