package com.solexgames.lib.commons.npc;

import lombok.Data;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.skin.Skin;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * @author GrowlyX
 * @since 6/27/2021
 */

@Data
public class CommonsNpc {

    private final NPC npc;

    private final String callbackData;
    private final CommonsNpcClickCallbackType callbackType;

    private final Skin skin;

}
