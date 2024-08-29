package me.kat.playgroundparkour.commands;

import me.kat.playgroundparkour.Main;
import me.kat.playgroundparkour.ParkourManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParkourCommand implements TabExecutor {

    private final Main plugin;


    public ParkourCommand(Main main){
        this.plugin = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("Use: /parkour <start|stop|setstart|setend|setcheckpoint>");
            return true;
        }
        ParkourManager parkourManager = plugin.getParkourManager();

        switch (args[0].toLowerCase()) {
            case "start":
                parkourManager.startParkour(player);
                return true;
            case "stop":
                parkourManager.stopParkour(player);
                return true;
            case "setstart":
                if (player.hasPermission("parkour.admin")) {
                    parkourManager.setStartLocation(player.getLocation());
                    player.sendMessage("Start point of the parkour added.");
                    return true;
                }
                player.sendMessage("You don`t have permissions to use this command.");
                return false;
            case "setend":
                if (player.hasPermission("parkour.admin")) {
                    parkourManager.setEndLocation(player.getLocation());
                    player.sendMessage("Finish point of the parkour added.");
                    return true;
                }
                player.sendMessage("You don`t have permissions to use this command.");
                return false;
            case "setcheckpoint":
                if (!player.hasPermission("parkour.admin")) {
                    player.sendMessage("You don`t have permissions to use this command.");
                    return false;
                } else if (args.length < 2) {
                    player.sendMessage("You need to specify the number of the checkpoint. Use: /parkour setcheckpoint <number>");
                    return false;
                }
                try {
                    int checkpointNumber = Integer.parseInt(args[1]);
                    parkourManager.setCheckpointLocation(checkpointNumber, player.getLocation());
                    player.sendMessage("Checkpoint " + checkpointNumber + " added.");
                } catch (NumberFormatException e) {
                    player.sendMessage("The checkpoint number must be an integer.");
                    return false;
                }
                return true;
            default:
                player.sendMessage("Use: /parkour <start|stop|setstart|setend|setcheckpoint>");
                break;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1){
            return Arrays.asList("start", "stop", "setstart", "setend", "setcheckpoint");
        }
        return Collections.emptyList();
    }
}
