package com.minecraft.economy.interest;

import com.minecraft.economy.economyMain.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;


public class Interest extends BukkitRunnable {

    @Override
    public void run() {
        String path = Economy.getInstance().getDataFolder() + "/playerData";
        File file = new File(path);
        File[] array = file.listFiles();
        assert array != null;
        for (File value : array) {
            if (value.isFile()) {
                File folder = new File(Economy.getInstance().getDataFolder() + "/playerData");
                File f = new File(folder, value.getName());
                YamlConfiguration config;
                if (!f.exists()) {
                    config = new YamlConfiguration();
                } else {
                    config = YamlConfiguration.loadConfiguration(f);
                }
                double ckmoney = config.getDouble("bank");

                if (ckmoney >= 10000) {
                    double interest_rate = Economy.getInstance().getConfig().getDouble("interestRate");
                    double interests = ckmoney * (interest_rate / 10000);
                    config.set("bank", ckmoney + interests);
                    try {
                        config.save(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
                        if (value.getName().equals(onlineplayer.getName() + ".yml") && interests > 0) {
                            onlineplayer.sendMessage(ChatColor.GOLD + "你已收到来自银行的利息！");
                            onlineplayer.sendMessage(ChatColor.GOLD + "利息金额：" + interests);
                        }
                    }
                }
            } else if (value.isDirectory()) {
                Economy.getInstance().getServer().getConsoleSender().sendMessage("出错！");
            }
        }
    }
}
