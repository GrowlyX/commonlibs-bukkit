package com.solexgames.lib.commons.manager;

import com.google.common.base.Preconditions;
import com.google.gson.JsonParser;
import com.solexgames.lib.commons.CommonLibsBukkit;
import com.solexgames.lib.commons.hologram.CommonsHologram;
import com.solexgames.lib.commons.npc.CommonsNpc;
import me.lucko.helper.Schedulers;
import me.lucko.helper.Services;
import me.lucko.helper.hologram.Hologram;
import me.lucko.helper.hologram.HologramFactory;
import me.lucko.helper.npc.NpcFactory;
import me.lucko.helper.serialize.Position;
import me.lucko.helper.terminable.composite.CompositeTerminable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GrowlyX
 * @since 6/27/2021
 */

public class NPCManager {

//    private final Map<String, CommonsNpc> hologramMap = new HashMap<>();
//
//    public NpcFactory npcFactory;
//
//    public void saveNpcService(NpcFactory factory) {
//        this.npcFactory = factory;
//    }
//
//    public NpcFactory loadHologramService() {
//        return Services.load(NpcFactory.class);
//    }
//
//    public CommonsNpc formHologram(String identifier, Position position, String nameTag,
//                                   String texture, String signature, String command) {
//        Preconditions.checkNotNull(identifier, "id");
//
//        final CommonsNpc commonsNpc = new CommonsNpc(this.npcFactory.spawnNpc(position.toLocation(), nameTag, texture, signature), command);
//        commonsNpc.getNpc().setClickCallback(player -> {
//            assert command != null;
//            player.performCommand(command);
//        });
//
//        return this.hologramMap.put(identifier, commonsNpc);
//    }
//
//    public void deleteIfPresent(String identifier) {
//        Preconditions.checkNotNull(identifier, "id");
//
//        final CommonsNpc commonsNpc = this.hologramMap.remove(identifier);
//
//
//    }
//
//    public void loadAllAsync() {
//        final CompositeTerminable terminable = CompositeTerminable.create();
//
//        Schedulers.async().run(() -> {
//            final FileConfiguration configuration = CommonLibsBukkit.getInstance().getConfig();
//            final ConfigurationSection section = configuration.getConfigurationSection("holograms");
//
//            if (section != null) {
//                section.getKeys(false).forEach(identifier -> {
//                    final String serialized = section.getString(identifier);
//                    final Hologram helperHologram = this.hologramFactory.deserialize(JsonParser.parseString(serialized));
//
//                    this.hologramMap.put(identifier, new CommonsHologram(helperHologram));
//                });
//            }
//        }).bindWith(terminable);
//
//        terminable.closeAndReportException();
//    }
//
//    public void saveAllAsync() {
//        final CompositeTerminable terminable = CompositeTerminable.create();
//        final CommonLibsBukkit commonLibsBukkit = CommonLibsBukkit.getInstance();
//
//        Schedulers.async().run(() -> {
//            final FileConfiguration configuration = commonLibsBukkit.getConfig();
//
//            this.hologramMap.forEach((identifier, commonsHologram) -> {
//                configuration.set("holograms." + identifier, commonsHologram.getHologram().serialize().toString());
//            });
//
//            commonLibsBukkit.saveConfig();
//        }).bindWith(terminable);
//
//        terminable.closeAndReportException();
//    }
//
//    public CommonsHologram fetchHologram(String identifier) {
//        return this.hologramMap.getOrDefault(identifier, null);
//    }
}
