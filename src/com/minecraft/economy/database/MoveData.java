package com.minecraft.economy.database;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoveData {

    public static boolean MoveDataFromLocal(){
        String path = UltiEconomyMain.getInstance().getDataFolder() + "/playerData";
        File files = new File(path);
        File[] array = files.listFiles();
        DataBase dataBase;
        String username, password, host, database, table;
        int port;

        username = UltiEconomyMain.getInstance().getConfig().getString("username");
        password = UltiEconomyMain.getInstance().getConfig().getString("password");
        host = UltiEconomyMain.getInstance().getConfig().getString("host");
        database = UltiEconomyMain.getInstance().getConfig().getString("database");
        port = UltiEconomyMain.getInstance().getConfig().getInt("port");
        table = "player_economy_data";

        dataBase = new LinkedDataBase(new String[]{"Name", "Money", "Bank"});

        dataBase.login(host, String.valueOf(port), username, password, database, table);
        assert array != null;
        dataBase.connect();
        dataBase.createTable();
        dataBase.close();

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

                dataBase.connect();
                if (dataBase.isExist(player_name)) {
                    dataBase.setData(player_name, "Money", money);
                    dataBase.setData(player_name, "Bank", bank);
                } else {
                    List<String> data = new ArrayList<>();
                    data.add(player_name);
                    data.add(String.valueOf(money));
                    data.add(String.valueOf(bank));
                    dataBase.addData(data);
                    dataBase.close();
                }
                dataBase.close();
            }

        }
        return true;
    }

    public static boolean MoveDataToLocal() throws IOException {
        String path = UltiEconomyMain.getInstance().getDataFolder() + "/playerData";
        File files = new File(path);
        File[] array = files.listFiles();
        DataBase dataBase = UltiEconomyMain.dataBase;
        UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
        assert array != null;
        dataBase.connect();

        for (String player_name : dataBase.getAllKey()){
            File file = new File(path, player_name+".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            int money = economy.checkMoney(player_name);
            int bank = economy.checkBank(player_name);

            config.set("money", money);
            config.set("bank", bank);
            config.save(file);

        }
        dataBase.close();
        return true;
    }

}
