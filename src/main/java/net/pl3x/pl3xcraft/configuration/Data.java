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

    public String getRandomMOTD() {
        List<String> motds = getStringList("motds");
        if (motds == null || motds.isEmpty()) {
            return null;
        }
        return motds.get(ThreadLocalRandom.current().nextInt(motds.size()));
    }
}
