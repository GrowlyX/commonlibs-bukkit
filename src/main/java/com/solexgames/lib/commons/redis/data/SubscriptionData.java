package com.solexgames.lib.commons.redis.data;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author GrowlyX
 * @since 7/27/2021
 */

@Data
public class SubscriptionData {

    private final Map<Class<?>, Method> methodMap = new HashMap<>();

}
