package com.minecraft.economy.interest;

import com.minecraft.economy.apis.checkMoney;
import com.minecraft.economy.database.DataBase;
import com.minecraft.economy.economyMain.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.minecraft.economy.apis.addMoney.addToBank;


public class Interest extends BukkitRunnable {

    @Override
    public void run() {
        String path = Economy.getInstance().getDataFolder() + "/playerData";
        File file = new File(path);
        File[] array = file.listFiles();
        DataBase dataBase = Economy.dataBase;
        assert array != null;
        double interest_rate = Economy.getInstance().getConfig().getDouble("interestRate");

        if (!Economy.getInstance().getConfig().getBoolean("enableDataBase")) {
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
                    double interests = ckmoney * (interest_rate / 10000);
                    if (ckmoney >= 10000) {
                        config.set("bank", ckmoney + interests);
                        try {
                            config.save(f);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
                            if (value.getName().equals(onlineplayer.getName() + ".yml") && interests > 0) {
                                onlineplayer.sendMessage(ChatColor.GOLD + "你已收到来自银行的利息！");
                                onlineplayer.sendMessage(ChatColor.GOLD + "利息金额：" + ((int) interests));
                            }
                        }
                    }
                } else if (value.isDirectory()) {
                    Economy.getInstance().getServer().getConsoleSender().sendMessage("出错！");
                }
            }
        } else {
            dataBase.connect();
            List<String> keys = dataBase.getAllKey();
            for (String player_name : keys) {
                dataBase.connect();
                if (dataBase.isExist(player_name)) {
                    int bank = checkMoney.checkbank(player_name);
                    double interests = bank * (interest_rate / 10000);
                    if (interests > 0) {
                        if (addToBank(player_name, (int) interests)) {
                            for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
                                if (player_name.equals(onlineplayer.getName())) {
                                    onlineplayer.sendMessage(ChatColor.GOLD + "你已收到来自银行的利息！");
                                    onlineplayer.sendMessage(ChatColor.GOLD + "利息金额：" + ((int) interests));
                                }
                            }
                        }
                    }
                }
                dataBase.close();
            }
            dataBase.close();
        }
    }
}
