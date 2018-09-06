package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CmdAssign implements TabExecutor {
    private final Pl3xCraft plugin;
    private static final HashMap<UUID, Location> assigndb = new HashMap<>();

    public CmdAssign(Pl3xCraft plugin) {
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
        if (!sender.hasPermission("command.assign")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }
        Player player = (Player) sender;
        PlayerConfig config = PlayerConfig.getConfig(player);
        ItemStack inHand = player.getItemInHand();
        Material inHandMaterial = player.getItemInHand().getType();
        if (inHandMaterial.isBlock()) {
            Lang.send(sender, Lang.ONLY_ITEMS_CAN_ASSIGN);
            return true;
        }
        // Remove command from item
        if (args.length < 1) {
            if (inHand == null || inHand.getType() == Material.AIR) {
                Lang.send(sender, Lang.NOTHING_IN_HAND_TO_REMOVE);
                return true;
            }

            List<String> inHandList = config.getAssignCommand(inHandMaterial);

            if ((inHandList.isEmpty())) {
                Lang.send(sender, Lang.NO_ASSIGN_COMMANDS_TO_ITEM);
                return true;
            }
            config.setAssignCommand(inHandMaterial, null);
            Lang.send(sender, Lang.COMMANDS_REMOVED_FROM_ITEM
                    .replace("{item}", inHand.getType().toString().toLowerCase().replace("_", " ")));
            return true;
        }

        if (inHand == null || inHand.getType() == Material.AIR) {
            Lang.send(sender, Lang.NOTHING_IN_HAND_TO_COMMAND);
            return true;
        }
        config.setAssignCommand(inHandMaterial, Collections.singletonList(args[0]));
        Lang.send(sender, Lang.COMMAND_ADDED_TO_ITEM
                .replace("{commandInput}", args[0]));
        return true;
    }
}
