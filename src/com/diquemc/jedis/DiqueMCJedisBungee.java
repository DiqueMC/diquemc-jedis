package com.diquemc.jedis;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class DiqueMCJedisBungee extends Plugin implements DiqueMCJedisServer {

    public void log (String message) {
        getLogger().info(message);
    }

    public void scheduleAsync ( Runnable runnable) {
        BungeeCord.getInstance().getScheduler().runAsync(this, runnable);
    }

    public void scheduleAsync ( Runnable runnable, Long delay) {
        BungeeCord.getInstance().getScheduler().schedule(this, runnable, delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onEnable() {
        BungeeCord.getInstance().getLogger().info("INICIANDO ESTO");
        DiqueMCJedis.server = this;
        DiqueMCJedis.initializePool();
    }

    public void onDisable() {
        DiqueMCJedis.destroyPool();
    }
}