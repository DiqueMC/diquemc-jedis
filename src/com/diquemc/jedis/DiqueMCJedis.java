package com.diquemc.jedis;

import net.md_5.bungee.api.ChatColor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

@SuppressWarnings("unused")
public abstract class DiqueMCJedis {
    public static DiqueMCJedisServer server;
    public static JedisPool jedisPool = null;


    public static String get(String key){
        try (Jedis j = jedisPool.getResource()) {
            return j.get(key);
        }
    }

    public static String set(String key, String value){
        try (Jedis j = jedisPool.getResource()) {
            return j.set(key,value);
        }
    }

    public static boolean exists(String key){
        try (Jedis j = jedisPool.getResource()) {
            return j.exists(key);
        }
    }

    public static void initializePool() {
        if(jedisPool == null) {
            jedisPool = new JedisPool("localhost");
        }

    }

    public static void destroyPool() {
        if(jedisPool != null){
            jedisPool.destroy();
            jedisPool = null;
        }
    }

    @SuppressWarnings("unused")
    public static void publish (final String channel, final String message){
        server.scheduleAsync(() -> {
            Jedis jedis = new Jedis("localhost");
            jedis.publish(channel, message);
            jedis.quit();
        });
    }

    public static void subscribeDelayed (final JedisPubSub listener, final String channel, Long delay){
        server.scheduleAsync(() -> {
            Jedis jedis = new Jedis("localhost");
            try {
                jedis.subscribe(listener,channel);
            }catch (Exception e) {
                server.log(ChatColor.RED + "Failed to RECONECTING");
                subscribeDelayed(listener,channel, delay);
            }
            jedis.quit();
        },delay);

    }

    @SuppressWarnings("unused")
    public static void subscribe (final JedisPubSub listener, final String channel){
        server.scheduleAsync(() -> {
            Jedis jedis = new Jedis("localhost");
            try {
                jedis.subscribe(listener,channel);
            }catch (JedisConnectionException e) {
                server.log(ChatColor.RED + "RECONECTING");
                subscribeDelayed(listener,channel, 10000L);
                e.addSuppressed(new Throwable());
            }
            jedis.quit();
        });
    }

}