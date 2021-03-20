package ca.mathiewmay.hideandseek.game;

import ca.mathiewmay.hideandseek.HideAndSeek;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

public class HiderPlayer {

    private final Player player;
    private BlockFollower follower;
    private Location solidifiedBlock;
    private boolean solidified;

    HideAndSeek plugin;
    HideAndSeekGame game;
    public HiderPlayer(HideAndSeek plugin, HideAndSeekGame game, Player player){
        this.plugin = plugin;
        this.game = game;
        this.player = player;
    }

    public Player getPlayer() { return player; }

    public FallingBlock getFollower(){
        return follower.getFallingBlock();
    }

    public void makeFollower() {
        this.follower = new BlockFollower(plugin, game, player, Material.AIR);
    }

    public boolean hasFollower(){
        return this.follower != null;
    }

    public void setFollowerMaterial(Material material){
        follower.setFollowerMaterial(material);
    }

    public Location getSolidifiedLocation(){
        return solidifiedBlock;
    }

    public void solidify(){
        if(hasFollower() && !isSolidified()){
            Block block = player.getLocation().getBlock();
            if(block.getType()==Material.AIR || block.getType()==Material.CAVE_AIR){
                block.setType(follower.getType());
                solidifiedBlock = block.getLocation();
                follower.setFollowerMaterial(Material.AIR); // TODO Make a function that completely hides the follower.
                solidified = true;
            }else{
                player.sendMessage("You can't solidify here!");
            }
        }
    }

    public  void unSolidify(){
        if(hasFollower() && isSolidified()){
            Block block = solidifiedBlock.getBlock();
            follower.setFollowerMaterial(block.getType());
            block.setType(Material.AIR);
            solidified = false;
        }
    }

    public boolean isSolidified(){
        return solidified;
    }
}
