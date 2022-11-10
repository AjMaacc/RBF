package me.ajmac.contactflag;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class RBF extends JavaPlugin implements Listener {

    StateFlag CONTACT_DAMAGE;
    WorldGuardPlugin worldguard;

    public void onEnable() {
        getLogger().log(Level.INFO, "RBF - Made by AjMaacc");
        // Plugin startup logic
        Plugin wgPlugin = getServer().getPluginManager().getPlugin("WorldGuard");
        if (wgPlugin instanceof WorldGuardPlugin) {
            worldguard = (WorldGuardPlugin) wgPlugin;
        }
        this.getServer().getPluginManager().registerEvents(this, this);
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    @Override
    public void onLoad() {
        WorldGuard worldGuard = WorldGuard.getInstance();
        FlagRegistry flagRegistry = worldGuard.getFlagRegistry();

        flagRegistry.register(CONTACT_DAMAGE = new StateFlag("contact-damage", true));
        getLogger().log(Level.INFO, "Loaded contact flag successfully!");
    }
    @EventHandler
    public void onContactDamage(EntityDamageByBlockEvent e) {

        if (e.getEntity() instanceof Player) {
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer((Player) e.getEntity());
            Location l = e.getEntity().getLocation();
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            ApplicableRegionSet set = container.get(new BukkitWorld(l.getWorld())).getApplicableRegions(BlockVector3.at(l.getX(), l.getY(), l.getZ()));

            if (e.getDamager() != null) {
                if (e.getDamager().getType().equals(Material.SWEET_BERRY_BUSH) && !(set.testState(localPlayer, CONTACT_DAMAGE)))
                    e.setCancelled(true);
            }
        }
    }
}
