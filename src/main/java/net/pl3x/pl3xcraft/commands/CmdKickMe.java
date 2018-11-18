package net.pl3x.pl3xcraft.commands;

import java.util.List;
import net.pl3x.pl3xcraft.configuration.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class CmdKickMe implements TabExecutor {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Lang.send(sender, Lang.PLAYER_COMMAND);
            return true;
        }

        Player target = (Player) sender;

        if (!target.hasPermission("command.kickme.exempt")){
            target.kickPlayer(Lang.KICK_ME);
        } else {
            Lang.send(sender, Lang.KICK_ME_EXEMPT);
        }

        return true;
    }
}
