package ca.mathiewmay.hideandseek.game;

import ca.mathiewmay.hideandseek.GameSettings;
import ca.mathiewmay.hideandseek.HideAndSeek;
import ca.mathiewmay.hideandseek.maps.Aquarius;
import ca.mathiewmay.hideandseek.maps.Map;
import ca.mathiewmay.hideandseek.maps.ValdinCity;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class HideAndSeekGame {

    State gameState;
    int startCountdown;
    int hCountdown;
    int gameCountdown;

    List<Map> mapPool = new ArrayList<>();
    Map map;

    ArrayList<Player> seekers = new ArrayList<>();
    ArrayList<HiderPlayer> hiders = new ArrayList<>();

    int seekersTicket = 0;
    int hidersTicket = 0;

    HideAndSeek plugin;
    public HideAndSeekGame(HideAndSeek plugin, State gameState){
        mapPool.add(new Aquarius()); mapPool.add(new ValdinCity());
        chooseMap();
        this.plugin = plugin;
        this.gameState = gameState;
        this.startCountdown = GameSettings.startCountdown;
        this.hCountdown = GameSettings.hCountdown;
        this.gameCountdown = GameSettings.gameCountdown;
    }

    public State getState(){
        return gameState;
    }
    public void setState(State newState){
        this.gameState = newState;
    }

    public int getStartCountdown(){
        return startCountdown;
    }
    public int getHCountdown(){
        return hCountdown;
    }
    public int getGameCountdown(){
        return gameCountdown;
    }
    public void setStartCountdown(int newValue){
        this.startCountdown = newValue;
    }
    public void setHCountdown(int newValue){
        this.hCountdown = newValue;
    }
    public void setGameCountdown(int newValue){
        this.gameCountdown = newValue;
    }

    public void resetStartCountdown(){
        this.startCountdown = GameSettings.startCountdown;
    }

    public ArrayList<Player> getSeekers(){
        return seekers;
    }
    public ArrayList<HiderPlayer> getHiders(){
        return hiders;
    }
    public void addSeeker(Player player){
        seekers.add(player);
        seekersTicket++;
    }
    public void addHider(HiderPlayer hider){
        hiders.add(hider);
        hidersTicket++;
    }
    public void removeHider(HiderPlayer hider){
        hiders.remove(hider);
    }
    public boolean isPlayerHider(Player player){
        for(HiderPlayer hider : hiders){
            if(hider.getPlayer() == player)
                return true;
        }
        return false;
    }
    public HiderPlayer getPlayerHiderFromPlayer(Player player){
        if(isPlayerHider(player)){
            for(HiderPlayer hider : hiders){
                if(hider.getPlayer() == player){
                    return hider;
                }
            }
        }
        return null;
    }

    public int getHidersTicket(){
        return hidersTicket;
    }

    public Map getMap() { return  map; }

    public void startPlayers(){
        for(HiderPlayer hider : hiders){
            hider.getPlayer().teleport(map.getStart());
            hidersSelectionLoadout(hider.getPlayer());
        }
        for(Player player : seekers){
            player.teleport(map.getSeekersRoom());
        }
    }

    public void hidersSelectionLoadout(Player player){
        player.getInventory().clear();
        for(Material material : map.getBlockPool().get()){
            player.getInventory().addItem(new ItemStack(material));
        }
    }

    public void hidersLoadout(Player player){
        player.getInventory().clear();
        player.getInventory().addItem(makeSimpleItem(Material.BEDROCK, "Solidify"));
        player.getInventory().addItem(makeSimpleItem(Material.REDSTONE_TORCH, "Rotate"));
    }

    public void hiderSelectBlock(HiderPlayer hider, Material material){
        if(!hider.hasFollower())
            hider.makeFollower();
        hider.setFollowerMaterial(material);
        hider.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2147483647, 2));
        hider.getPlayer().sendMessage("You are now hiding as a "+material.toString().replace("_", " ").toLowerCase());
        hidersLoadout(hider.getPlayer());
    }

    public void deploySeekers(){
        for(Player player : seekers){
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(map.getStart());
            player.getInventory().clear();
            player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
        }
    }

    public boolean isPlayerOfGame(Player player){
        boolean playerHider = false;
        for(HiderPlayer hider : hiders){
            if (hider.getPlayer() == player) {
                playerHider = true;
                break;
            }
        }
        return getSeekers().contains(player) || playerHider;
    }

    public void eliminatePlayer(Player player){
        if(isPlayerHider(player)) {
            removeHider(getPlayerHiderFromPlayer(player));
            addSeeker(player);
            hidersTicket--;
        }
    }

    public void notifyTeams(String hiders, String seekers) {
        for(HiderPlayer hider : getHiders()){
            plugin.notify(hider.getPlayer(), hiders);
        }
        for (Player player : getSeekers()){
            plugin.notify(player, seekers);
        }
    }

    public void resetGame(){
        chooseMap();
        this.gameState = State.W_PLAYERS;
        this.startCountdown = GameSettings.startCountdown;
        this.hCountdown = GameSettings.hCountdown;
        this.gameCountdown = GameSettings.gameCountdown;
        seekers.clear();
        hiders.clear();
        this.seekersTicket = 0;
        this.hidersTicket = 0;
    }

    public void autoRegisterHider(HiderPlayer hider){
        if(!hider.hasFollower()){
            Material material = hider.getPlayer().getInventory().getItemInMainHand().getType();
            if(material.equals(Material.AIR)){
                material = map.getBlockPool().get().get(new Random().nextInt(map.getBlockPool().get().size()));
            }
            hiderSelectBlock(hider, material);
        }
    }

    public void chooseMap(){
        if(map == null){
            map = mapPool.get(new Random().nextInt(mapPool.size()));
        }else{
            Map new_map = mapPool.get(new Random().nextInt(mapPool.size()));
            if(new_map.equals(map))
                chooseMap();
            else
                map = mapPool.get(new Random().nextInt(mapPool.size()));
        }
    }

    private ItemStack makeSimpleItem(Material material, String displayName){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
