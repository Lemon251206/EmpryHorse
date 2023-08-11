package nade.empty.horse.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.HorseInventory;

public class InventoryEvents implements Listener{

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory() instanceof HorseInventory) {
            if (Handlers.mounts.containsKey(e.getWhoClicked().getUniqueId())) {
                int rawSlot = e.getRawSlot();
                if (rawSlot == 0 || rawSlot == 1) e.setCancelled(true);
            }
        }
    }
    
}
