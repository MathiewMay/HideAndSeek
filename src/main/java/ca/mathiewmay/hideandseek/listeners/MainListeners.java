package ca.mathiewmay.hideandseek.listeners;

import ca.mathiewmay.hideandseek.HideAndSeek;
import ca.mathiewmay.hideandseek.game.HiderPlayer;
import ca.mathiewmay.hideandseek.game.State;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MainListeners implements Listener {

    HideAndSeek plugin;
    public MainListeners(HideAndSeek plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        plugin.preparePlayer(player);
    }

    @EventHandler
    public void playerDie(PlayerDeathEvent event){
        Player player = event.getEntity();
        player.getInventory().clear();
        if(plugin.game.isPlayerOfGame(player)){
            plugin.game.eliminatePlayer(player);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                player.spigot().respawn();
            }
        }.runTaskLater(plugin, 20L);
    }

    @EventHandler
    public void playerRespawn(PlayerRespawnEvent event){
        // TODO hider to seeker respawn logic
        event.setRespawnLocation(plugin.game.getMap().getWaitingRoom());
    }

    @EventHandler
    public void playerAttackPlayer(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
            Player attacker = (Player) event.getDamager();
            if(!plugin.game.getState().equals(State.PLAYING)){
                event.setCancelled(true);
            }else {
                if(plugin.game.getHiders().contains(attacker)){
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void playerTakeDamage(EntityDamageEvent event){
        if(!plugin.game.getState().equals(State.PLAYING)){
            event.setCancelled(true);
        }else {
            if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL) || event.getCause().equals(EntityDamageEvent.DamageCause.DROWNING) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || event.getCause().equals(EntityDamageEvent.DamageCause.LAVA)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void selectBlock(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(event.getHand() != null && event.getHand().equals(EquipmentSlot.HAND) && !player.getInventory().getItemInMainHand().getType().equals(Material.AIR)){
            if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR) && plugin.game.getState().equals(State.H_COUNTDOWN)){
                Material material = player.getInventory().getItemInMainHand().getType();
                if(plugin.game.isPlayerHider(player) && !plugin.game.getPlayerHiderFromPlayer(player).hasFollower())
                    plugin.game.hiderSelectBlock(plugin.game.getPlayerHiderFromPlayer(player), material);
                if(material.equals(Material.BEDROCK) && plugin.game.isPlayerHider(player) && plugin.game.getPlayerHiderFromPlayer(player).hasFollower())
                    plugin.game.getPlayerHiderFromPlayer(player).solidify();

            }
        }
    }

    @EventHandler
    public void unSolidifyHider(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(plugin.game.isPlayerHider(player)){
            HiderPlayer hider = plugin.game.getPlayerHiderFromPlayer(player);
            if(hider.isSolidified()){
                double solidX = hider.getSolidifiedLocation().getX();
                double solidY = hider.getSolidifiedLocation().getY();
                double solidZ = hider.getSolidifiedLocation().getZ();
                double currentX = Math.floor(player.getLocation().getX());
                double currentY = Math.floor(player.getLocation().getY());
                double currentZ = Math.floor(player.getLocation().getZ());
                Vector solidVector = new Vector(solidX,solidY,solidZ);
                Vector currentVector = new Vector(currentX,currentY,currentZ);
                if(!solidVector.equals(currentVector))
                    hider.unSolidify();
            }
        }
    }

    @EventHandler
    public void seekerAttackFollower(EntityDamageByEntityEvent event){
        Entity victim = event.getEntity();
        if(victim instanceof FallingBlock){
            for(HiderPlayer hider : plugin.game.getHiders()){
                if(!hider.isSolidified() && hider.hasFollower()){
                    if(victim == hider.getFollower()){
                        hider.getPlayer().damage(event.getDamage());
                    }
                }
            }
        }
        // TODO Check if a seeker is attacking a falling block that is owned by a player (follower)
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        if(!event.getPlayer().isOp())
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        if(!event.getPlayer().isOp())
            event.setCancelled(true);
    }
}
