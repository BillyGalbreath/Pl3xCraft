package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import org.apache.commons.lang.BooleanUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdTeleportToggle implements TabExecutor {
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

        if (!sender.hasPermission("command.teleporttoggle")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        PlayerConfig config = PlayerConfig.getConfig((Player) sender);
        config.setAllowTeleports(!config.allowTeleports());

        Lang.send(sender, Lang.TELEPORT_TOGGLE_SET
                .replace("{toggle}", BooleanUtils.toStringOnOff(config.allowTeleports())));
        return true;
    }
}
