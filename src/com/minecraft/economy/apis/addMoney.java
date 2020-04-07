package com.minecraft.economy.apis;


import com.minecraft.economy.economyMain.Economy;
import com.minecraft.economy.database.DataBase;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class addMoney {

    //Input: (String) 玩家名称，游戏名 {Player.getName()}
    //       (Integer) 想要添加的金币数量 （数额必须大于0）
    //Output: (Boolean) 是否添加成功，True成功，False失败
    public static boolean addTo(String player_name, Integer amount) {
        try {
            assert amount >= 0;
            DataBase dataBase = Economy.dataBase;

            if (!Economy.getInstance().getConfig().getBoolean("enableDataBase")) {
                if (checkMoney.player_file_exists(player_name)) {
                    YamlConfiguration config = checkMoney.load_config(checkMoney.get_player_file(player_name));
                    config.set("money", checkMoney.checkmoney(player_name) + amount);
                    config.save(checkMoney.get_player_file(player_name));
                }
                return true;
            }else {
                dataBase.connect();
                if (dataBase.isExist(player_name)) {
                    dataBase.increaseData(player_name, "Money", amount, "int");
                }
                dataBase.close();
                return true;
            }

        } catch (AssertionError e) {
            System.out.println("数额异常:" + e);
        } catch (IOException e) {
            System.out.println("保存数据异常：" + e);
        }
        return false;
    }

    public static boolean addToBank(String player_name, Integer amount) {
        try {
            assert amount >= 0;
            DataBase dataBase = Economy.dataBase;

            if (!Economy.getInstance().getConfig().getBoolean("enableDataBase")) {
                if (checkMoney.player_file_exists(player_name)) {
                    YamlConfiguration config = checkMoney.load_config(checkMoney.get_player_file(player_name));
                    config.set("bank", checkMoney.checkmoney(player_name) + amount);
                    config.save(checkMoney.get_player_file(player_name));
                }
                return true;
            }else {
                dataBase.connect();
                if (dataBase.isExist(player_name)) {
                    dataBase.increaseData(player_name, "Bank", amount, "int");
                }
                dataBase.close();
                return true;
            }

        } catch (AssertionError e) {
            System.out.println("数额异常:" + e);
        } catch (IOException e) {
            System.out.println("保存数据异常：" + e);
        }
        return false;
    }
}
