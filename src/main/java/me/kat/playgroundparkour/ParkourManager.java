package me.kat.playgroundparkour;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ParkourManager {

    private final Main plugin;
    private Location startLocation;
    private Location endLocation;
    private final Map<Integer, Location> checkpoints = new HashMap<>();
    private final Map<Player, Location> playerCheckpoints = new HashMap<>();

    public ParkourManager(Main plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void setStartLocation(Location location) {
        this.startLocation = location;
        saveLocationToConfig("startLocation", location);
    }

    public void setEndLocation(Location location) {
        this.endLocation = location;
        saveLocationToConfig("endLocation", location);
    }

    public void setCheckpointLocation(int checkpointNumber, Location location) {
        checkpoints.put(checkpointNumber, location);
        saveLocationToConfig("checkpoints." + checkpointNumber, location);
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void startParkour(Player player) {
        player.sendMessage("¡You have started the parkour!");
    }

    public void stopParkour(Player player) {
        // Detiene el parkour para el jugador (pendiente de hacer xd)
        player.sendMessage("You have been stopped the parkour.");
    }

    public Location getLastCheckpoint(Player player) {
        return playerCheckpoints.get(player);
    }

    public void reachCheckpoint(Player player, int checkpointNumber) {
        Location checkpoint = checkpoints.get(checkpointNumber);
        if (checkpoint != null) {
            playerCheckpoints.put(player, checkpoint);
            player.sendMessage("Checkpoint " + checkpointNumber + " reached.");
        } else {
            player.sendMessage("Checkpoint " + checkpointNumber + " not found.");
        }
    }

    private void loadConfig() {
        if (plugin.getConfig().contains("startLocation")) {
            startLocation = getLocationFromConfig("startLocation");
        }
        if (plugin.getConfig().contains("endLocation")) {
            endLocation = getLocationFromConfig("endLocation");
        }

        if (plugin.getConfig().contains("checkpoints")) {
            for (String key : plugin.getConfig().getConfigurationSection("checkpoints").getKeys(false)) {
                int checkpointNumber = Integer.parseInt(key);
                Location location = getLocationFromConfig("checkpoints." + key);
                checkpoints.put(checkpointNumber, location);
            }
        }
    }

    private Location getLocationFromConfig(String path) {
        double x = plugin.getConfig().getDouble(path + ".x");
        double y = plugin.getConfig().getDouble(path + ".y");
        double z = plugin.getConfig().getDouble(path + ".z");
        String world = plugin.getConfig().getString(path + ".world");
        float yaw = (float) plugin.getConfig().getDouble(path + ".yaw");
        float pitch = (float) plugin.getConfig().getDouble(path + ".pitch");

        return new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch);
    }

    private void saveLocationToConfig(String path, Location location) {
        plugin.getConfig().set(path + ".world", location.getWorld().getName());
        plugin.getConfig().set(path + ".x", location.getX());
        plugin.getConfig().set(path + ".y", location.getY());
        plugin.getConfig().set(path + ".z", location.getZ());
        plugin.getConfig().set(path + ".yaw", location.getYaw());
        plugin.getConfig().set(path + ".pitch", location.getPitch());
        plugin.saveConfig();
    }
}