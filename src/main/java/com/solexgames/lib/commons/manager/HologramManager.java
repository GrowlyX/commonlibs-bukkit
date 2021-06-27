package com.solexgames.lib.commons.manager;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.solexgames.lib.commons.CommonLibsBukkit;
import com.solexgames.lib.commons.hologram.CommonsHologram;
import me.lucko.helper.Schedulers;
import me.lucko.helper.Services;
import me.lucko.helper.hologram.Hologram;
import me.lucko.helper.hologram.HologramFactory;
import me.lucko.helper.serialize.Position;
import me.lucko.helper.terminable.composite.CompositeTerminable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GrowlyX
 * @since 6/26/2021
 */

public class HologramManager {

    private final Map<String, CommonsHologram> hologramMap = new HashMap<>();

    public HologramFactory hologramFactory;

    public void saveHologramService(HologramFactory factory) {
        this.hologramFactory = factory;
    }

    public HologramFactory loadHologramService() {
        return Services.load(HologramFactory.class);
    }

    public CommonsHologram formHologram(String identifier, Position position, List<String> lines) {
        Preconditions.checkNotNull(identifier, "id");

        final Hologram hologram = this.hologramFactory.newHologram(position, lines);

        return this.hologramMap.put(identifier, new CommonsHologram(hologram));
    }

    public void deleteIfPresent(String identifier) {
        Preconditions.checkNotNull(identifier, "id");

        this.hologramMap.remove(identifier);
    }

    public void loadAllAsync() {
        final CompositeTerminable terminable = CompositeTerminable.create();

        Schedulers.async().run(() -> {
            final FileConfiguration configuration = CommonLibsBukkit.getPlugin(CommonLibsBukkit.class).getConfig();
            final ConfigurationSection section = configuration.getConfigurationSection("holograms");
            final JsonParser parser = new JsonParser();

            if (section != null) {
                section.getKeys(false).forEach(identifier -> {
                    final String serialized = section.getString(identifier);
                    final Hologram helperHologram = this.hologramFactory.deserialize(parser.parse(serialized));

                    this.hologramMap.put(identifier, new CommonsHologram(helperHologram));
                });
            }
        }).bindWith(terminable);

        terminable.closeAndReportException();
    }

    public void saveAllAsync() {
        final CompositeTerminable terminable = CompositeTerminable.create();
        final CommonLibsBukkit commonLibsBukkit = CommonLibsBukkit.getPlugin(CommonLibsBukkit.class);

        Schedulers.async().run(() -> {
            final FileConfiguration configuration = commonLibsBukkit.getConfig();

            this.hologramMap.forEach((identifier, commonsHologram) -> {
                configuration.set("holograms." + identifier, commonsHologram.getHologram().serialize().toString());
            });

            commonLibsBukkit.saveConfig();
        }).bindWith(terminable);

        terminable.closeAndReportException();
    }

    public CommonsHologram fetchHologram(String identifier) {
        return this.hologramMap.getOrDefault(identifier, null);
    }
}
