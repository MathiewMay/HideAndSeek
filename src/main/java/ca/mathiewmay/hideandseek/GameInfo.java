package ca.mathiewmay.hideandseek;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class GameInfo {
    public static final int max_players = 20;
    public static final int min_players = 2;

    public static final int start_countdown = 30;
    public static final int game_countdown = 300;

    public static final World world = Bukkit.getWorld("world");

    public static final Vector waiting_room = new Vector(23, 191, 94);
    public static final Vector seekers_waiting_room = new Vector(37.5,119,181.5);

    public static final Vector start = new Vector(24.5,69,203.5);
}
