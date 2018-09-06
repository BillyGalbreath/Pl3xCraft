package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CmdBroadcast implements TabExecutor {
    private final Pl3xCraft plugin;

    public CmdBroadcast(Pl3xCraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        // Set up new List for our autocomplete data
        List<String> autoComplete = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()){
            autoComplete.add(player.getDisplayName());
        }
        return autoComplete;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            Lang.send(sender, Lang.PLAYER_COMMAND);
            return true;
        }

        if (!sender.hasPermission("command.broadcast")){
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (args.length == 0){
            Lang.send(sender, cmd.getDescription());
            return false;
        }

        StringBuilder message = new StringBuilder();
        message.append(Lang.BROADCAST_TITLE);

        if (args.length > 0){
            message.append(args[0]);
            for (int i = 1; i < args.length; i++){
                message.append(" ").append(args[i]);
            }
        }

        Bukkit.broadcastMessage(message.toString());
        return false;
    }
}
