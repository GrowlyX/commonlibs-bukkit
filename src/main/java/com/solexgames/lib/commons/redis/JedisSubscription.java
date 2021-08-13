package com.solexgames.lib.commons.redis;

import com.solexgames.lib.commons.CommonLibsBukkit;
import com.solexgames.lib.commons.redis.json.JsonAppender;
import lombok.RequiredArgsConstructor;
import redis.clients.jedis.JedisPubSub;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * @author GrowlyX
 * @since 5/18/2021
 * <p>
 * Handles all incoming packets from the jedis subscription
 * defined in the {@link JedisManager} instance.
 */

@RequiredArgsConstructor
public class JedisSubscription extends JedisPubSub {

    private final JedisManager jedisManager;

    @Override
    public void onMessage(String channel, String message) {
        CompletableFuture.runAsync(() -> {
            final JsonAppender jsonAppender = CommonLibsBukkit.GSON.fromJson(message, JsonAppender.class);
            final String redisAction = jsonAppender.getPacket();
            final Method method = this.jedisManager.getJedisActionHandlers().get(redisAction);

            if (method != null) {
                try {
                    if (jsonAppender != null) {
                        method.invoke(this.jedisManager.getJedisHandlers().get(0) /* temporary */, jsonAppender);
                    }
                } catch (Exception exception) {
                    if (this.jedisManager.isPostExceptions()) {
                        exception.printStackTrace();

                        Logger.getGlobal().severe("Couldn't handle this packet: " + redisAction);
                    } else {
                        this.jedisManager.getCollectedExceptions().add(exception);
                    }
                }
            } else {
                Logger.getGlobal().severe("Couldn't handle this packet as a handler does not exist: " + redisAction);
            }
        });
    }
}
