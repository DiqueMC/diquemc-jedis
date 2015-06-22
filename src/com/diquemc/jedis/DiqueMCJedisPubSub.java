package com.diquemc.jedis;

import redis.clients.jedis.JedisPubSub;


public class DiqueMCJedisPubSub extends JedisPubSub {

    private final String separator;
    private final DiqueMCJedisListener listener;

    public void subscribeToChannel (String channel) {
        DiqueMCJedis.subscribe(this,channel);
    }

    public DiqueMCJedisPubSub(DiqueMCJedisListener listener, String separator) {
        this.listener = listener;
        this.separator = separator;
    }

    public DiqueMCJedisPubSub(DiqueMCJedisListener listener) {
        this.listener = listener;
        this.separator = "\r";
    }

    public void onMessage(String channel, String message) {
        String[] parsedMessage = message.split("\r");
        listener.onMessage(channel, message, parsedMessage);
    }

    public void onSubscribe(String channel, int subscribedChannels) {
    }

    public void onUnsubscribe(String channel, int subscribedChannels) {
    }

    public void onPSubscribe(String pattern, int subscribedChannels) {
    }

    public void onPUnsubscribe(String pattern, int subscribedChannels) {
    }

    public void onPMessage(String pattern, String channel, String message) {
    }
}
