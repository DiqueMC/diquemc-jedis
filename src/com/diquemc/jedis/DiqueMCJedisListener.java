package com.diquemc.jedis;

public interface DiqueMCJedisListener {
    void onMessage (String channel, String message, String[] parsedMessage);
}
