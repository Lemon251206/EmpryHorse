package nade.empty.horse;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import nade.empty.horse.commands.Commands;
import nade.empty.horse.listeners.Handlers;
import nade.empty.horse.listeners.InventoryEvents;
import nade.empty.horse.listeners.MountEvents;
import nade.empty.horse.mechanics.HorseObject;
import nade.empty.horse.utils.CustomConfig;
import nade.empty.horse.utils.strings.Colors;
import net.milkbowl.vault.economy.Economy;

public class EmptyHorse extends JavaPlugin{
    
    private static Plugin instance;

    private Economy economy;

    public EmptyHorse() {
        instance = this;
    }

    @Override
    public void onLoad() {
        CustomConfig.create("config.yml");
        CustomConfig.create("database.yml");
        super.onLoad();
    }

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            Bukkit.getConsoleSender().sendMessage(Colors.vanilla("[EmptyHorse] - &cDisabled due to no &4Vault &cdependency found!"));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getPluginCommand("horse").setExecutor(new Commands(this));
        Bukkit.getPluginManager().registerEvents(new MountEvents(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryEvents(), this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        for (HorseObject horse : Lists.newArrayList(Handlers.mounts.values())) {
            horse.dismount();
        }
        super.onDisable();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public Economy getEconomy() {
        return economy;
    }

    public static Plugin getInstance() {
        return instance;
    }

}