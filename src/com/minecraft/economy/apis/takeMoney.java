package com.minecraft.economy.apis;

import com.minecraft.economy.economyMain.Economy;
import com.minecraft.economy.database.DataBase;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;


public class takeMoney {

    //Input: (String) 玩家名称，游戏名 {Player.getName()}
    //       (Integer) 想要扣除的金币数量 （数额必须大于0）
    //Output: (Boolean) 是否移除成功，True移除成功，False失败
    public static boolean takeFrom(String player_name, Integer amount) {
        try {
            DataBase dataBase = Economy.dataBase;
            assert amount >= 0;
            if (!Economy.getInstance().getConfig().getBoolean("enableDataBase")) {
                if (checkMoney.player_file_exists(player_name)) {
                    if (checkMoney.checkmoney(player_name) >= amount) {
                        YamlConfiguration config = checkMoney.load_config(checkMoney.get_player_file(player_name));
                        config.set("money", checkMoney.checkmoney(player_name) - amount);
                        config.save(checkMoney.get_player_file(player_name));
                        return true;
                    }
                }
            }else {
                dataBase.connect();
                if (dataBase.isExist(player_name)){
                    dataBase.reduceData(player_name, "Money", amount, "int");
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

    public static boolean takeFromBank(String player_name, Integer amount) {
        try {
            assert amount >= 0;
            DataBase dataBase = Economy.dataBase;

            if (!Economy.getInstance().getConfig().getBoolean("enableDataBase")) {
                if (checkMoney.player_file_exists(player_name)) {
                    if (checkMoney.checkmoney(player_name) >= amount) {
                        YamlConfiguration config = checkMoney.load_config(checkMoney.get_player_file(player_name));
                        config.set("bank", checkMoney.checkmoney(player_name) - amount);
                        config.save(checkMoney.get_player_file(player_name));
                        return true;
                    }
                }
            }else {
                dataBase.connect();
                if (dataBase.isExist(player_name)){
                    dataBase.reduceData(player_name, "Bank", amount, "int");
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
