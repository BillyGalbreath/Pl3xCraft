package net.pl3x.pl3xcraft.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.pl3x.pl3xcraft.Pl3xCraft;
import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.configuration.PlayerConfig;
import net.pl3x.pl3xcraft.hook.Vault;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CmdHomes implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .filter(player -> player.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    .map(Player::getName).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Execute this command in another thread
        // This is for LuckPerms to handle OfflinePlayer permission checks
        new BukkitRunnable() {
            @Override
            public void run() {
                execute(sender, cmd, label, args);
            }
        }.runTaskAsynchronously(Pl3xCraft.getInstance());
        return true;
    }

    private void execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Lang.send(sender, Lang.PLAYER_COMMAND);
            return;
        }

        if (!sender.hasPermission("command.homes")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return;
        }

        PlayerConfig config;
        if (args.length > 0) {
            if (!sender.hasPermission("command.homes.other")) {
                Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
                return;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (target == null) {
                Lang.send(sender, Lang.PLAYER_NOT_FOUND);
                return;
            }

            if (Vault.hasPermission(target, "command.homes.exempt")) {
                Lang.send(sender, Lang.HOME_LIST_EXEMPT);
                return;
            }

            config = PlayerConfig.getConfig(target);
            if (config == null) {
                Lang.send(sender, Lang.PLAYER_NOT_FOUND);
                return;
            }
        } else {
            config = PlayerConfig.getConfig((Player) sender);
        }

        List<BaseComponent> homeList = new ArrayList<>(Arrays.asList(TextComponent.fromLegacyText(
                ChatColor.translateAlternateColorCodes('&', Lang.HOME_LIST.replace("{home-list}", "")))
        ));

        int count = 0;
        TextComponent separator = new TextComponent(", ");
        separator.setColor(ChatColor.YELLOW);

        Map<String, Location> data = config.getHomeData();
        if (data != null) {
            for (Map.Entry<String, Location> entry : data.entrySet()) {
                if (count > 0) {
                    homeList.add(separator);
                }
                count++;
                TextComponent home = new TextComponent(entry.getKey());
                home.setColor(ChatColor.GRAY);
                if (entry.getValue() == null) {
                    home.setStrikethrough(true);
                    home.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Not set...")));
                } else {
                    home.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(
                            entry.getValue().getWorld().getName() + " "
                                    + entry.getValue().getBlockX() + ","
                                    + entry.getValue().getBlockY() + ","
                                    + entry.getValue().getBlockZ())));
                    home.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                            "/home " + entry.getKey() + " " + config.getPlayer().getName()));
                }
                homeList.add(home);
            }
        }

        sender.sendMessage(homeList.toArray(new BaseComponent[0]));
    }
}
