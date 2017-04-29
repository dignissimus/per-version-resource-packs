package me.ezeh.spammy23;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;


public class PVSListener implements Listener {
    static HashMap<UUID, Integer> versions = new HashMap<>();
    JavaPlugin plugin;

    PVSListener(JavaPlugin pl) {
        plugin = pl;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Integer v = ProtocolLibrary.getProtocolManager().getProtocolVersion(p);
        versions.put(p.getUniqueId(), v);
        String rp = getResourcePack(p.getWorld(), v);
        setResourcePack(p, rp);
    }

    public void setResourcePack(Player player, String string){
        if(string == null){
            plugin.getLogger().info("Did you set a default resource pack? ");
            return;
        }
        player.setResourcePack(string);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        versions.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerEnterWorld(PlayerTeleportEvent e) {
        if (e.getFrom().getWorld() == e.getFrom().getWorld()) return;
        Player p = e.getPlayer();
        p.setResourcePack(getResourcePack(e.getTo().getWorld(), versions.get(p.getUniqueId())));
    }

    private String getResourcePack(World w, int version) {
        String config = plugin.getConfig().getString("resources." + w.getName() + "." + version);
        if (config == null)
            config = plugin.getConfig().getString("resources." + w.getName() + "default");
        if (config == null)
            config = plugin.getConfig().getString("defaults." + version);
        if (config == null)
            config = plugin.getConfig().getString("default");
        return config;
    }
}
