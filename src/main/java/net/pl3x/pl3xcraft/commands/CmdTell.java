package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CmdTell implements TabExecutor {
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
        if (!(sender instanceof Player)) {
            Lang.send(sender, Lang.PLAYER_COMMAND);
            return true;
        }

        if (!sender.hasPermission("command.tell")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            Lang.send(sender, Lang.NO_PLAYER_SPECIFIED);
            return true;
        }

        //noinspection deprecation
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            Lang.send(sender, Lang.PLAYER_NOT_FOUND);
            return true;
        }

        if (args.length < 2) {
            Lang.send(sender, Lang.NO_MESSAGE_SPECIFIED);
            return true;
        }

        List<String> joined = new ArrayList<>(Arrays.asList(args));
        joined.remove(0);
        String message = String.join(" ", joined);
        if (Lang.isEmpty(message)) {
            Lang.send(sender, Lang.NO_MESSAGE_SPECIFIED);
            return true;
        }

        Player player = (Player) sender;
        PlayerConfig senderConfig = PlayerConfig.getConfig(player);
        if (senderConfig.isMuted()) {
            Lang.send(sender, Lang.YOU_ARE_MUTED);
            return true;
        }

        senderConfig.setReplyTarget(target);

        PlayerConfig.getConfig(target).setReplyTarget(player);

        Lang.send(target, Lang.TELL_TARGET
                .replace("{sender}", sender.getName())
                .replace("{target}", target.getName())
                .replace("{message}", message));
        Lang.send(sender, Lang.TELL_SENDER
                .replace("{sender}", sender.getName())
                .replace("{target}", target.getName())
                .replace("{message}", message));
        Bukkit.getOnlinePlayers().stream()
                .filter(spy -> PlayerConfig.getConfig(spy).isSpying())
                .filter(spy -> spy != player && spy != target)
                .forEach(spy -> Lang.send(spy, Lang.SPY_PREFIX + Lang.TELL_SPY
                        .replace("{sender}", sender.getName())
                        .replace("{target}", target.getName())
                        .replace("{message}", message)));
        return true;
    }
}
