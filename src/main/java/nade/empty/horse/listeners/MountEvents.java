package nade.empty.horse.listeners;

import java.util.Calendar;
import java.util.Date;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class MountEvents implements Listener{
    
    @EventHandler
    public void onEntityDismount(EntityDismountEvent e) {
        if (Handlers.mounts.containsKey(e.getEntity().getUniqueId())) {
            e.getDismounted().remove();
            Handlers.mounts.remove(e.getEntity().getUniqueId());
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity().getType() == EntityType.HORSE) {
            Horse horse = (Horse) e.getEntity();
            for (Entity entity : horse.getPassengers()) {
                if (Handlers.mounts.containsKey(entity.getUniqueId())) {
                    e.getDrops().clear();
                    e.setDroppedExp(0);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.SECOND, 30);

                    Handlers.MOUNT_COOLDOWN.put(entity.getUniqueId(), calendar.getTime());
                }
            }
        }
    }

}
