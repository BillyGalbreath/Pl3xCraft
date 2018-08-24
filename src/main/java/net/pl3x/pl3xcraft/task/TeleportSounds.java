package net.pl3x.pl3xcraft.task;

import net.pl3x.pl3xcraft.configuration.Config;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportSounds extends BukkitRunnable {
    private final Location to;
    private final Location from;

    public TeleportSounds(Location to, Location from) {
        this.to = to;
        this.from = from;
    }

    @Override
    public void run() {
        if (Config.USE_TELEPORT_SOUNDS) {
            if (Config.SOUND_TO != null) {
                to.getWorld().playSound(to, Config.SOUND_TO, 1.0F, 1.0F);
            }

            if (Config.SOUND_FROM != null) {
                from.getWorld().playSound(from, Config.SOUND_FROM, 1.0F, 1.0F);
            }
        }
    }
}
