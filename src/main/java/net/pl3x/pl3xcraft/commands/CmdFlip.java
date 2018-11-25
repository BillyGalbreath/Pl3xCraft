package net.pl3x.pl3xcraft.commands;

import net.pl3x.pl3xcraft.configuration.Lang;
import net.pl3x.pl3xcraft.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdFlip implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("command.flip")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (!(sender instanceof Player)) {
            Lang.send(sender, Lang.PLAYER_COMMAND);
            return true;
        }

        Player player = (Player) sender;

        String message = args.length == 0 ? "(╯°□°）╯︵ ┻━┻" : flip(String.join(" ", args));

        player.chat(ChatUtil.checkColorPerms(player, "chat", message));
        return true;
    }

    private String flip(String str) {
        String normal = "abcdefghijklmnopqrstuvwxyz_,;.?!/\\'ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String split = "ɐqɔpǝɟbɥıظʞןɯuodbɹsʇnʌʍxʎz‾'؛˙¿¡/\\,∀qϽᗡƎℲƃHIſʞ˥WNOԀὉᴚS⊥∩ΛMXʎZ";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char letter = str.charAt(i);
            int a = normal.indexOf(letter);
            sb.append((a != -1) ? split.charAt(a) : letter);
        }
        return new StringBuilder(sb.toString()).reverse().toString();
    }
}
