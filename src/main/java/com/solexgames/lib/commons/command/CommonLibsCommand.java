package com.solexgames.lib.commons.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.solexgames.lib.commons.command.annotation.Service;
import com.solexgames.lib.commons.redis.JedisManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 5/30/2021
 */

@Service
@CommandAlias("commonlibs|lib")
public class CommonLibsCommand extends BaseCommand {

    @Default
    public void onDefault(Player player) {
        player.sendMessage(ChatColor.RED + "This server is currently running " + ChatColor.YELLOW + JedisManager.INSTANCES.size() + ChatColor.RED + " instances.");
    }
}
