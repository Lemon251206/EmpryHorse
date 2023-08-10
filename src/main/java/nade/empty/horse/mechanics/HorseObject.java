package nade.empty.horse.mechanics;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Horse.Color;
import org.bukkit.inventory.ItemStack;

import nade.empty.horse.EmptyHorse;
import nade.empty.horse.listeners.Handlers;
import nade.empty.horse.utils.strings.Colors;

public class HorseObject {
    private String displayname;
    private Color color;
    private Date duration;

    private UUID owner;

    private Horse horse;

    public HorseObject(UUID owner) {
        this.owner = owner;
    }

    public HorseObject(UUID owner, Date duration) {
        this.owner = owner;
        this.duration = duration;
    }

    public void mount() {
        Player player = Bukkit.getPlayer(owner);
        horse = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
        horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
        horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.225);
        horse.setAdult();
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        horse.setOwner(player);
        if (!Objects.isNull(displayname)) {
            horse.setCustomName(Colors.vanilla(displayname));
            horse.setCustomNameVisible(true);
        }
        if (!Objects.isNull(color)) {
            horse.setColor(color);
        }
        horse.addPassenger(player);
        Handlers.mounts.put(owner, this);
        
        if (!Objects.isNull(this.getDuration())) {
            new TimerTask(EmptyHorse.getInstance(), 0, 20) {
                @Override
                public void run() {
                    long current = new Date().getTime();
                    long duration = getDuration().getTime();

                    if (!Handlers.mounts.containsKey(player.getUniqueId())) {
                        this.canncel();
                        return;
                    }

                    if (current >= duration) {
                        horse.removePassenger(player);
                        player.sendMessage(Colors.vanilla("&cNgựa của bạn đã hết thời hạn thuê, vui lòng gia hạn hoặc thuê một con ngựa khác!!"));
                        this.canncel();
                        return;
                    }
                }
            };
        }
    }

    public void dismount() {
        this.getHorse().removePassenger(Bukkit.getPlayer(owner));
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public String getDisplayname() {
        return displayname;
    }

    public Color getColor() {
        return color;
    }

    public Date getDuration() {
        return duration;
    }

    public Horse getHorse() {
        return horse;
    }
}
