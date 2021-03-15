package ca.mathiewmay.hideandseek.game;

import ca.mathiewmay.hideandseek.GameInfo;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class HideAndSeekGame {

    State game_state;
    int start_countdown;
    int game_countdown;

    ArrayList<Player> seekers = new ArrayList<>();
    ArrayList<Player> hiders = new ArrayList<>();

    public HideAndSeekGame(State game_state){
        this.game_state = game_state;
        this.start_countdown = GameInfo.start_countdown;
        this.game_countdown = GameInfo.game_countdown;
    }

    public void gameTick(){
        this.game_countdown = this.game_countdown -1;
    }

    public int getGameCountdown(){
        return game_countdown;
    }

    public State getState(){
        return game_state;
    }

    public void setState(State new_state){
        this.game_state = new_state;
    }

    public int getStartCountdown(){
        return start_countdown;
    }

    public void setStartCountdown(int new_value){
        this.start_countdown = new_value;
    }

    public void resetStartCountdown(){
        this.start_countdown = GameInfo.start_countdown;
    }

    public void addSeeker(Player player){
        seekers.add(player);
    }

    public void removeSeeker(Player player){
        seekers.remove(player);
    }

    public ArrayList<Player> getSeekers(){
        return seekers;
    }

    public void addHider(Player player){
        hiders.add(player);
    }

    public void removeHider(Player player){
        hiders.remove(player);
    }

    public ArrayList<Player> getHiders(){
        return hiders;
    }

    public void startPlayers(){
        for(Player player : hiders){
            player.teleport(new Location(GameInfo.world, GameInfo.start.getX(), GameInfo.start.getY(), GameInfo.start.getZ()));
        }
        for(Player player : seekers){
            player.teleport(new Location(GameInfo.world, GameInfo.seekers_waiting_room.getX(), GameInfo.seekers_waiting_room.getY(), GameInfo.seekers_waiting_room.getZ()));
        }
    }

    public void deploySeekers(){
        for(Player player : seekers){
            player.teleport(new Location(GameInfo.world, GameInfo.start.getX(), GameInfo.start.getY(), GameInfo.start.getZ()));
            player.getInventory().clear();
            player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
        }
    }
}
