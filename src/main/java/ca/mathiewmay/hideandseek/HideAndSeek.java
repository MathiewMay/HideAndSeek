package ca.mathiewmay.hideandseek;

import ca.mathiewmay.hideandseek.game.HideAndSeekGame;
import ca.mathiewmay.hideandseek.game.State;
import ca.mathiewmay.hideandseek.listeners.MainListeners;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public final class HideAndSeek extends JavaPlugin {

    public HideAndSeekGame game = new HideAndSeekGame(State.W_PLAYERS);

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new MainListeners(this), this);
        for(Player player : Bukkit.getOnlinePlayers()){
            player.teleport(new Location(GameInfo.world, GameInfo.waiting_room.getX(), GameInfo.waiting_room.getY(), GameInfo.waiting_room.getZ()));
        }
        game_clock();
    }

    @Override
    public void onDisable() {
    }

    private void game_clock(){
        new BukkitRunnable() {
            public void run(){
                if(game.getState().equals(State.W_PLAYERS)){
                    if(Bukkit.getOnlinePlayers().size() < GameInfo.min_players){
                        HideAndSeek.notifyPlayers("Waiting for more players...");
                    }else if(Bukkit.getOnlinePlayers().size() >= GameInfo.min_players){
                        HideAndSeek.notifyPlayers("Game starting soon...");
                        game.setState(State.S_COUNTDOWN);
                    }
                }
                if(game.getState().equals(State.S_COUNTDOWN)){
                    int countdown = game.getStartCountdown();
                    if(countdown > 0){
                        if(Bukkit.getOnlinePlayers().size() >= GameInfo.min_players){
                            HideAndSeek.notifyPlayers("Game starting in "+countdown);
                            game.setStartCountdown(countdown-1);
                        }else{
                            HideAndSeek.notifyPlayers("Waiting for more players...");
                            game.resetStartCountdown();
                        }
                    }else{
                        HideAndSeek.notifyPlayers("Game starting now");
                        game.setState(State.PRE_GAME);
                    }
                }
                if(game.getState().equals(State.PRE_GAME)){
                    int seekers_amount = 1;
                    if(Bukkit.getOnlinePlayers().size() >= 15) seekers_amount = 2;
                    ArrayList<Player> player_pool = new ArrayList<>(Bukkit.getOnlinePlayers());
                    while(seekers_amount > 0){
                        Player player = player_pool.get(new Random().nextInt(player_pool.size()));
                        game.addSeeker(player);
                        player_pool.remove(player);
                        seekers_amount--;
                    }
                    for(Player player : player_pool)
                        game.addHider(player);
                    game.setState(State.PLAYING);
                    game.startPlayers();
                }
                if(game.getState().equals(State.PLAYING)){
                    int countdown = game.getGameCountdown();
                    if(countdown > 0){
                        HideAndSeek.notifyPlayers("Time left "+countdown);
                        if(countdown == 280){
                            game.deploySeekers();
                        }
                        game.gameTick();
                    }
                }
            }
        }.runTaskTimer(this, 20, 20);
    }

    public static void notifyPlayers(String message){
        for(Player player : Bukkit.getOnlinePlayers()){
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        }
    }
}
