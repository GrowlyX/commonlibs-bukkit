package com.solexgames.lib.commons.command;

import com.solexgames.lib.commons.redis.JedisManager;
import lombok.SneakyThrows;
import me.lucko.helper.Services;
import me.lucko.helper.hologram.Hologram;
import me.lucko.helper.hologram.HologramFactory;
import me.lucko.helper.serialize.Position;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Collections;

/**
 * @author GrowlyX
 * @since 5/30/2021
 */

public class CommonLibsCommand implements CommandExecutor {

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("commons.command.commonlibs")) {
            sender.sendMessage(ChatColor.RED + "This server is currently running " + ChatColor.YELLOW + JedisManager.INSTANCES.size() + ChatColor.RED + " instances.");
        } else {
            sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command.");
        }

        return false;
    }
}
