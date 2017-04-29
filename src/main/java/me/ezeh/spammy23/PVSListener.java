package me.ezeh.spammy23;

import com.comphenix.protocol.ProtocolLibrary;
import net.minecraft.server.v1_11_R1.PacketLoginOutSuccess;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;


public class PVSListener implements Listener {
    JavaPlugin plugin;
    PVSListener(JavaPlugin pl){
        plugin = pl;
    }
    static HashMap<UUID, Integer> versions = new HashMap<>();
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        Integer v = ProtocolLibrary.getProtocolManager().getProtocolVersion(p);
        versions.put(p.getUniqueId(), v);
        p.setResourcePack(getResourcePack(p.getWorld(), v));
    }
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        versions.remove(e.getPlayer().getUniqueId());
    }
    @EventHandler
    public void onPlayerEnterWorld(PlayerTeleportEvent e){
        if(e.getFrom().getWorld()==e.getFrom().getWorld())return;
        Player p = e.getPlayer();
        p.setResourcePack(getResourcePack(e.getTo().getWorld(), versions.get(p.getUniqueId())));
    }
    private String getResourcePack(World w, int version){
        String config = plugin.getConfig().getString("resources."+w.getName()+"."+version);
        if (config==null)
            config = plugin.getConfig().getString("resources."+w.getName()+"default");
        if(config==null)
            config = plugin.getConfig().getString("defaults."+version);
        if (config==null)
            config = plugin.getConfig().getString("default");
        return config;
    }
}
