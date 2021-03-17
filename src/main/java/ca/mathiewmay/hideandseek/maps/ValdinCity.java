package ca.mathiewmay.hideandseek.maps;

import ca.mathiewmay.hideandseek.game.BlockPool;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ValdinCity implements Map{
    @Override
    public String getName() {
        return "ValdinCity";
    }

    @Override
    public String getAuthor() {
        return "SimonValdin";
    }

    @Override
    public String getLink() {
        return "https://mcpedl.com/mega-metropolis-valdin-city/";
    }

    @Override
    public String getDifficulty() {
        return "Hard";
    }

    @Override
    public Location getWaitingRoom() {
        return new Location(Bukkit.getWorld(getName()), 103.5, 133, -3.5, 90, 0);
    }

    @Override
    public Location getSeekersRoom() {
        return new Location(Bukkit.getWorld(getName()), -45.5, 62, 175.5, -90, 0);
    }

    @Override
    public Location getStart() {
        return new Location(Bukkit.getWorld(getName()), -121.5, 71, -9.5, -45, 0);
    }

    @Override
    public BlockPool getBlockPool() {
        return new BlockPool("spruce_leaves,quartz_pillar,hay_block,bookshelf,cobblestone_stairs,anvil,diorite");
    }
}
