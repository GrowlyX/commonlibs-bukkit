package com.solexgames.lib.commons.npc;

import com.solexgames.lib.commons.CommonLibsBukkit;
import com.solexgames.lib.commons.util.BungeeUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

/**
 * @author GrowlyX
 * @since 6/28/2021
 */

@Getter
@RequiredArgsConstructor
public enum CommonsNpcClickCallbackType {

    DEFAULT(null),
    COMMAND((commonsNpc, player) -> {
        player.performCommand(commonsNpc.getCallbackData());
    }),
    SEND_MESSAGE((commonsNpc, player) -> {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', commonsNpc.getCallbackData()));
    }),
    SEND_SERVER((commonsNpc, player) -> {
        player.sendMessage(ChatColor.YELLOW + "You're being connected to " + ChatColor.GOLD + commonsNpc.getCallbackData() + ChatColor.YELLOW + "...");
        BungeeUtil.sendToServer(player, commonsNpc.getCallbackData(), CommonLibsBukkit.getInstance());
    }),
    PLAYER_CHAT((commonsNpc, player) -> {
        player.chat(commonsNpc.getCallbackData());
    }),

    HANDLE_EXCEPTION((commonsNpc, player) -> {
        player.chat(ChatColor.RED + "Error: Something went wrong while trying to perform an action. (" + commonsNpc.getCallbackData() + ") (" + commonsNpc.getNpc().getId() + ")");
    });

    private final BiConsumer<CommonsNpc, Player> consumer;

}
