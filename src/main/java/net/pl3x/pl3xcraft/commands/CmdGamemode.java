package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.configuration.Lang;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdGamemode implements TabExecutor {
    private final GameMode gameMode;
    private final String gameModeStr;

    public CmdGamemode(GameMode gameMode) {
        this.gameMode = gameMode;
        this.gameModeStr = gameMode.name().toLowerCase();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            String name = args[0].toLowerCase();
            return Bukkit.getOnlinePlayers().stream()
                    .filter(player -> player.getName().toLowerCase().startsWith(name))
                    .map(Player::getName).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player target = args.length > 0 ? Bukkit.getPlayer(args[0]) : (Player) sender;
        if (!sender.hasPermission("command.gamemode." + gameModeStr
                + (target != sender ? ".others" : ""))) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (target == null) {
            Lang.send(sender, Lang.PLAYER_NOT_FOUND);
            return true;
        }

        target.setGameMode(gameMode);
        Lang.send(sender, (target == sender ? Lang.GAMEMODE_SET : Lang.GAMEMODE_SET_OTHER)
                .replace("{target}", target.getName())
                .replace("{gamemode}", gameModeStr));
        return true;
    }
}
