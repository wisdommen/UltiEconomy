package com.minecraft.economy.placeholderExpension;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

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
        switch (params){
            case "money":
                return String.valueOf(economy.checkMoney(player.getName()));
            case "bank":
                return String.valueOf(economy.checkBank(player.getName()));
            default:
                return null;
        }
    }
}
