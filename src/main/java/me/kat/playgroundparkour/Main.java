package me.kat.playgroundparkour;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.Location;

public class Main extends JavaPlugin implements Listener {

    private ParkourManager parkourManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        parkourManager = new ParkourManager(this);
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("ParkourPlugin enable.");
    }

    @Override
    public void onDisable() {
        getLogger().info("ParkourPlugin disable.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("parkour")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be executed by a player.");
                return true;
            }

            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage("Use: /parkour <start|stop|setstart|setend|setcheckpoint>");
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "start":
                    parkourManager.startParkour(player);
                    break;
                case "stop":
                    parkourManager.stopParkour(player);
                    break;
                case "setstart":
                    if (player.hasPermission("parkour.admin")) {
                        parkourManager.setStartLocation(player.getLocation());
                        player.sendMessage("Start point of the parkour added.");
                    } else {
                        player.sendMessage("You don`t have permissions to use this command.");
                    }
                    break;
                case "setend":
                    if (player.hasPermission("parkour.admin")) {
                        parkourManager.setEndLocation(player.getLocation());
                        player.sendMessage("Finish point of the parkour added.");
                    } else {
                        player.sendMessage("You don`t have permissions to use this command.");
                    }
                    break;
                case "setcheckpoint":
                    if (player.hasPermission("parkour.admin")) {
                        if (args.length < 2) {
                            player.sendMessage("You need to specify the number of the checkpoint. Use: /parkour setcheckpoint <number>");
                        } else {
                            try {
                                int checkpointNumber = Integer.parseInt(args[1]);
                                parkourManager.setCheckpointLocation(checkpointNumber, player.getLocation());
                                player.sendMessage("Checkpoint " + checkpointNumber + " added.");
                            } catch (NumberFormatException e) {
                                player.sendMessage("The checkpoint number must be an integer.");
                            }
                        }
                    } else {
                        player.sendMessage("You don`t have permissions to use this command.");
                    }
                    break;
                default:
                    player.sendMessage("Use: /parkour <start|stop|setstart|setend|setcheckpoint>");
                    break;
            }

            return true;
        }

        return false;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();

        // Verifica si el jugador ha caído a la altura 0 o por debajo
        if (to.getY() <= 0) {
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