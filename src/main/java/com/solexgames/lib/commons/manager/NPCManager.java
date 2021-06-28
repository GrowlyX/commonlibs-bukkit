package com.solexgames.lib.commons.manager;

import com.google.common.base.Preconditions;
import com.google.gson.JsonParser;
import com.solexgames.lib.commons.CommonLibsBukkit;
import com.solexgames.lib.commons.npc.CommonsNpc;
import com.solexgames.lib.commons.npc.CommonsNpcClickCallbackType;
import lombok.Getter;
import me.lucko.helper.Schedulers;
import me.lucko.helper.serialize.Position;
import me.lucko.helper.terminable.composite.CompositeTerminable;
import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.skin.Skin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GrowlyX
 * @since 6/27/2021
 */

@Getter
public class NPCManager {

    private final Map<String, CommonsNpc> npcMap = new HashMap<>();

    private NPCLib npcFactory;

    public void saveNpcService(NPCLib factory) {
        this.npcFactory = factory;
    }

    public NPCLib loadNpcService(JavaPlugin plugin) {
        return new NPCLib(plugin);
    }

    public CommonsNpc formNpc(String identifier, Position position, List<String> nameTag,
                              String texture, String signature, String data, CommonsNpcClickCallbackType type) {
        Preconditions.checkNotNull(identifier, "id");

        final CommonsNpc commonsNpc = new CommonsNpc(this.npcFactory.createNPC(nameTag), data, type, new Skin(texture, signature));

        commonsNpc.getNpc().setLocation(position.toLocation());
        commonsNpc.getNpc().setSkin(new Skin(texture, signature));
        commonsNpc.getNpc().create();

        Bukkit.getOnlinePlayers().forEach(player -> {
            commonsNpc.getNpc().show(player);
        });

        return this.npcMap.put(identifier, commonsNpc);
    }

    public void deleteIfPresent(String identifier) {
        Preconditions.checkNotNull(identifier, "id");

        final CommonsNpc commonsNpc = this.npcMap.remove(identifier);

        Bukkit.getOnlinePlayers().forEach(player -> {
            commonsNpc.getNpc().hide(player);
        });

        commonsNpc.getNpc().destroy();
    }

    public void loadAllAsync() {
        final CompositeTerminable terminable = CompositeTerminable.create();
        final JsonParser jsonParser = new JsonParser();

        Schedulers.async().run(() -> {
            final FileConfiguration configuration = CommonLibsBukkit.getInstance().getConfig();
            final ConfigurationSection section = configuration.getConfigurationSection("npcs");

            if (section != null) {
                section.getKeys(false).forEach(identifier -> {
                    final List<String> lines = section.getStringList(identifier + ".lines");
                    final Position position = Position.deserialize(jsonParser.parse(section.getString(identifier + ".position")));

                    final String callbackData = section.getString(identifier + ".callback.data");
                    final CommonsNpcClickCallbackType callbackType = CommonsNpcClickCallbackType.valueOf(section.getString(identifier + ".callback.data"));

                    final String skinTexture = section.getString(identifier + ".skin.texture");
                    final String skinSignature = section.getString(identifier + ".skin.signature");

                    final CommonsNpc commonsNpc = new CommonsNpc(this.npcFactory.createNPC(lines), callbackData, callbackType, new Skin(skinTexture, skinSignature));

                    commonsNpc.getNpc().setLocation(position.toLocation());
                    commonsNpc.getNpc().setSkin(commonsNpc.getSkin());
                    commonsNpc.getNpc().create();

                    this.npcMap.put(identifier, commonsNpc);
                });
            }
        }).bindWith(terminable);

        terminable.closeAndReportException();
    }

    public void saveAllAsync() {
        final CompositeTerminable terminable = CompositeTerminable.create();
        final CommonLibsBukkit commonLibsBukkit = CommonLibsBukkit.getInstance();

        Schedulers.async().run(() -> {
            final FileConfiguration configuration = commonLibsBukkit.getConfig();

            this.npcMap.forEach((identifier, commonsHologram) -> {
                configuration.set("npcs." + identifier + ".lines", commonsHologram.getNpc().getText());
                configuration.set("npcs." + identifier + ".position", Position.of(commonsHologram.getNpc().getLocation()).serialize());
                configuration.set("npcs." + identifier + ".skin.texture", commonsHologram.getSkin().getValue());
                configuration.set("npcs." + identifier + ".skin.signature", commonsHologram.getSkin().getSignature());
                configuration.set("npcs." + identifier + ".callback.data", commonsHologram.getCallbackData());
                configuration.set("npcs." + identifier + ".callback.type", commonsHologram.getCallbackType().name());
            });

            commonLibsBukkit.saveConfig();
        }).bindWith(terminable);

        terminable.closeAndReportException();
    }

    public CommonsNpc fetchNpcFromInternalId(String identifier) {
        return this.npcMap.values().stream()
                .filter(commonsNpc -> commonsNpc.getNpc().getId().equals(identifier))
                .findFirst().orElse(null);
    }

    public CommonsNpc fetchNpc(String identifier) {
        return this.npcMap.getOrDefault(identifier, null);
    }
}
