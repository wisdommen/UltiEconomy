package com.minecraft.economy.money;

import com.minecraft.economy.economyMain.Economy;
import com.minecraft.economy.database.DataBase;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.minecraft.economy.apis.checkMoney.get_player_file;

public class onJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        Player player = event.getPlayer();
        DataBase dataBase = Economy.dataBase;
        if (!Economy.getInstance().getConfig().getBoolean("enableDataBase")) {
            File file = get_player_file(player.getName());
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            if (!file.exists()) {
                config.set("money", 1000);
                config.set("bank", 0);
                config.save(file);
            }
        }else {
            dataBase.connect();
            if (!dataBase.isExist(player.getName())){
                List<String> data = new ArrayList<>();
                data.add(player.getName());
                data.add(String.valueOf(1000));
                data.add(String.valueOf(0));
                dataBase.addData(data);
                dataBase.close();
            }
        }
    }
}
