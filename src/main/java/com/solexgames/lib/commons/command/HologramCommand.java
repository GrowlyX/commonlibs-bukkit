package com.solexgames.lib.commons.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.annotation.*;
import com.solexgames.lib.commons.CommonLibsBukkit;
import com.solexgames.lib.commons.hologram.CommonsHologram;
import me.lucko.helper.serialize.Position;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GrowlyX
 * @since 6/26/2021
 */

@CommandPermission("commons.command.hologram")
@CommandAlias("hologram|commonsholograms|holo|holos")
public class HologramCommand extends BaseCommand {

    @Default
    @HelpCommand
    public void onHelp(CommandHelp commandHelp) {
        commandHelp.showHelp();
    }

    @Subcommand("create|add")
    @Description("Create a new hologram")
    public void onCreate(Player player, String name, List<String> lines) {
        final CommonsHologram hologram = CommonLibsBukkit.getInstance().getHologramManager().fetchHologram(name);

        if (hologram != null) {
            throw new ConditionFailedException("A hologram by that name already exists.");
        }

        CommonLibsBukkit.getInstance().getHologramManager().formHologram(name, Position.of(player.getLocation()), lines);
        player.sendMessage(ChatColor.GREEN + "Created a new hologram by the name \"" + name + "\".");
    }

    @Subcommand("remove|delete")
    @Description("Remove a hologram")
    public void onRemove(Player player, String name) {
        final CommonsHologram hologram = CommonLibsBukkit.getInstance().getHologramManager().fetchHologram(name);

        if (hologram == null) {
            throw new ConditionFailedException("A hologram by that name does not exist.");
        }

        CommonLibsBukkit.getInstance().getHologramManager().deleteIfPresent(name);
        player.sendMessage(ChatColor.RED + "Deleted the \"" + name + "\" hologram.");
    }
}
