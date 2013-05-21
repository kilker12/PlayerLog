package com.craftrealms.playerlog;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandTest implements CommandExecutor {
    PlayerLog p;
    public CommandTest(PlayerLog plugin) {
        p = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(p.server.playersockets.containsKey(strings[0])) {
            p.server.playersockets.get(strings[0]).sendMessage("Haeillsdf");
            return true;
        } else {
            sender.sendMessage("Player is not connected through staff client!");
            return true;
        }
    }
}
