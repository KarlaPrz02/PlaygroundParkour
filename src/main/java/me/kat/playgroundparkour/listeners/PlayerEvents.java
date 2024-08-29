package me.kat.playgroundparkour.listeners;

import me.kat.playgroundparkour.Main;
import me.kat.playgroundparkour.ParkourManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerEvents implements Listener {

    private final Main plugin;


    public PlayerEvents(Main main){
        this.plugin = main;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();

        // Verifica si el jugador ha caído a la altura 0 o por debajo
        if (to.getY() <= 0) {
            ParkourManager parkourManager = plugin.getParkourManager();
            Location lastCheckpoint = parkourManager.getLastCheckpoint(player);

            if (lastCheckpoint != null) {
                player.teleport(lastCheckpoint);
                player.sendMessage("You have been teleported to your last checkpoint.");
            } else {
                Location startLocation = parkourManager.getStartLocation();
                if (startLocation != null) {
                    player.teleport(startLocation);
                    player.sendMessage("No checkpoint found, teleported to start.");
                }
            }
        }
    }

}
