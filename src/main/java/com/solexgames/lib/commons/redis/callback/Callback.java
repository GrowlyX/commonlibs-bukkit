package com.solexgames.lib.commons.redis.callback;

/**
 * @author GrowlyX
 * @since 7/19/2021
 */

public interface Callback<T> {

    void call(T t);

}
