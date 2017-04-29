package me.ezeh.spammy23;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Perversiontps extends JavaPlugin {

    @Override
    public void onEnable() {
        saveResource("config.yml", false);
        Bukkit.getPluginManager().registerEvents(new PVSListener(this), this);
        getLogger().info("Enabled Per-version resources");

    }
}
