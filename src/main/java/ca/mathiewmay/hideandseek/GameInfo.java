package ca.mathiewmay.hideandseek;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class GameInfo {
    public static final int min_players = 2;

    public static final int start_countdown = 30;
    public static final int game_countdown = 300;
    public static final int h_countdown = 20;

    public static final Location waiting_room =  new Location(Bukkit.getWorld("world"), 103.5, 133, -3.5, 90, 0);
    public static final Location seekers_waiting_room = new Location(Bukkit.getWorld("world"), -45.5, 62, 175.5);
    public static final Location start = new Location(Bukkit.getWorld("world"), -121.5, 71, -9.5, -45, 0);

    public static List<Material> block_pool(){
        List<Material> pool = new ArrayList<>();
        pool.add(Material.SPRUCE_LEAVES);
        pool.add(Material.QUARTZ_PILLAR);
        pool.add(Material.HAY_BLOCK);
        pool.add(Material.BOOKSHELF);
        pool.add(Material.COBBLESTONE_STAIRS);
        pool.add(Material.ANVIL);
        pool.add(Material.DIORITE);
        return pool;
    }
}