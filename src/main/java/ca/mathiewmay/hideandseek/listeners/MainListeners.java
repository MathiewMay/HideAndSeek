package ca.mathiewmay.hideandseek.listeners;

import ca.mathiewmay.hideandseek.HideAndSeek;
import ca.mathiewmay.hideandseek.game.State;
import org.bukkit.Material;
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
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

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
            if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)){
                Material material = player.getInventory().getItemInMainHand().getType();
                if(plugin.game.getHiders().contains(player) && plugin.game.getState().equals(State.H_COUNTDOWN)){
                    plugin.game.setBlockHider(player, material);
                    player.sendMessage("You are now hiding as a "+material.toString().replace("_", " ").toLowerCase());
                    plugin.game.hidersLoadout(player);
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        event.setCancelled(true);
    }
}
