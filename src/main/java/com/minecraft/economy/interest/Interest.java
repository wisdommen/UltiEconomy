package com.minecraft.economy.interest;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;


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
            if (value.isFile()) {
                String playerName = value.getName().replace(".yml", "");
                int savings = economy.checkBank(playerName);
                double interests = savings * (interestRate / 10000);
                if (savings >= 10000) {
                    economy.addToBank(playerName, (int) interests);
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (value.getName().equals(onlinePlayer.getName() + ".yml") && interests > 0) {
                            onlinePlayer.sendMessage(ChatColor.GOLD + "你已收到来自银行的利息！");
                            onlinePlayer.sendMessage(ChatColor.GOLD + "利息金额：" + ((int) interests));
                        }
                    }
                }
            } else if (value.isDirectory()) {
                UltiEconomyMain.getInstance().getServer().getConsoleSender().sendMessage("出错！");
            }
        }
    }
}
