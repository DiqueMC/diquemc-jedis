package com.diquemc.jedis;

import redis.clients.jedis.JedisPool;

public interface DiqueMCJedisServer {
    public void scheduleAsync ( Runnable runnable);
    public void scheduleAsync ( Runnable runnable, Long delay);
    public void log (String message);
}