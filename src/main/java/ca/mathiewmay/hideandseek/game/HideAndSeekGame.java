package ca.mathiewmay.hideandseek.game;

import ca.mathiewmay.hideandseek.GameSettings;
import ca.mathiewmay.hideandseek.HideAndSeek;
import ca.mathiewmay.hideandseek.maps.Aquarius;
import ca.mathiewmay.hideandseek.maps.Map;
import ca.mathiewmay.hideandseek.maps.ValdinCity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HideAndSeekGame {

    State game_state;
    int start_countdown;
    int h_countdown;
    int game_countdown;

    List<Map> map_pool = new ArrayList<>();
    Map map;

    ArrayList<Player> seekers = new ArrayList<>();
    ArrayList<Player> hiders = new ArrayList<>();

    HashMap<Player, Material> hiders_block = new HashMap<>();

    int seekers_ticket = 0;
    int hiders_ticket = 0;

    HideAndSeek plugin;
    public HideAndSeekGame(HideAndSeek plugin, State game_state){
        map_pool.add(new Aquarius()); map_pool.add(new ValdinCity());
        chooseMap();
        this.plugin = plugin;
        this.game_state = game_state;
        this.start_countdown = GameSettings.start_countdown;
        this.h_countdown = GameSettings.h_countdown;
        this.game_countdown = GameSettings.game_countdown;
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
    public int getHCountdown(){
        return h_countdown;
    }
    public int getGameCountdown(){
        return game_countdown;
    }
    public void setStartCountdown(int new_value){
        this.start_countdown = new_value;
    }
    public void setHCountdown(int new_value){
        this.h_countdown = new_value;
    }
    public void setGameCountdown(int new_value){
        this.game_countdown = new_value;
    }

    public void resetStartCountdown(){
        this.start_countdown = GameSettings.start_countdown;
    }

    public ArrayList<Player> getSeekers(){
        return seekers;
    }
    public ArrayList<Player> getHiders(){
        return hiders;
    }
    public void addSeeker(Player player){
        seekers.add(player);
        seekers_ticket++;
    }
    public void addHider(Player player){
        hiders.add(player);
        hiders_ticket++;
    }
    public void removeSeeker(Player player){
        seekers.remove(player);
    }
    public void removeHider(Player player){
        hiders.remove(player);
    }

    public int getHidersTicket(){
        return hiders_ticket;
    }

    public Map getMap() { return  map; }

    public void startPlayers(){
        for(Player player : hiders){
            player.teleport(map.getStart());
            hidersTempLoadout(player);
        }
        for(Player player : seekers){
            player.teleport(map.getSeekersRoom());
        }
    }

    public void hidersTempLoadout(Player player){
        player.getInventory().clear();
        for(Material material : map.getBlockPool().get()){
            player.getInventory().addItem(new ItemStack(material));
        }
    }

    public void hidersLoadout(Player player){
        player.getInventory().clear();
    }

    public void deploySeekers(){
        for(Player player : seekers){
            player.teleport(map.getStart());
            player.getInventory().clear();
            player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
        }
    }

    public boolean isPlayerOfGame(Player player){
        return getSeekers().contains(player) || getHiders().contains(player);
    }

    public void eliminatePlayer(Player player){
        if(getHiders().contains(player)){
            removeHider(player);
            addSeeker(player);
            hiders_block.remove(player);
            hiders_ticket--;
        }
    }

    public void notifyTeams(String hiders, String seekers) {
        for(Player player : getHiders()){
            plugin.notify(player, hiders);
        }
        for (Player player : getSeekers()){
            plugin.notify(player, seekers);
        }
    }

    public void resetGame(){
        chooseMap();
        this.game_state = State.W_PLAYERS;
        this.start_countdown = GameSettings.start_countdown;
        this.h_countdown = GameSettings.h_countdown;
        this.game_countdown = GameSettings.game_countdown;
        seekers.clear();
        hiders.clear();
        this.seekers_ticket = 0;
        this.hiders_ticket = 0;
    }

    public void setBlockHider(Player player, Material material){
        hiders_block.put(player, material);
    }

    public void autoRegisterHider(Player player){
        if(!hiders_block.containsKey(player)){
            Material mat = player.getInventory().getItemInMainHand().getType();
            if(mat.equals(Material.AIR)){
                mat = map.getBlockPool().get().get(new Random().nextInt(map.getBlockPool().get().size()));
            }
            hiders_block.put(player, mat);
            player.sendMessage("You are now hiding as a "+mat.toString().replace("_", " ").toLowerCase());
            plugin.game.hidersLoadout(player);
        }
    }

    public void chooseMap(){
        if(map == null){
            map = map_pool.get(new Random().nextInt(map_pool.size()));
        }else{
            Map new_map = map_pool.get(new Random().nextInt(map_pool.size()));
            if(map.getName() == new_map.getName())
                chooseMap();
            else
                map = map_pool.get(new Random().nextInt(map_pool.size()));
        }
    }
}
