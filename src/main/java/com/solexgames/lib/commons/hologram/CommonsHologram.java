package com.solexgames.lib.commons.hologram;

import com.google.gson.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.lucko.helper.hologram.Hologram;
import me.lucko.helper.scheduler.threadlock.ServerThreadLock;
import me.lucko.helper.serialize.Position;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author GrowlyX
 * @since 6/26/2021
 */

@Getter
@RequiredArgsConstructor
public class CommonsHologram {

    private final String name;
    private final Hologram hologram;

    private Position position;

    private List<String> lines = new ArrayList<>();

    public void remove() {
        this.hologram.despawn();
    }

    public void spawn() {
        this.hologram.spawn();
    }

    public void setPosition(Position position) {
        this.position = position;
        this.updateLines();
    }

    public void addLine(String line) {
        this.lines.add(line);
        this.updateLines();
    }

    public void setLine(int index, String line) {
        this.lines.set(index, line);
        this.updateLines();
    }

    public void removeLine(int index) {
        this.lines.remove(index);
        this.updateLines();
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
        this.updateLines();
    }

    private void updateLines() {
        this.hologram.updateLines(this.translate(this.lines));

        try (ServerThreadLock serverThreadLock = ServerThreadLock.obtain()) {
            try {
                this.remove();
                this.spawn();
            } catch (Exception ignored) { }
        }
    }

    public List<String> translate(List<String> text) {
        final List<String> newList = new ArrayList<>();

        for (String string : text) {
            newList.add(ChatColor.translateAlternateColorCodes('&', string));
        }

        return newList;
    }

    public Location getLocation() {
        return this.position.toLocation();
    }
}
