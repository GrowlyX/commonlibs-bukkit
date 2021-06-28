package com.solexgames.lib.commons;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solexgames.lib.commons.command.CommonLibsCommand;
import com.solexgames.lib.commons.command.HologramCommand;
import com.solexgames.lib.commons.manager.HologramManager;
import com.solexgames.lib.commons.redis.JedisManager;
import lombok.Getter;
import me.lucko.helper.hologram.BukkitHologramFactory;
import me.lucko.helper.hologram.HologramFactory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@Getter
public final class CommonLibsBukkit extends JavaPlugin {

    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

    @Getter
    private static CommonLibsBukkit instance;

    private HologramManager hologramManager;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        this.loadCommonsHolograms();
        this.loadCommonsNpcs();
        this.loadCommonsCommands();
    }

    private void loadCommonsCommands() {
        this.getCommand("commonlibs").setExecutor(new CommonLibsCommand());

        final PaperCommandManager paperCommandManager = new PaperCommandManager(this);

        paperCommandManager.enableUnstableAPI("help");
        paperCommandManager.registerCommand(new HologramCommand());
    }

    private void loadCommonsNpcs() {
    }

    private void loadCommonsHolograms() {
        Bukkit.getServicesManager().register(HologramFactory.class, new BukkitHologramFactory(), this, ServicePriority.High);

        this.hologramManager = new HologramManager();
        this.hologramManager.saveHologramService(this.hologramManager.loadHologramService());
        this.hologramManager.loadAllAsync();
    }

    @Override
    public void onDisable() {
        JedisManager.INSTANCES.stream().filter(Objects::nonNull)
                .forEach(JedisManager::disconnect);

        this.hologramManager.saveAllSync();
    }
}
