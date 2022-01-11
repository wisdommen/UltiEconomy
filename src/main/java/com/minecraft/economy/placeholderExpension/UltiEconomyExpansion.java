package com.minecraft.economy.placeholderExpension;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import com.minecraft.economy.task.LeaderBoardTask;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * UltiEconomy PlaceHolderAPI变量
 */
public class UltiEconomyExpansion extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "ue";
    }

    @Override
    public String getAuthor() {
        return "wisdomme";
    }

    @Override
    public String getVersion() {
        return "2.0.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
        if (player == null) {
            return "";
        }
        if (params.contains("leaderboard")){
            try {
                int position = Integer.parseInt(params.split("_")[1]);
                Map.Entry<String, Double> entry = LeaderBoardTask.getPlayer(position);
                if (entry == null){
                    return null;
                }
                return String.format("%s: %.2f", entry.getKey(), entry.getValue());
            }catch (Exception e){
                return null;
            }
        }
        switch (params){
            case "money":
                return String.format("%.2f", economy.checkMoney(player.getName()));
            case "bank":
                return String.format("%.2f", economy.checkBank(player.getName()));
            default:
                return null;
        }
    }
}
