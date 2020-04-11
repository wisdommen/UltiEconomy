package com.minecraft.economy.apis;

import com.minecraft.economy.economyMain.Economy;
import com.minecraft.economy.database.DataBase;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class checkMoney {

    //输入：（String）玩家名
    //输出：（File）玩家配置文件
    public static File get_player_file(String player_name) {
        return new File(Economy.getInstance().getDataFolder() + "/playerData", player_name + ".yml");
    }

    //输入：（File）玩家配置文件
    //输出：（YamlConfiguration）玩家配置
    public static YamlConfiguration load_config(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }

    //输入：（String）玩家名
    //输出： （Boolean）玩家文件是否存在，True存在，False不存在
    public static boolean player_file_exists(String player_name) {
        return get_player_file(player_name).exists();
    }

    //输入：（String）玩家名
    //输出：（Integer）玩家剩余的金币，输出-1为未找到玩家信息
    public static int checkmoney(String player_name) {
        DataBase dataBase = Economy.dataBase;

        if (!Economy.getInstance().getConfig().getBoolean("enableDataBase")) {
            if (player_file_exists(player_name)) {
                YamlConfiguration config = load_config(get_player_file(player_name));
                return config.getInt("money");
            }
        }else {
            dataBase.connect();
            if (dataBase.isExist(player_name)){
                return Integer.parseInt((String) dataBase.getData(player_name, "Money"));
            }
            dataBase.close();
        }
        return -1;
    }

    //输入：（String）玩家名
    //输出：（Integer）玩家剩余的金币，输出-1为未找到玩家信息
    public static int checkbank(String player_name) {
        DataBase dataBase = Economy.dataBase;
        if (!Economy.getInstance().getConfig().getBoolean("enableDataBase")) {
            if (player_file_exists(player_name)) {
                YamlConfiguration config = load_config(get_player_file(player_name));
                return config.getInt("bank");
            }
        }else {
            dataBase.connect();
            if (dataBase.isExist(player_name)){
                return Integer.parseInt((String) dataBase.getData(player_name, "Bank"));
            }
            dataBase.close();
        }
        return -1;
    }
}
