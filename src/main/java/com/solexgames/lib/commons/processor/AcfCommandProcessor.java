package com.solexgames.lib.commons.processor;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.PaperCommandManager;
import com.solexgames.lib.commons.CommonLibsBukkit;
import com.solexgames.lib.commons.command.context.CommonsPlayer;
import com.solexgames.lib.commons.manager.HologramManager;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * @author GrowlyX
 * @since 6/29/2021
 */

public class AcfCommandProcessor extends PaperCommandManager {

    public AcfCommandProcessor(Plugin plugin) {
        super(plugin);

        this.loadDefaultContexts();
    }

    private void loadDefaultContexts() {
        this.enableUnstableAPI("help");

        this.getCommandContexts().registerContext(CommonsPlayer.class, context -> {
            final String nameRaw = context.popFirstArg();
            final String username = nameRaw.replace(":confirm", "");
            final Player bukkitPlayer = Bukkit.getPlayer(username);

            if (bukkitPlayer != null && bukkitPlayer.hasMetadata("vanished")) {
                if (!context.getSender().hasPermission("scandium.staff")) {
                    throw new ConditionFailedException("No player matching " + ChatColor.YELLOW + username + ChatColor.RED + " is online.");
                } else {
                    if (!nameRaw.endsWith(":confirm")) {
                        throw new ConditionFailedException("That player's vanished, please add :confirm to the end of the user's name to confirm this action.");
                    }
                }
            }

            return new CommonsPlayer(bukkitPlayer);
        });

        this.setDefaultExceptionHandler((command, registeredCommand, sender, args, t) -> {
            sender.sendMessage(ChatColor.RED + "Something went terrible wrong if this command just executed.");
            return false;
        });

        this.registerDependency(HologramManager.class, CommonLibsBukkit.getInstance().getHologramManager());
    }

    @SneakyThrows
    private void registerCommand(Class<?> clazz) {
        final Object object = clazz.newInstance();

        if (object instanceof BaseCommand) {
            final BaseCommand baseCommand = (BaseCommand) object;

            this.registerCommand(baseCommand);
        }
    }
}
