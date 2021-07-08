package com.solexgames.lib.commons.command.context;

import lombok.Data;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 7/8/2021
 */

@Data
public class CommonsPlayer {

    private final Player player;

    public boolean isVanished() {
        return this.player.hasMetadata("vanished");
    }

    @Override
    public boolean equals(Object object) {
        try {
            final CommonsPlayer commonsPlayer = (CommonsPlayer) object;

            return commonsPlayer.getPlayer().getUniqueId().equals(this.player.getUniqueId());
        } catch (Exception ignored) {
            return false;
        }
    }
}
