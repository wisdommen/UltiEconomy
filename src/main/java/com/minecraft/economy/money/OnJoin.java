package com.minecraft.economy.money;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.beans.PlayerEcoData;
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

//import static com.minecraft.economy.apis.UltiEconomy.playerDataMap;

/**
 * 玩家入服
 */
public class OnJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        Player player = event.getPlayer();
        UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
        double money = UltiEconomyMain.getInstance().getConfig().getDouble("initial_money");
        if (!UltiEconomyMain.isDatabaseEnabled) {

            File file = economy.getPlayerFile(player.getName());
            if (!file.exists()) {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.set("money", money);
                config.set("bank", 0);
                config.save(file);
            }
            return;
        }
        if (!DatabasePlayerTools.isPlayerExist(player.getName())) {
            Map<String, String> data = new HashMap<>();
            data.put("Name", player.getName());
            data.put("Money", String.valueOf(money));
            data.put("Bank", String.valueOf(0));
            DatabasePlayerTools.insertPlayerData(data);
        }
    }
}
