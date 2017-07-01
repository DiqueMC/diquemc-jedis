package com.diquemc.jedis;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DiqueMCJedisBukkit extends JavaPlugin implements DiqueMCJedisServer {

    public void log (String message) {
        getLogger().info(message);
    }

    public void scheduleAsync ( Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(this, runnable);
    }

    public void scheduleAsync ( Runnable runnable, Long delay) {
        Bukkit.getScheduler().runTaskLater(this, runnable, delay);
    }

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("INICIANDO ESTO");
        DiqueMCJedis.server = this;
        DiqueMCJedis.initializePool();
    }

    public void onDisable() {
        DiqueMCJedis.destroyPool();
    }
}