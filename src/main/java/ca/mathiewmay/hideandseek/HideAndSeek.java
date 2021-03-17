package ca.mathiewmay.hideandseek;

import ca.mathiewmay.hideandseek.game.HideAndSeekGame;
import ca.mathiewmay.hideandseek.game.State;
import ca.mathiewmay.hideandseek.listeners.MainListeners;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public final class HideAndSeek extends JavaPlugin {

    public HideAndSeekGame game = new HideAndSeekGame(this, State.W_PLAYERS);

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new MainListeners(this), this);
        resetPlayers();
        startNewGame();
    }

    public void startNewGame(){
        game.resetGame();
        game_clock();
    }

    private void game_clock(){
        new BukkitRunnable() {
            public void run(){

                /* WAITING FOR PLAYERS */
                if(game.getState().equals(State.W_PLAYERS)){
                    if(Bukkit.getOnlinePlayers().size() < GameInfo.min_players){
                        HideAndSeek.notify("Waiting for more players...");
                    }else if(Bukkit.getOnlinePlayers().size() >= GameInfo.min_players){
                        HideAndSeek.notify("Game starting soon...");
                        game.setState(State.S_COUNTDOWN);
                    }
                }

                /* START COUNTDOWN */
                if(game.getState().equals(State.S_COUNTDOWN)){
                    int countdown = game.getStartCountdown();
                    if(countdown > 0){
                        if(Bukkit.getOnlinePlayers().size() >= GameInfo.min_players){
                            HideAndSeek.notify("Game starting in "+countdown);
                            game.setStartCountdown(countdown-1);
                        }else{
                            HideAndSeek.notify("Waiting for more players...");
                            game.resetStartCountdown();
                        }
                    }else{
                        HideAndSeek.notify("Game starting now");
                        game.setState(State.PRE_GAME);
                    }
                }

                /* PRE GAME */
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
                    game.startPlayers();
                    game.setState(State.H_COUNTDOWN);
                }

                /* H COUNTDOWN */
                if(game.getState().equals(State.H_COUNTDOWN)){
                    int countdown = game.getHCountdown();
                    if(countdown > 0){
                        game.notifyTeams("Choose your block "+countdown, "Deploying in "+countdown+" seconds");
                        game.setHCountdown(countdown-1);
                    }else{
                        for(Player player : game.getHiders())
                            game.autoRegisterHider(player);
                        game.deploySeekers();
                        game.setState(State.PLAYING);
                    }
                }

                /* GAME LOGIC */
                if(game.getState().equals(State.PLAYING)){
                    int countdown = game.getGameCountdown();
                    if(countdown > 0){
                        HideAndSeek.notify("Time left "+countdown+" H: "+game.getHidersTicket());
                        if(game.getHidersTicket() == 0){
                            game.setState(State.ENDING);
                        }
                        game.setGameCountdown(countdown-1);
                    }else{
                        game.setState(State.ENDING);
                    }
                }

                /* ENDING GAME */
                if(game.getState().equals(State.ENDING)){
                    if(game.getHidersTicket() == 0){
                        HideAndSeek.notify("Seekers Wins!");
                    }else{
                        HideAndSeek.notify("Hiders Wins!");
                    }
                    game.setState(State.ENDED);
                }

                /* END GAME */
                if(game.getState().equals(State.ENDED)){
                    for(Player player : Bukkit.getOnlinePlayers()){
                        player.teleport(GameInfo.waiting_room);
                        player.getInventory().clear();
                    }
                    game.resetGame();
                }
            }
        }.runTaskTimer(this, 20, 20);
    }

    public static void notify(String message){
        for(Player player : Bukkit.getOnlinePlayers()){
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        }
    }

    public void notify(Player player, String message){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    public void resetPlayers(){
        for(Player player : Bukkit.getOnlinePlayers()){
            preparePlayer(this, player);
        }
    }

    public static void preparePlayer(HideAndSeek plugin,Player player){
        player.teleport(GameInfo.waiting_room);
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();;
        player.setInvisible(false);
        for(Player target : Bukkit.getOnlinePlayers()){
            player.showPlayer(plugin, target);
        }
    }
}
