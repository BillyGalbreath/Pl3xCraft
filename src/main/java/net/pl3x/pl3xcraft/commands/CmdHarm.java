package net.pl3x.pl3xcraft.commands;

import com.sun.org.apache.bcel.internal.generic.LAND;
import java.util.List;
import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class CmdHarm implements TabExecutor {
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

        if (!sender.hasPermission("command.harm")){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (args.length < 1){
            Lang.send(sender, Lang.GET_INFO
                    .replace("{getDescription}", cmd.getDescription())
                    .replace("{getUsage}", cmd.getUsage()));
            return true;
        }

        Player otherPlayer = Pl3xCraft.getPlugin().getServer().getPlayer(args[0]);

        if (otherPlayer == null){
            Lang.send(sender, Lang.PLAYER_NOT_FOUND);
            return true;
        }

        int harmDamage;

        if (args.length > 1){
            try {
                harmDamage = Integer.parseInt(args[1]);
            } catch (NumberFormatException numberFormatExecption){
                Lang.send(sender, Lang.GET_INFO
                        .replace("{getDescription}", cmd.getDescription())
                        .replace("{getUsage}", cmd.getUsage()));
                return true;
            }

            if (harmDamage > 20 || harmDamage < 1){
                Lang.send(sender, Lang.GET_INFO
                        .replace("{getDescription}", cmd.getDescription())
                        .replace("{getUsage}", cmd.getUsage()));
                return true;
            }
        } else {
            harmDamage = 1; // Default damage if not specified
        }

        if (otherPlayer.hasPermission("command.harm.exempt")){
            Lang.send(sender, Lang.PLAYER_EXEMPT
                    .replace("{command}", cmd.getName())
                    .replace("{player}", otherPlayer.getDisplayName()));
            return true;
        }

        otherPlayer.damage(harmDamage);
        Lang.send(otherPlayer, Lang.HARMED_BY
                .replace("{getPlayer}", ((Player) sender).getDisplayName())
                .replace("{getHeartsAmount}", (harmDamage == 1 ? "half a" : (harmDamage / 2) + (harmDamage % 2 == 1 ? " and a half" : "") ) )
                .replace("{getHeart}", (harmDamage == 1 ? "heart" : "hearts") ));
        Lang.send(sender, Lang.YOU_HARMED
                .replace("{getPlayer}", otherPlayer.getDisplayName())
                .replace("{getHeartsAmount}", (harmDamage == 1 ? "half a" : (harmDamage / 2) + (harmDamage % 2 == 1 ? " and a half" : "") ) )
                .replace("{getHeart}", (harmDamage == 1 ? "heart" : "hearts") ));
        return true;
    }
}
