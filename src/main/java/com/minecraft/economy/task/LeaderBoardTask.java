package com.minecraft.economy.task;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;


public class LeaderBoardTask extends BukkitRunnable {
    private static LinkedList<Map.Entry<String, Double>> list;

    @Override
    public void run() {
        UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
        Map<String, Double> leaderboard = new HashMap<>();
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            String playerName = player.getName();
            Double money = economy.checkMoney(playerName);
            Double bank = economy.checkBank(playerName);
            Double total = money + bank;
            leaderboard.put(playerName, total);
        }
        list = new LinkedList<>(leaderboard.entrySet());
        list.sort(Map.Entry.comparingByValue());
    }

    public static Map.Entry<String, Double> getPlayer(int position){
        if (position >= list.size()){
            return null;
        }
        return list.get(position);
    }
}
