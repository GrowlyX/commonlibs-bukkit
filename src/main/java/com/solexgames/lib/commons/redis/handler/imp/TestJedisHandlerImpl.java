package com.solexgames.lib.commons.redis.handler.imp;

import com.solexgames.lib.commons.redis.JedisBuilder;
import com.solexgames.lib.commons.redis.JedisManager;
import com.solexgames.lib.commons.redis.JedisSettings;
import com.solexgames.lib.commons.redis.annotation.Subscription;
import com.solexgames.lib.commons.redis.handler.JedisHandler;
import com.solexgames.lib.commons.redis.json.JsonAppender;

/**
 * @author GrowlyX
 * @since 6/29/2021
 */

public class TestJedisHandlerImpl implements JedisHandler
{

    public TestJedisHandlerImpl()
    {
        final JedisManager jedisManager = new JedisBuilder()
                .withChannel("channel")
                .withSettings(new JedisSettings(
                        "127.0.0.1",
                        6379, true,
                        "password"
                ))
                .withHandlers(this)
                .build();
    }

    @Subscription(action = "action")
    public void onAction(JsonAppender data)
    {
        final String something = data.getParam("yes");

        System.out.println(something);
    }
}
