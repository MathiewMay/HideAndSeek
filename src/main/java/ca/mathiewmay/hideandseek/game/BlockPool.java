package ca.mathiewmay.hideandseek.game;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BlockPool {

    List<Material> pool = new ArrayList<>();

    public BlockPool(String materials){
        makeWith(materials);
    }

    public List<Material> get(){
        return pool;
    }

    private void makeWith(String input){
        String[] materials = input.toUpperCase().split(",");
        pool.clear();
        for (String material : materials) {
            pool.add(Material.valueOf(material));
        }
    }
}
