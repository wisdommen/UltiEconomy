package com.minecraft.economy.interest;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;


/**
 * 利息任务
 */
public class Interest extends BukkitRunnable {

    @Override
    public void run() {
        String path = UltiEconomyMain.getInstance().getDataFolder() + "/playerData";
        File file = new File(path);
        File[] array = file.listFiles();
        UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
        assert array != null;
        double interestRate = UltiEconomyMain.getInstance().getConfig().getDouble("interestRate");

        for (File value : array) {
            if (!value.isFile()) {
                return;
            }
            String playerName = value.getName().replace(".yml", "");
            double savings = economy.checkBank(playerName);
            double interests = savings * (interestRate / 10000);
            economy.addToBank(playerName, interests);
            Player player = Bukkit.getPlayerExact(playerName);
            if (player != null && player.isOnline() && interests > 0) {
                player.sendMessage(ChatColor.GOLD + "你已收到来自银行的利息！");
                player.sendMessage(ChatColor.GOLD + "利息金额：" + interests);
            }
        }
    }
}
