package net.pl3x.pl3xcraft.util;

import org.bukkit.entity.Player;

/*
https://github.com/legobmw99/BetterThanMending/blob/master/src/main/java/com/legobmw99/BetterThanMending/util/Utilities.java
 */
public class ExpUtil {
    public static int getPlayerXP(Player player) {
        return (int) (getExperienceForLevel(player.getLevel()) + (player.getExp() * player.getExpToLevel()));
    }

    public static void addPlayerXP(Player player, int amount) {

        int experience = getPlayerXP(player) + amount;
        if (experience < 0) {
            return;
        }
        player.setTotalExperience(experience);
        player.setLevel(getLevelForExperience(experience));
        int expForLevel = getExperienceForLevel(player.getLevel());
        player.setExp((experience - expForLevel) / (float) player.getExpToLevel());
    }

    public static int getLevelForExperience(int experience) {
        int i = 0;
        while (getExperienceForLevel(i) <= experience) {
            i++;
        }
        return i - 1;
    }

    public static int getExperienceForLevel(int level) {
        if (level == 0) {
            return 0;
        }
        if (level > 0 && level < 16) {
            return (int) (Math.pow(level, 2) + 6 * level);
        } else if (level > 15 && level < 32) {
            return (int) (2.5 * Math.pow(level, 2) - 40.5 * level + 360);
        } else {
            return (int) (4.5 * Math.pow(level, 2) - 162.5 * level + 2220);
        }
    }
}
