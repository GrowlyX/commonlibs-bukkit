package com.solexgames.lib.commons.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;

import java.util.Arrays;
import java.util.List;

/**
 * @author GrowlyX
 * @since 6/28/2021
 */

@CommandAlias("npc|commonsnpcs|npcs")
@CommandPermission("commons.command.hologram")
public class NpcCommand extends BaseCommand {

    private final List<String> defaultLines = Arrays.asList(
            "&6&lCommonLibs Npcs",
            "&7use /npc to view line commands..."
    );

    @Default
    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

    // TODO: 6/28/2021 add these commands
    // /npc create
    // /npc remove
    // /npc setlines
    // /npc setcallbackdata
    // /npc setcallbacktype
    // etc..

}
