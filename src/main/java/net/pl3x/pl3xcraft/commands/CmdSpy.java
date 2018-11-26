package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import org.apache.commons.lang.BooleanUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdSpy implements TabExecutor {
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
        if (!sender.hasPermission("command.spy")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
                return true;
            }

            PlayerConfig senderConfig = PlayerConfig.getConfig((Player) sender);
            boolean isSpying = !senderConfig.isSpying();
            senderConfig.setSpying(isSpying);

            Lang.send(sender, Lang.SPY_MODE_TOGGLED
                    .replace("{toggle}", BooleanUtils.toStringOnOff(isSpying)));
            return true;
        }

        if (!sender.hasPermission("command.spy.others")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            Lang.send(sender, Lang.PLAYER_NOT_FOUND);
            return true;
        }

        PlayerConfig targetConfig = PlayerConfig.getConfig(target);
        boolean isSpying = !targetConfig.isSpying();
        targetConfig.setSpying(isSpying);

        Lang.send(sender, Lang.SPY_MODE_TOGGLED_TARGET
                .replace("{toggle}", BooleanUtils.toStringOnOff(isSpying))
                .replace("{target}", target.getName()));
        return true;
    }
}
