package com.diquemc.jedis;


import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class DiqueMCJedisBungee extends Plugin implements DiqueMCJedisServer {

    public void log (String message) {
        getLogger().info(message);
    }

    public void scheduleAsync ( Runnable runnable) {
        getProxy().getScheduler().runAsync(this, runnable);
    }

    public void scheduleAsync ( Runnable runnable, Long delay) {
        getProxy().getScheduler().schedule(this, runnable, delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onEnable() {
        getProxy().getLogger().info("INICIANDO ESTO");
        DiqueMCJedis.enable(this);
//        DiqueMCJedis.setServer(this);
//        DiqueMCJedis.getPool();
    }

    public void onDisable() {
//        DiqueMCJedis.destroyPool();
        DiqueMCJedis.disable();
    }
}
