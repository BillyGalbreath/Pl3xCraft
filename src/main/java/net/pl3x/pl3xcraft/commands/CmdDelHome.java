package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import net.pl3x.pl3xcraft.hook.Vault;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdDelHome implements TabExecutor {
    private final Pl3xCraft plugin;

    public CmdDelHome(Pl3xCraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                return PlayerConfig.getConfig(plugin, (OfflinePlayer) sender).getMatchingHomeNames(args[0]);
            }
            if (args.length == 2) {
                return Bukkit.getOnlinePlayers().stream()
                        .filter(player -> player.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                        .map(Player::getName).collect(Collectors.toList());
            }
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Lang.send(sender, Lang.PLAYER_COMMAND);
            return true;
        }

        if (!sender.hasPermission("command.delhome")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        PlayerConfig config;
        if (args.length > 1) {
            if (!sender.hasPermission("command.delhome.other")) {
                Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
                return true;
            }

            //noinspection deprecation (fucking bukkit)
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if (target == null) {
                Lang.send(sender, Lang.PLAYER_NOT_FOUND);
                return true;
            }

            if (Vault.hasPermission(target, "command.delhome.exempt")) {
                Lang.send(sender, Lang.HOME_DELETE_EXEMPT);
                return true;
            }

            config = PlayerConfig.getConfig(plugin, target);

            if (config == null) {
                Lang.send(sender, Lang.PLAYER_NOT_FOUND);
                return true;
            }
        } else {
            config = PlayerConfig.getConfig(plugin, (Player) sender);
        }

        String home = (args.length > 0) ? args[0] : "home";
        if (config.getHome(home) == null) {
            Lang.send(sender, Lang.HOME_DOES_NOT_EXIST);
            return true;
        }

        config.setHome(home, null);
        Lang.send(sender, Lang.HOME_DELETED
                .replace("{home}", home));
        return true;
    }
}
