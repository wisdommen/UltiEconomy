package com.minecraft.economy.apis;


import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class addMoney {

    //Input: (String) 玩家名称，游戏名 {Player.getName()}
    //       (Integer) 想要添加的金币数量 （数额必须大于0）
    //Output: (Boolean) 是否添加成功，True成功，False失败
    public static boolean addTo(String player_name, Integer amount) {
        try {
            assert amount >= 0;
            if (checkMoney.player_file_exists(player_name)) {
                if (checkMoney.check(player_name) > 0) {
                    YamlConfiguration config = checkMoney.load_config(checkMoney.get_player_file(player_name));
                    config.set("money", checkMoney.check(player_name) + amount);
                    config.save(checkMoney.get_player_file(player_name));
                    return true;
                }
            } else {
                return false;
            }
        } catch (AssertionError e) {
            System.out.println("数额异常:"+e);
        }catch (IOException e){
            System.out.println("保存数据异常："+e);
        }
        return false;
    }
}
