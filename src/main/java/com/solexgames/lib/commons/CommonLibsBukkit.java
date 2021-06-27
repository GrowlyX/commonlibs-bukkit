package com.solexgames.lib.commons;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solexgames.lib.commons.command.CommonLibsCommand;
import com.solexgames.lib.commons.manager.HologramManager;
import com.solexgames.lib.commons.redis.JedisManager;
import lombok.Getter;
import me.lucko.helper.hologram.HologramFactory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@Getter
public final class CommonLibsBukkit extends JavaPlugin {

    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

    private HologramManager hologramManager;

    @Override
    public void onEnable() {
        this.getCommand("commonlibs").setExecutor(new CommonLibsCommand());

        this.loadCommonsHolograms();
        this.loadCommonsNpcs();
    }

    private void loadCommonsNpcs() {
    }

    private void loadCommonsHolograms() {
        this.hologramManager = new HologramManager();
        this.hologramManager.saveHologramService(this.hologramManager.loadHologramService());
        this.hologramManager.loadAllAsync();
    }

    @Override
    public void onDisable() {
        JedisManager.INSTANCES.stream().filter(Objects::nonNull)
                .forEach(JedisManager::disconnect);
        
        this.hologramManager.saveAllAsync();
    }
}
