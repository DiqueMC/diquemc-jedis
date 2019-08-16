package com.diquemc.jedis;

import net.md_5.bungee.api.ChatColor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.Map;

public abstract class DiqueMCJedis {
    private static DiqueMCJedisServer server;
    private static JedisPool jedisPool = null;

    static void enable(DiqueMCJedisServer _server) {
        server = _server;
        getPool();
    }

    static void disable() {
        server = null;
        destroyPool();
    }

    private static JedisPool getPool() {
        if(jedisPool == null) {
            jedisPool = new JedisPool("localhost");
        }
        return jedisPool;
    }

    private static void destroyPool() {
        if (jedisPool != null) {
            jedisPool.destroy();
            jedisPool = null;
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static Jedis getPoolResource () {
        return getPool().getResource();
    }

    public static void returnPoolResource (Jedis resource) {
        getPool().returnResourceObject(resource);
    }

    public static void del(String key) {
        try (Jedis j = getPoolResource()) {
            j.del(key);
        }

    }

    public static String get(String key){
        try (Jedis j = getPoolResource()) {
            return j.get(key);
        }
    }

    public static String set(String key, String value){
        try (Jedis j = getPoolResource()) {
            return j.set(key,value);
        }
    }

    public static boolean exists(String key){
        try (Jedis j = getPoolResource()) {
            return j.exists(key);
        }
    }

    public static Object eval(String key){
        try (Jedis j = getPoolResource()) {
            return j.eval(key);
        }
    }

    public static Map<String, String> hgetall(String key){
        try (Jedis j = getPoolResource()) {
            return j.hgetAll(key);
        }
    }

    public static String hget(String key, String field){
        try (Jedis j = getPoolResource()) {
            return j.hget(key, field);
        }
    }

    public static void hmset(String key, Map<String,String> fieldValues){
        try (Jedis j = getPoolResource()) {
            j.hmset(key, fieldValues);
        }
    }

    public static void hset(String key, String field, String value){
        try (Jedis j = getPoolResource()) {
            j.hset(key, field, value);
        }
    }

    public static void hdel(String key, String field){
        try (Jedis j = getPoolResource()) {
            j.hdel(key, field);
        }
    }

    public static boolean sismember(String set, String value){
        try (Jedis j = getPoolResource()) {
            return j.sismember(set, value);
        }
    }

    public static void publish (final String channel, final String message){
        if(server == null) {
            return;
        }
        server.scheduleAsync(() -> {
//            Jedis jedis = new Jedis("localhost");
            Jedis jedis = getPoolResource();
            jedis.publish(channel, message);
            returnPoolResource(jedis);
        });
    }

    public static void subscribeDelayed (final JedisPubSub listener, final String channel, Long delay) {
        if(server == null) {
            return;
        }
        server.scheduleAsync(() -> {
//            Jedis jedis = new Jedis("localhost");
            Jedis jedis = getPoolResource();
            try {
                jedis.subscribe(listener,channel);
            }catch (Exception e) {
                server.log(ChatColor.RED + "Failed to RECONECTING");
                subscribeDelayed(listener,channel, delay);
            }
            returnPoolResource(jedis);
        },delay);

    }

    public static void subscribe (final JedisPubSub listener, final String channel){
        if(server == null) {
            return;
        }
        server.scheduleAsync(() -> {
            Jedis jedis = getPoolResource();
            try {
                jedis.subscribe(listener, channel);
            }catch (JedisConnectionException e) {
                server.log(ChatColor.RED + "RECONECTING");
                subscribeDelayed(listener,channel, 10000L);
                e.addSuppressed(new Throwable());
            }
            returnPoolResource(jedis);
        });
    }

}