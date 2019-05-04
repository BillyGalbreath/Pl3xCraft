package net.pl3x.pl3xcraft.commands;

import java.awt.Label;
import java.util.List;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.hook.Vault;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class CmdWorkbench implements TabExecutor {


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            Lang.send(sender, Lang.PLAYER_COMMAND);
            return true;
        }

        if (!sender.hasPermission("command.workbench")){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        Player player = (Player) sender;

        player.openWorkbench(null, true);
        Lang.send(sender, Lang.WORKBENCH_OPEN);
        return true;
    }
}
