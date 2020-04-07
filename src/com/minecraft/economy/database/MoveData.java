package com.minecraft.economy.database;

import com.minecraft.economy.economyMain.Economy;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.minecraft.economy.apis.checkMoney.checkbank;
import static com.minecraft.economy.apis.checkMoney.checkmoney;

public class MoveData {

    public static boolean MoveDataFromLocal(){
        String path = Economy.getInstance().getDataFolder() + "/playerData";
        File files = new File(path);
        File[] array = files.listFiles();
        DataBase dataBase;
        String username, password, host, database, table;
        int port;

        username = Economy.getInstance().getConfig().getString("username");
        password = Economy.getInstance().getConfig().getString("password");
        host = Economy.getInstance().getConfig().getString("host");
        database = Economy.getInstance().getConfig().getString("database");
        port = Economy.getInstance().getConfig().getInt("port");
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
                int money = config.getInt("money");
                int bank = config.getInt("bank");

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
        String path = Economy.getInstance().getDataFolder() + "/playerData";
        File files = new File(path);
        File[] array = files.listFiles();
        DataBase dataBase = Economy.dataBase;
        assert array != null;
        dataBase.connect();

        for (String player_name : dataBase.getAllKey()){
            File file = new File(path, player_name+".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            int money = checkmoney(player_name);
            int bank = checkbank(player_name);

            config.set("money", money);
            config.set("bank", bank);
            config.save(file);

        }
        dataBase.close();
        return true;
    }

}
