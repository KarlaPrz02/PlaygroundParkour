package me.kat.playgroundparkour;

import me.kat.playgroundparkour.commands.ParkourCommand;
import me.kat.playgroundparkour.listeners.PlayerEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private ParkourManager parkourManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        parkourManager = new ParkourManager(this);
        getServer().getPluginManager().registerEvents(new PlayerEvents(this), this);
        getCommand("parkour").setExecutor(new ParkourCommand(this));
        getLogger().info("ParkourPlugin enable.");
    }

    @Override
    public void onDisable() {
        getLogger().info("ParkourPlugin disable.");
    }


    public ParkourManager getParkourManager() {
        return parkourManager;
    }

}