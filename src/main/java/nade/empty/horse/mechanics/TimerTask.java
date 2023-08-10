package nade.empty.horse.mechanics;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public abstract class TimerTask implements Runnable{
    private BukkitTask task;

    public TimerTask(Plugin plugin, int arg1, int arg2) {
        task = Bukkit.getScheduler().runTaskTimer(plugin, this, arg1, arg2);
    }

    public void canncel() {
        task.cancel();
    }
}
