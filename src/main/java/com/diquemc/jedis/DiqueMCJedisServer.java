package com.diquemc.jedis;

interface DiqueMCJedisServer {
    void scheduleAsync ( Runnable runnable);
    void scheduleAsync ( Runnable runnable, Long delay);
    void log (String message);
}