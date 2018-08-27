package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import net.pl3x.pl3xcraft.request.TpaHereRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class CmdTeleportRequestAll implements TabExecutor {
    private final Pl3xCraft plugin;

    public CmdTeleportRequestAll(Pl3xCraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Lang.send(sender, Lang.PLAYER_COMMAND);
            return true;
        }

        if (!sender.hasPermission("command.teleportrequestall")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        UUID senderUUID = ((Player) sender).getUniqueId();
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target.getUniqueId().equals(senderUUID)) {
                continue;
            }

            // check for pending requests
            PlayerConfig targetConfig = PlayerConfig.getConfig(target);
            if (targetConfig.getRequest() != null) {
                continue; // skip
            }

            // check for toggles and overrides
            if (target.hasPermission("command.teleporttoggle") &&
                    !sender.hasPermission("command.teleport.override") &&
                    !targetConfig.allowTeleports()) {
                continue;
            }

            // Create new request
            targetConfig.setRequest(new TpaHereRequest(plugin, (Player) sender, target));
        }

        Lang.send(sender, Lang.TELEPORT_REQUESTALL_REQUESTER);
        return true;
    }
}
