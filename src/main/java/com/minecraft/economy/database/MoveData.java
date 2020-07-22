package com.minecraft.economy.database;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import com.minecraft.economy.utils.DatabasePlayerTools;
import com.minecraft.economy.utils.DatabaseUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库和本地储存的转换
 */
public class MoveData {

    /**
     * 将数据从本地转移到数据库
     *
     * @return the boolean
     */
    public static boolean MoveDataFromLocal(){
        String path = UltiEconomyMain.getInstance().getDataFolder() + "/playerData";
        File files = new File(path);
        File[] array = files.listFiles();

        if(DatabaseUtils.createTable("player_economy_data", new String[]{"Name", "Money", "Bank"})){
            UltiEconomyMain.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "经济插件接入数据库成功！");
        }else {
            UltiEconomyMain.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "经济插件接入数据库失败！");
            UltiEconomyMain.getInstance().getConfig().set("enableDataBase", false);
            UltiEconomyMain.getInstance().saveConfig();
            UltiEconomyMain.getInstance().reloadConfig();
            return false;
        }

        for (File file : array){
            if (file.isFile()) {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                String player_name = file.getName().replace(".yml", "");
                System.out.println("传输"+player_name+"的数据中...");
                int money, bank;
                if (file.length()!=0) {
                    money = config.getInt("money");
                    bank = config.getInt("bank");
                }else {
                    money = 0;
                    bank = 0;
                }

                if (DatabasePlayerTools.isPlayerExist(player_name)) {
                    DatabasePlayerTools.updatePlayerData(player_name, "Money", String.valueOf(money));
                    DatabasePlayerTools.updatePlayerData(player_name, "Bank", String.valueOf(money));
                } else {
                    Map<String, String> data = new HashMap<>();
                    data.put("Name",player_name);
                    data.put("Money",String.valueOf(money));
                    data.put("Bank",String.valueOf(bank));
                    DatabasePlayerTools.insertPlayerData(data);
                }
            }

        }
        return true;
    }

    /**
     * 将数据从数据库转移到本地
     *
     * @return the boolean
     * @throws IOException the io exception
     */
    public static boolean MoveDataToLocal() throws IOException {
        String path = UltiEconomyMain.getInstance().getDataFolder() + "/playerData";
        File files = new File(path);
        File[] array = files.listFiles();
        UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
        assert array != null;

        for (String player_name : DatabasePlayerTools.getKeys()){
            File file = new File(path, player_name+".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            int money = economy.checkMoney(player_name);
            int bank = economy.checkBank(player_name);

            config.set("money", money);
            config.set("bank", bank);
            config.save(file);

        }
        return true;
    }

}
