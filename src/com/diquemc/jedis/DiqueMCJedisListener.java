package com.diquemc.jedis;

public interface DiqueMCJedisListener {
    public void onMessage (String channel, String message, String[] parsedMessage);
}
