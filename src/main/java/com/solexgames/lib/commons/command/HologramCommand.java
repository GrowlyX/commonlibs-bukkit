package com.solexgames.lib.commons.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.HelpCommand;
import org.bukkit.command.CommandSender;

/**
 * @author GrowlyX
 * @since 6/26/2021
 */

@CommandPermission("commons.command.hologram")
@CommandAlias("hologram|commonsholograms|holo|holos")
public class HologramCommand extends BaseCommand {

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp commandHelp) {
        commandHelp.showHelp();
    }
}
