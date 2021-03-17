package ca.mathiewmay.hideandseek.maps;

import ca.mathiewmay.hideandseek.game.BlockPool;
import org.bukkit.Location;

public interface Map {

    public String getName();
    public String getAuthor();
    public String getLink();
    public String getDifficulty();

    public Location getWaitingRoom();
    public Location getSeekersRoom();
    public Location getStart();

    public BlockPool getBlockPool();
}
