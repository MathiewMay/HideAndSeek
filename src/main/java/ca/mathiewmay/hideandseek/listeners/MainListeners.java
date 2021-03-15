package ca.mathiewmay.hideandseek.listeners;

import ca.mathiewmay.hideandseek.GameInfo;
import ca.mathiewmay.hideandseek.HideAndSeek;
import ca.mathiewmay.hideandseek.game.State;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MainListeners implements Listener {

    HideAndSeek plugin;
    public MainListeners(HideAndSeek plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event){
        if(plugin.game.getState().equals(State.W_PLAYERS)){
            event.getPlayer().teleport(new Location(GameInfo.world, GameInfo.waiting_room.getX(), GameInfo.waiting_room.getY(), GameInfo.waiting_room.getZ()));
        }
    }
}
