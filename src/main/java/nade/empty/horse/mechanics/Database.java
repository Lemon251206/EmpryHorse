package nade.empty.horse.mechanics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Horse.Color;

import nade.empty.horse.utils.CustomConfig;

public class Database {

    private static FileConfiguration database = CustomConfig.get("database.yml");

    private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static HorseObject register(UUID owner, Date duration) {
        database.set(owner.toString() + ".duration", format.format(duration));
        CustomConfig.save("database.yml", false);
        return new HorseObject(owner, duration);
    }

    public static void remove(UUID owner) {
        database.set(owner.toString(), null);
        CustomConfig.save("database.yml", false);
    }

    public static void update(UUID owner, HorseObject horse) {
        if (!Objects.isNull(horse.getDisplayname())) {
            database.set(owner.toString() + ".displayname", horse.getDisplayname());
        }
        if (!Objects.isNull(horse.getColor())) {
            database.set(owner.toString() + ".color", horse.getColor().toString());
        }
        if (!Objects.isNull(horse.getDuration())) {
            database.set(owner.toString() + ".duration", format.format(horse.getDuration()));
        }
        CustomConfig.save("database.yml", false);
    }

    public static HorseObject get(UUID owner) {
        if (database.contains(owner.toString())) {
            HorseObject horse = new HorseObject(owner);
            ConfigurationSection section = database.getConfigurationSection(owner.toString());
            if (section.contains("displayname")) {
                horse.setDisplayname(section.getString("displayname"));
            }
            if (section.contains("color")) {
                horse.setColor(Color.valueOf(section.getString("color").toUpperCase()));
            }
            if (section.contains("duration")) {
                try {
                    horse.setDuration(format.parse(section.getString("duration")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return horse;
        }
        return null;
    }

    public static boolean contains(UUID owner) {
        return database.contains(owner.toString());
    }
}
