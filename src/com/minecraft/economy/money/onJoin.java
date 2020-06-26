package com.minecraft.economy.money;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import com.minecraft.economy.database.DataBase;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class onJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        Player player = event.getPlayer();
        DataBase dataBase = UltiEconomyMain.dataBase;
        UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
        if (!UltiEconomyMain.getInstance().getConfig().getBoolean("enableDataBase") && !UltiEconomyMain.getIsVaultInstalled()) {
            File file = economy.getPlayerFile(player.getName());
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            if (!file.exists()) {
                config.set("money", 1000);
                config.set("bank", 0);
                config.save(file);
            }
        }else if (UltiEconomyMain.getInstance().getConfig().getBoolean("enableDataBase") && !UltiEconomyMain.getIsVaultInstalled()){
            dataBase.connect();
            if (!dataBase.isExist(player.getName())){
                List<String> data = new ArrayList<>();
                data.add(player.getName());
                data.add(String.valueOf(1000));
                data.add(String.valueOf(0));
                dataBase.addData(data);
                dataBase.close();
            }
        }else if (!UltiEconomyMain.getInstance().getConfig().getBoolean("enableDataBase")&& UltiEconomyMain.getIsVaultInstalled()){
            File file = economy.getPlayerFile(player.getName());
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            if (!file.exists()) {
                config.set("money", 0);
                config.set("bank", 0);
                config.save(file);
            }
        }
    }
}
