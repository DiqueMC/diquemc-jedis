package com.diquemc.jedis;

import net.md_5.bungee.api.ChatColor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.Map;

@SuppressWarnings("unused")
public abstract class DiqueMCJedis {
    public static DiqueMCJedisServer server;
    public static JedisPool jedisPool = null;

    public static void del(String key) {
        try (Jedis j = jedisPool.getResource()) {
            j.del(key);
        }
    }

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

    public static Object eval(String key){
        try (Jedis j = jedisPool.getResource()) {
            return j.eval(key);
        }
    }

    public static Map<String, String> hgetall(String key){
        try (Jedis j = jedisPool.getResource()) {
            return j.hgetAll(key);
        }
    }

    public static String hget(String key, String field){
        try (Jedis j = jedisPool.getResource()) {
            return j.hget(key, field);
        }
    }

    public static void hmset(String key, Map<String,String> fieldValues){
        try (Jedis j = jedisPool.getResource()) {
            j.hmset(key, fieldValues);
        }
    }

    public static void hset(String key, String field, String value){
        try (Jedis j = jedisPool.getResource()) {
            j.hset(key, field, value);
        }
    }

    public static boolean sismember(String set, String value){
        try (Jedis j = jedisPool.getResource()) {
            return j.sismember(set, value);
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