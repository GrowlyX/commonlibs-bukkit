package com.solexgames.lib.commons.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.annotation.*;
import com.solexgames.lib.commons.CommonLibsBukkit;
import com.solexgames.lib.commons.hologram.CommonsHologram;
import com.solexgames.lib.commons.manager.HologramManager;
import me.lucko.helper.serialize.Position;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author GrowlyX
 * @since 6/26/2021
 */

@CommandPermission("commons.command.hologram")
@CommandAlias("hologram|commonsholograms|holo|holos")
public class HologramCommand extends BaseCommand {

    private final List<String> defLines = Arrays.asList(
            "&6&lCommonLibs Holograms",
            "&7use /holo to view line commands..."
    );

    @Default
    @HelpCommand
    public void onHelp(CommandHelp commandHelp) {
        commandHelp.showHelp();
    }

    @Subcommand("create|add")
    @Description("Create a new hologram")
    public void onCreate(Player player, String name, String[] lines) {
        final CommonsHologram hologram = CommonLibsBukkit.getInstance().getHologramManager().fetchHologram(name);

        if (hologram != null) {
            throw new ConditionFailedException("A hologram by that name already exists.");
        }

        final List<String> stringList = new ArrayList<>(lines.length > 0 ? Arrays.asList(lines) : this.defLines);

        CommonLibsBukkit.getInstance().getHologramManager().formHologram(name, Position.of(player.getLocation()), stringList);

        player.sendMessage(ChatColor.YELLOW + "You've created a new hologram by the name " + ChatColor.GOLD + name + ChatColor.YELLOW + ".");
    }

    @Subcommand("remove|delete")
    @Description("Remove a hologram")
    public void onRemove(Player player, String name) {
        final CommonsHologram hologram = CommonLibsBukkit.getInstance().getHologramManager().fetchHologram(name);

        if (hologram == null) {
            throw new ConditionFailedException("A hologram by that name does not exist.");
        }

        CommonLibsBukkit.getInstance().getHologramManager().deleteIfPresent(name);

        player.sendMessage(ChatColor.YELLOW + "You've deleted a hologram by the name " + ChatColor.GOLD + name + ChatColor.YELLOW + ".");
    }

    @Subcommand("addline")
    @Description("Add hologram lines")
    public void onAddLine(Player player, String name, String[] line) {
        final CommonsHologram hologram = CommonLibsBukkit.getInstance().getHologramManager().fetchHologram(name);

        if (hologram == null) {
            throw new ConditionFailedException("A hologram by that name does not exist.");
        }

        final String joinedLine = String.join(" ", line);
        hologram.addLine(joinedLine);

        player.sendMessage(ChatColor.YELLOW + "You've added a line " + ChatColor.GOLD + joinedLine + ChatColor.YELLOW + " to the hologram by the name " + ChatColor.GOLD + name + ChatColor.YELLOW + ".");
    }

    @Subcommand("setline")
    @Description("Set hologram lines")
    public void onSetLine(Player player, String name, int index, String[] line) {
        final CommonsHologram hologram = CommonLibsBukkit.getInstance().getHologramManager().fetchHologram(name);

        if (hologram == null) {
            throw new ConditionFailedException("A hologram by that name does not exist.");
        }

        final String joinedLine = String.join(" ", line);
        hologram.setLine(index, joinedLine);

        player.sendMessage(ChatColor.YELLOW + "You've set the line " + ChatColor.GOLD + joinedLine + " - " + index + ChatColor.YELLOW + " to the hologram by the name " + ChatColor.GOLD + name + ChatColor.YELLOW + ".");
    }

    @Subcommand("removeline")
    @Description("Remove hologram lines")
    public void onRemoveLine(Player player, String name, Integer index) {
        final CommonsHologram hologram = CommonLibsBukkit.getInstance().getHologramManager().fetchHologram(name);

        if (hologram == null) {
            throw new ConditionFailedException("A hologram by that name does not exist.");
        }

        hologram.removeLine(index);

        player.sendMessage(ChatColor.YELLOW + "You've removed the line " + ChatColor.GOLD + index + ChatColor.YELLOW + " to the hologram by the name " + ChatColor.GOLD + name + ChatColor.YELLOW + ".");
    }

    @Subcommand("listlines")
    @Description("List hologram lines")
    public void onListLines(Player player, String name) {
        final CommonsHologram hologram = CommonLibsBukkit.getInstance().getHologramManager().fetchHologram(name);

        if (hologram == null) {
            throw new ConditionFailedException("A hologram by that name does not exist.");
        }

        player.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "Lines:");
        hologram.getLines().forEach(line -> {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.GRAY + " - " + line));
        });
    }

    @Subcommand("list")
    @Description("List holograms")
    public void onList(Player player) {
        final HologramManager hologramManager = CommonLibsBukkit.getInstance().getHologramManager();

        if (hologramManager.getHologramMap().isEmpty()) {
            throw new ConditionFailedException("No holograms were found.");
        }

        player.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "Holograms:");
        hologramManager.getHologramMap().forEach((string, hologram) -> {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.GRAY + " - " + hologram.getName()));
        });
    }

    @Subcommand("save")
    @Description("Save holograms")
    public void onSave(Player player) {
        final HologramManager hologramManager = CommonLibsBukkit.getInstance().getHologramManager();

        if (hologramManager.getHologramMap().isEmpty()) {
            throw new ConditionFailedException("No holograms were found.");
        }

        hologramManager.saveAllSync();
    }
}
