package com.diquemc.jedis;

import redis.clients.jedis.JedisPubSub;


public class DiqueMCJedisPubSub extends JedisPubSub {

    private final String separator;
    private final DiqueMCJedisListener listener;

    public void subscribeToChannel (String channel) {
        DiqueMCJedis.subscribe(this,channel);
    }

    private DiqueMCJedisPubSub(DiqueMCJedisListener listener, String separator) {
        this.listener = listener;
        this.separator = separator;
    }

    public DiqueMCJedisPubSub(DiqueMCJedisListener listener) {
        this(listener, "\r");
//        this.listener = listener;
//        this.separator = "\r";
    }

    public void onMessage(String channel, String message) {
        try {
            String[] parsedMessage = message.split(separator);
            listener.onMessage(channel, message, parsedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
