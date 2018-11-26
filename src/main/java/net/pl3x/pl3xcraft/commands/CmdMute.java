package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdMute implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .filter(player -> player.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    .map(Player::getName).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("command.mute")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            Lang.send(sender, Lang.NO_PLAYER_SPECIFIED);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            Lang.send(sender, Lang.PLAYER_NOT_FOUND);
            return true;
        }

        PlayerConfig targetConfig = PlayerConfig.getConfig(target);
        if (!targetConfig.isMuted() && target.hasPermission("command.mute.exempt")) {
            Lang.send(sender, Lang.EXEMPT_MUTE
                    .replace("{target}", target.getName()));
            return true;
        }

        targetConfig.setMuted(!targetConfig.isMuted());

        Lang.send(sender, (targetConfig.isMuted() ? Lang.TARGET_MUTED : Lang.TARGET_UNMUTED)
                .replace("{target}", target.getName()));
        return true;
    }
}
