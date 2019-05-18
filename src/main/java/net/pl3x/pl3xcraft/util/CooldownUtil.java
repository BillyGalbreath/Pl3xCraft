package net.pl3x.pl3xcraft.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.pl3x.pl3xcraft.Logger;
import net.pl3x.pl3xcraft.configuration.Config;

public class CooldownUtil {
    private static Map<String, CooldownUtil> cooldowns = new HashMap<String, CooldownUtil>();
    private long start;
    private final int timeInSeconds;
    private final UUID uuid;
    private final String cooldownName;

    public CooldownUtil(UUID uuid, String cooldownName, int timeInSeconds){
        this.uuid = uuid;
        this.cooldownName = cooldownName;
        this.timeInSeconds = timeInSeconds;
    }

    public static boolean isInCooldown(UUID uuid, String cooldownName){
        if (getTimeLeft(uuid, cooldownName) >= 1){
            return true;
        } else {
            stop(uuid, cooldownName);
            return false;
        }
    }

    private static void stop(UUID uuid, String cooldownName){
        CooldownUtil.cooldowns.remove(uuid + cooldownName);
    }

    private static CooldownUtil getCooldown(UUID uuid, String cooldownName){
        return cooldowns.get(uuid.toString() + cooldownName);
    }

    public static int getTimeLeft(UUID uuid, String cooldownName){
        CooldownUtil cooldown = getCooldown(uuid, cooldownName);
        int f = -1;
        if (cooldown != null){
            long now = System.currentTimeMillis();
            long cooldownTime = cooldown.start;
            int totalTime = cooldown.timeInSeconds;
            int r = (int) (now - cooldownTime) / 1000;
            f = (r - totalTime) * (-1);
        }
        return f;
    }

    public void start(){
        if (Config.COOLDOWN_MODE){
            this.start = System.currentTimeMillis();
            cooldowns.put(this.uuid.toString() + this.cooldownName, this);
        } else {
            Logger.debug("&dCooldown Mode is disabled in config, cannot apply cooldown to command.");
        }
    }
}
