package com.minecraft.economy.money;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import com.minecraft.economy.utils.DatabasePlayerTools;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 玩家入服
 */
public class onJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        Player player = event.getPlayer();
        UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
        if (!UltiEconomyMain.isDatabaseEnabled) {
            int money = 1000;
            if (UltiEconomyMain.getIsVaultInstalled()) {
                money = 0;
            }

            File file = economy.getPlayerFile(player.getName());
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            if (!file.exists()) {
                config.set("money", money);
                config.set("bank", 0);
                config.save(file);
            }
        } else {
            if (!DatabasePlayerTools.isPlayerExist(player.getName())) {
                Map<String, String> data = new HashMap<>();
                data.put("Name", player.getName());
                data.put("Money",String.valueOf(1000));
                data.put("Bank",String.valueOf(0));
                DatabasePlayerTools.insertPlayerData(data);
            }
        }
    }
}
