package com.solexgames.lib.commons.redis;

import com.solexgames.lib.commons.redis.handler.JedisHandler;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author GrowlyX
 * @since 5/18/2021
 * <p>
 * Builds a new instance of {@link JedisManager}
 */

@Getter
@NoArgsConstructor
public class JedisBuilder {

    private JedisSettings settings;
    private List<JedisHandler> handlers;

    private String channel;

    public JedisBuilder withSettings(JedisSettings settings) {
        this.settings = settings;
        return this;
    }

    public JedisBuilder withHandlers(JedisHandler handler) {
        if (this.handlers == null) {
            this.handlers = Collections.singletonList(handler);
        } else {
            this.handlers.add(handler);
        }

        return this;
    }

    public JedisBuilder withHandlers(JedisHandler... handler) {
        final List<JedisHandler> jedisHandlers = Arrays.asList(handler);

        if (this.handlers == null) {
            this.handlers = jedisHandlers;
        } else {
            this.handlers.addAll(jedisHandlers);
        }

        return this;
    }

    public JedisBuilder withChannel(String channel) {
        this.channel = channel;
        return this;
    }

    @SneakyThrows
    public JedisManager build() {
        if (this.settings == null) {
            throw new RuntimeException("Jedis settings is null");
        }

        return new JedisManager(this.channel == null ? "jedis" : this.channel, this.settings, this.handlers == null ? null : this.handlers);
    }
}
