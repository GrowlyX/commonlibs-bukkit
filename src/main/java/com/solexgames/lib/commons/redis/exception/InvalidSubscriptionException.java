package com.solexgames.lib.commons.redis.exception;

public class InvalidSubscriptionException extends Exception {

    public InvalidSubscriptionException(String reason) {
        super(reason);
    }
}
