package com.solexgames.lib.commons.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.annotation.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.solexgames.lib.commons.CommonLibsBukkit;
import com.solexgames.lib.commons.command.annotation.Service;
import com.solexgames.lib.commons.npc.CommonsNpc;
import com.solexgames.lib.commons.npc.CommonsNpcClickCallbackType;
import me.lucko.helper.serialize.Position;
import net.jitse.npclib.api.skin.Skin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author GrowlyX
 * @since 6/28/2021
 */

@Service(active = false)
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

    @Subcommand("create|add")
    @Description("Create a new npc")
    public void onCreate(Player player, String name, String skinPlayer, CommonsNpcClickCallbackType callbackType, String callbackData, String[] lines) {
        final CommonsNpc npc = CommonLibsBukkit.getInstance().getNpcManager().fetchNpc(name);

        if (npc != null) {
            throw new ConditionFailedException("An npc by that name already exists.");
        }

        final List<String> stringList = new ArrayList<>(lines.length > 0 ? Arrays.asList(lines) : this.defaultLines);
        final Skin skin = this.getDisguiseData(this.fetchUUID(skinPlayer));

        if (skin == null) {
            throw new ConditionFailedException("A player by that name does not exist.");
        }

        CommonLibsBukkit.getInstance().getNpcManager().formNpc(name, Position.of(player.getLocation()), stringList, skin.getValue(), skin.getSignature(), callbackData, callbackType);

        player.sendMessage(ChatColor.YELLOW + "You've created a new npc by the name " + ChatColor.GOLD + name + ChatColor.YELLOW + ".");
    }

    @Subcommand("remove|delete")
    @Description("Remove an npc")
    public void onRemove(Player player, String name) {
        final CommonsNpc npc = CommonLibsBukkit.getInstance().getNpcManager().fetchNpc(name);

        if (npc != null) {
            throw new ConditionFailedException("An npc by that name already exists.");
        }

        CommonLibsBukkit.getInstance().getNpcManager().deleteIfPresent(name);

        player.sendMessage(ChatColor.YELLOW + "You've deleted an npc by the name " + ChatColor.GOLD + name + ChatColor.YELLOW + ".");
    }


    /**
     * Fetch a player's skin
     * <p>
     *
     * @param uuid UUID of the target player's skin.
     */
    public Skin getDisguiseData(UUID uuid) {
        try {
            final URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", "") + "?unsigned=false");
            final JsonObject json = new JsonParser().parse(new InputStreamReader(url.openStream())).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();

            final String skin = json.get("value").getAsString();
            final String signature = json.get("signature").getAsString();

            return new Skin(skin, signature);
        } catch (Exception exception) {
            return null;
        }
    }

    public UUID fetchUUID(String name) {
        try {
            final URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            final JsonObject json = new JsonParser().parse(new InputStreamReader(url.openStream())).getAsJsonObject();
            final String uuid = json.get("id").toString().replace("\"", "");

            return this.formatUUID(uuid);
        } catch (Exception ignored) {
            return null;
        }
    }

    public UUID formatUUID(String id) {
        return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
    }
}
