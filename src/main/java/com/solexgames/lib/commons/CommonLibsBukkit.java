package com.solexgames.lib.commons;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solexgames.lib.commons.command.CommonLibsCommand;
import com.solexgames.lib.commons.command.HologramCommand;
import com.solexgames.lib.commons.listener.NpcListener;
import com.solexgames.lib.commons.manager.HologramManager;
import com.solexgames.lib.commons.manager.NPCManager;
import com.solexgames.lib.commons.processor.AcfCommandProcessor;
import com.solexgames.lib.commons.redis.JedisManager;
import lombok.Getter;
import me.lucko.helper.Helper;
import me.lucko.helper.hologram.BukkitHologramFactory;
import me.lucko.helper.hologram.HologramFactory;
import me.lucko.helper.messaging.bungee.BungeeCord;
import me.lucko.helper.messaging.bungee.BungeeCordImpl;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import me.lucko.helper.plugin.HelperPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@Getter
public final class CommonLibsBukkit extends ExtendedJavaPlugin {

    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

    @Getter
    private static CommonLibsBukkit instance;

    private HologramManager hologramManager;
    private NPCManager npcManager;

    @Override
    public void enable() {
        instance = this;

        this.saveDefaultConfig();
        this.loadCommonsServices();
        this.loadCommonsHolograms();
//        this.loadCommonsNpcs();
        this.loadCommonsCommands();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    private void loadCommonsServices() {
        Bukkit.getServicesManager().register(HologramFactory.class, new BukkitHologramFactory(), this, ServicePriority.High);
        Bukkit.getServicesManager().register(BungeeCord.class, new BungeeCordImpl(Helper.hostPlugin()), this, ServicePriority.High);
    }

    private void loadCommonsCommands() {
        final AcfCommandProcessor acfCommandProcessor = new AcfCommandProcessor(this);

        acfCommandProcessor.enableUnstableAPI("help");
        acfCommandProcessor.registerCommand(new CommonLibsCommand());
        acfCommandProcessor.registerCommand(new HologramCommand());
    }

    private void loadCommonsNpcs() {
        this.npcManager = new NPCManager();
        this.npcManager.saveNpcService(this.npcManager.loadNpcService(this));
        this.npcManager.loadAllAsync();

        this.getServer().getPluginManager().registerEvents(new NpcListener(), this);
    }

    private void loadCommonsHolograms() {
        this.hologramManager = new HologramManager();
        this.hologramManager.saveHologramService(this.hologramManager.loadHologramService());
        this.hologramManager.loadAllAsync();
    }

    @Override
    public void disable() {
        JedisManager.INSTANCES.stream().filter(Objects::nonNull)
                .forEach(JedisManager::disconnect);

        this.hologramManager.saveAllSync();
    }
}
