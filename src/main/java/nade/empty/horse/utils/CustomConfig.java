package nade.empty.horse.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Maps;

import nade.empty.horse.EmptyHorse;
import nade.empty.horse.utils.strings.Colors;

public class CustomConfig {
    private static Map<String, FileConfiguration> configurations = Maps.newLinkedHashMap();

    private static Plugin plugin = EmptyHorse.getInstance();

    public static void create(String path) {
        File file = new File(plugin.getDataFolder(), path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(path, false);
        }

        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            System.out.println(Colors.vanilla("&cFail to create file '" + path + "', please try again!!"));
            return;
        }
        if (!Objects.isNull(config)) {
            configurations.put(path, config);
        }
    }

    public static FileConfiguration get(String path) {
        if (configurations.containsKey(path)) {
            return configurations.get(path);
        }
        return null;
    }

    public static void save(String path, boolean copyDefault) {
        if (configurations.containsKey(path)) {
            FileConfiguration config = configurations.get(path);
            if (copyDefault) {
                config.options().copyDefaults(copyDefault);
            }
            try {
                config.save(plugin.getDataFolder() + "\\" + path);
            } catch (IOException e) {
            System.out.println(Colors.vanilla("&cFail to save file '" + path + "', please try again!!"));
            }
        }
    }
}
