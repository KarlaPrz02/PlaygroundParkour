package me.kat.playgroundparkour;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ParkourManager {

    private final Main plugin;
    private Location startLocation;
    private Location endLocation;
    private Map<Integer, Location> checkpoints;

    public ParkourManager(Main plugin) {
        this.plugin = plugin;
        this.checkpoints = new HashMap<>();
        loadParkourLocations();
    }

    public void loadParkourLocations() {
        FileConfiguration config = plugin.getConfig();
        if (config.contains("parkour.start")) {
            startLocation = (Location) config.get("parkour.start");
        }
        if (config.contains("parkour.end")) {
            endLocation = (Location) config.get("parkour.end");
        }
        if (config.contains("parkour.checkpoints")) {
            for (String key : config.getConfigurationSection("parkour.checkpoints").getKeys(false)) {
                int checkpointNumber = Integer.parseInt(key);
                checkpoints.put(checkpointNumber, (Location) config.get("parkour.checkpoints." + checkpointNumber));
            }
        }
    }

    public void saveParkourLocations() {
        FileConfiguration config = plugin.getConfig();
        if (startLocation != null) {
            config.set("parkour.start", startLocation);
        }
        if (endLocation != null) {
            config.set("parkour.end", endLocation);
        }
        for (Map.Entry<Integer, Location> entry : checkpoints.entrySet()) {
            config.set("parkour.checkpoints." + entry.getKey(), entry.getValue());
        }
        plugin.saveConfig();
    }

    public void startParkour(Player player) {
        if (startLocation == null) {
            player.sendMessage("The starting point of parkour has not been established.");
            return;
        }
        player.teleport(startLocation);
        player.sendMessage("¡The parkour has started!");
        //Inicializa el estado del parkour del jugador, como su progreso en checkpoints
    }

    public void stopParkour(Player player) {
        player.sendMessage("The parkour has ended.");
        //Agregar lógica para verificar si el jugador llegó al final o calcular el tiempo total
    }

    public void setStartLocation(Location location) {
        this.startLocation = location;
        saveParkourLocations();
    }

    public void setEndLocation(Location location) {
        this.endLocation = location;
        saveParkourLocations();
    }

    public void setCheckpointLocation(int checkpointNumber, Location location) {
        checkpoints.put(checkpointNumber, location);
        saveParkourLocations();
    }

    public Location getCheckpointLocation(int checkpointNumber) {
        return checkpoints.get(checkpointNumber);
    }
}