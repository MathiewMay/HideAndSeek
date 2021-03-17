package ca.mathiewmay.hideandseek.maps;

import ca.mathiewmay.hideandseek.game.BlockPool;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Aquarius implements Map {
    @Override
    public String getName() {
        return "Aquarius";
    }

    @Override
    public String getAuthor() {
        return "ChaosOlymp";
    }

    @Override
    public String getLink() {
        return "https://chaosolymp.de/";
    }

    @Override
    public String getDifficulty() {
        return "Hard";
    }

    @Override
    public Location getWaitingRoom() {
        return new Location(Bukkit.getWorld(getName()), -54.5, 119, -103.5, -45, 0);
    }

    @Override
    public Location getSeekersRoom() {
        return new Location(Bukkit.getWorld(getName()), -54.5, 102, -103.5, 90, 0);
    }

    @Override
    public Location getStart() {
        return new Location(Bukkit.getWorld(getName()), -54.5, 38, -103.5, 0, 0);
    }

    @Override
    public BlockPool getBlockPool() {
        return new BlockPool("cobblestone_wall,acacia_wood,spruce_fence,lantern,smooth_quartz_stairs");
    }
}
