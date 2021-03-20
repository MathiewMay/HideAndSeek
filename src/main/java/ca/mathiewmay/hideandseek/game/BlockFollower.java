package ca.mathiewmay.hideandseek.game;

import ca.mathiewmay.hideandseek.HideAndSeek;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockFollower {

    private final Player player;
    private FallingBlock follower;
    private ArmorStand ride;
    private Material type;

    HideAndSeek plugin;
    HideAndSeekGame game;
    public BlockFollower(HideAndSeek plugin, HideAndSeekGame game, Player player, Material material){
        this.game = game;
        this.plugin = plugin;
        this.player = player;
        this.type = material;
        initFollower(material);
        protectFollower();
        attachFollower();
    }

    private void initFollower(Material material){
        Block block = player.getLocation().getBlock();
        follower = block.getWorld().spawnFallingBlock(block.getLocation(), material.createBlockData());
        follower.setPersistent(true);
        follower.setInvulnerable(true);
        follower.setGravity(false);
        follower.setDropItem(false);
        ride = (ArmorStand) block.getWorld().spawnEntity(player.getLocation().subtract(0,1.48,0), EntityType.ARMOR_STAND);
        ride.setGravity(false);
        ride.setCollidable(false);
        ride.setInvisible(true);
        ride.addPassenger(follower);
    }

    private void protectFollower(){
        new BukkitRunnable() {
            @Override
            public void run() {
                follower.setTicksLived(1);
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }

    private void attachFollower(){
        new BukkitRunnable(){
            @Override
            public void run() {
                ride.eject();
                Location newLocation = player.getLocation().subtract(0, 1.48, 0); newLocation.setYaw(0); newLocation.setPitch(0);
                ride.teleport(newLocation);
                ride.addPassenger(follower);
                if(player.getHealth() == 0){
                    end();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    public void setFollowerMaterial(Material material){
        this.type = material;
        ride.eject();
        follower.remove();
        Block block = player.getLocation().getBlock();
        follower = block.getWorld().spawnFallingBlock(block.getLocation(), material.createBlockData());
        follower.setPersistent(true);
        follower.setInvulnerable(true);
        follower.setGravity(false);
        follower.setDropItem(false);
        ride.addPassenger(follower);
    }

    public Material getType(){
        return this.type;
    }

    public FallingBlock getFallingBlock(){
        return follower;
    }

    private void end(){
        ride.remove();
        follower.remove();
    }
}
