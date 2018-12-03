package net.pl3x.pl3xcraft.configuration;

import net.pl3x.pl3xcraft.Pl3xCraft;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Data extends YamlConfiguration {
    private static Data data;

    public static Data getInstance() {
        if (data == null) {
            data = new Data();
        }
        return data;
    }

    private final File file;
    private final Object saveLock = new Object();

    public Data() {
        super();
        file = new File(Pl3xCraft.getInstance().getDataFolder(), "data.yml");
        if (!file.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (IOException ignore) {
            }
        }
        reload();
    }

    public void reload() {
        synchronized (saveLock) {
            try {
                load(file);
            } catch (Exception ignore) {
            }
        }
    }

    private void save() {
        synchronized (saveLock) {
            try {
                save(file);
            } catch (Exception ignore) {
            }
        }
    }

    public Location getSpawn() {
        World world = Bukkit.getWorld(getString("spawn.world", ""));
        if (world == null) {
            return null;
        }
        try {
            double x = getDouble("spawn.x");
            double y = getDouble("spawn.y");
            double z = getDouble("spawn.z");
            double pitch = getDouble("spawn.pitch");
            double yaw = getDouble("spawn.yaw");
            return new Location(world, x, y, z, (float) yaw, (float) pitch);
        } catch (Exception e) {
            return null;
        }
    }

    public void setSpawn(Location location) {
        set("spawn.world", location.getWorld().getName());
        set("spawn.x", location.getX());
        set("spawn.y", location.getY());
        set("spawn.z", location.getZ());
        set("spawn.pitch", location.getPitch());
        set("spawn.yaw", location.getYaw());
        save();
    }

    public String getRandomMOTD() {
        List<String> motds = getStringList("motds");
        if (motds == null || motds.isEmpty()) {
            return null;
        }
        return motds.get(ThreadLocalRandom.current().nextInt(motds.size()));
    }
}
