package me.kat.playgroundparkour;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import me.kat.playgroundparkour.ParkourManager;


public class Main extends JavaPlugin {

    private ParkourManager parkourManager;

    @Override
    public void onEnable() {
        // Save config if not exists
        saveDefaultConfig();

        // Start ParkourPlugin
        parkourManager = new ParkourManager(this);

        getLogger().info("PlaygroundParkour enable.");
    }

    @Override
    public void onDisable() {
        getLogger().info("PlaygroundParkour disable.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("parkour")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be executed by a player!.");
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
                        player.sendMessage("You don't have permission to start the parkour.");
                    }
                    break;
                case "setend":
                    if (player.hasPermission("parkour.admin")) {
                        parkourManager.setEndLocation(player.getLocation());
                        player.sendMessage("End point of the parkour added.");
                    } else {
                        player.sendMessage("You don't have permission to start the parkour.");
                    }
                    break;
                case "setcheckpoint":
                    if (player.hasPermission("parkour.admin")) {
                        if (args.length < 2) {
                            player.sendMessage("You need to specify the number of checkpoints. Use: /parkour setcheckpoint <number>");
                        } else {
                            try {
                                int checkpointNumber = Integer.parseInt(args[1]);
                                parkourManager.setCheckpointLocation(checkpointNumber, player.getLocation());
                                player.sendMessage("Checkpoint " + checkpointNumber + " added.");
                            } catch (NumberFormatException e) {
                                player.sendMessage("The number of checkpoint needs to be an integer.");
                            }
                        }
                    } else {
                        player.sendMessage("You don't have permission to specify the number of checkpoints.");
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

    public ParkourManager getParkourManager() {
        return parkourManager;
    }
}