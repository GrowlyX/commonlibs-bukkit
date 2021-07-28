package com.solexgames.lib.commons.redis.exception;

import java.lang.reflect.Method;

public class InvalidSubscriptionException extends Exception {

    public InvalidSubscriptionException(String reason, Method method) {
        super(reason + " (" + method.getDeclaringClass().getName() + "#" + method.getName() + ")");
    }
}
