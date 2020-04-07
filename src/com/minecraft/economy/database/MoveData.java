package com.minecraft.economy.database;

import com.minecraft.economy.economyMain.Economy;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MoveData {

    public static boolean MoveDataFromLocal(){
        String path = Economy.getInstance().getDataFolder() + "/playerData";
        File files = new File(path);
        File[] array = files.listFiles();
        DataBase dataBase = Economy.dataBase;
        assert array != null;

        for (File file : array){
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            String player_name = file.getName().replace(".yml", "");
            int money = config.getInt("money");
            int bank = config.getInt("bank");

            
        }
        return false;
    }

}
