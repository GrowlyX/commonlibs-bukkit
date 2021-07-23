package com.solexgames.lib.commons.redis;

import com.solexgames.lib.commons.redis.annotation.Subscription;
import com.solexgames.lib.commons.redis.callback.Callback;
import com.solexgames.lib.commons.redis.exception.InvalidSubscriptionException;
import com.solexgames.lib.commons.redis.handler.JedisHandler;
import com.solexgames.lib.commons.redis.json.JsonAppender;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * @author GrowlyX
 * @since 5/18/2021
 */

@Getter
@Setter
public class JedisManager {

    public static final List<JedisManager> INSTANCES = new ArrayList<>();

    private final HashMap<String, Method> jedisActionHandlers = new HashMap<>();

    private final String channel;
    private final JedisSettings settings;

    private JedisPool jedisPool;
    private JedisHandler jedisHandler;
    private JedisPubSub jedisPubSub;

    public JedisManager(String channel, JedisSettings settings, JedisHandler jedisHandler) throws InvalidSubscriptionException {
        this.settings = settings;
        this.channel = channel;
        this.jedisHandler = jedisHandler;

        this.jedisPool = new JedisPool(this.settings.getHostAddress(), this.settings.getPort());

        if (this.jedisHandler != null) {
            this.registerSubscriptions();
            this.connect();
        }

        JedisManager.INSTANCES.add(this);
    }

    private void connect() {
        this.jedisPubSub = new JedisSubscription(this);

        CompletableFuture.runAsync(() -> {
            this.runCommand(jedis -> {
                try {
                    jedis.subscribe(this.jedisPubSub, this.channel);
                } finally {
                    jedis.connect();
                }

                Logger.getGlobal().info("[Jedis] Now reading on jedis channel \"" + this.channel + "\"");
            });
        });
    }

    public void authenticate(Jedis jedis) {
        if (this.settings.isAuth()) {
            jedis.auth(this.settings.getPassword());
        }
    }

    public void disconnect() {
        try {
            if (this.jedisPool != null) {
                this.jedisPool.close();
            }

            if (this.jedisPubSub != null) {
                this.jedisPubSub.unsubscribe();
            }

            Logger.getGlobal().info("No longer reading on jedis channel " + this.channel);
        } catch (Exception ignored) {
            Logger.getGlobal().info("Something went wrong while trying to disconnect from jedis.");
        }
    }

    public void publish(String json) {
        this.runCommand(jedis -> {
            jedis.publish(this.channel, json);
        });
    }

    @SneakyThrows
    public void runCommand(Callback<Jedis> jedisTCallback) {
        try (final Jedis jedis = this.jedisPool.getResource()) {
            if (this.settings.isAuth()) {
                jedis.auth(this.settings.getPassword());
            }

            jedisTCallback.call(jedis);
        }
    }

    public void registerSubscriptions() throws InvalidSubscriptionException {
        final Method[] methodList = this.jedisHandler.getClass().getMethods();

        for (Method method : methodList) {
            if (method.isAnnotationPresent(Subscription.class)) {
                final Subscription subscription = method.getAnnotation(Subscription.class);

                if (method.getParameterTypes().length > 1) {
                    throw new InvalidSubscriptionException("Handler has more than 1 parameter");
                }

                if (method.getParameterTypes()[0] != JsonAppender.class) {
                    throw new InvalidSubscriptionException("Handler parameter is not JsonAppender");
                }

                if (!method.getName().startsWith("on")) {
                    throw new InvalidSubscriptionException("Handler method does not match with naming conventions (on<incomingPacket>)");
                }

                this.jedisActionHandlers.put(subscription.action(), method);
            }
        }
    }
}
