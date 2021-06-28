package com.solexgames.lib.commons.listener;

import com.solexgames.lib.commons.CommonLibsBukkit;
import com.solexgames.lib.commons.npc.CommonsNpc;
import com.solexgames.lib.commons.npc.CommonsNpcClickCallbackType;
import net.jitse.npclib.api.events.NPCInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author GrowlyX
 * @since 6/28/2021
 */

public class NpcListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        CommonLibsBukkit.getInstance().getNpcManager().getNpcMap().forEach((s, commonsNpc) -> {
            commonsNpc.getNpc().show(event.getPlayer());
        });
    }

    @EventHandler
    public void onInteract(NPCInteractEvent event) {
        final CommonsNpc commonsNpc = CommonLibsBukkit.getInstance().getNpcManager().fetchNpcFromInternalId(event.getNPC().getId());
        final Player player = event.getWhoClicked();

        if (commonsNpc != null) {
            if (commonsNpc.getCallbackType() == CommonsNpcClickCallbackType.DEFAULT) {
                return;
            }

            try {
                commonsNpc.getCallbackType().getConsumer().accept(commonsNpc, player);
            } catch (Exception ignored) {
                CommonsNpcClickCallbackType.HANDLE_EXCEPTION.getConsumer().accept(commonsNpc, player);
            }
        }
    }
}
