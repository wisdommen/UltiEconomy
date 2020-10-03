package com.minecraft.economy.beans;

import com.minecraft.economy.economyMain.UltiEconomyMain;
import com.minecraft.economy.utils.DatabasePlayerTools;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static com.minecraft.economy.apis.UltiEconomy.getNumber;

public class PlayerEcoData {

    OfflinePlayer player;
    Integer money;
    Integer savings;
    Integer points;

    public PlayerEcoData(@NotNull OfflinePlayer player) {
        this.player = player;
        this.money = checkMoney(player.getName());
        this.savings = checkBank(player.getName());
    }

    private Integer checkMoney(String player_name) {
            if (!UltiEconomyMain.isDatabaseEnabled) {
                if (playerFileExists(player_name)) {
                    YamlConfiguration config = loadConfig(getPlayerFile(player_name));
                    return config.getInt("money");
                }
            } else {
                if (DatabasePlayerTools.isPlayerExist(player_name)) {
                    return Math.round(Float.parseFloat(DatabasePlayerTools.getPlayerData(player_name, "Money")));
                }
            }
            return -1;
    }

    private Integer checkBank(String player_name) {
        if (!UltiEconomyMain.isDatabaseEnabled) {
            if (playerFileExists(player_name)) {
                YamlConfiguration config = loadConfig(getPlayerFile(player_name));
                return config.getInt("bank");
            }
        } else {
            if (DatabasePlayerTools.isPlayerExist(player_name)) {
                return Math.round(Float.parseFloat(DatabasePlayerTools.getPlayerData(player_name, "Bank")));
            }
        }
        return -1;
    }

    private @NotNull Boolean playerFileExists(String player_name) {
        return getPlayerFile(player_name).exists();
    }

    private @NotNull YamlConfiguration loadConfig(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }

    private @NotNull File getPlayerFile(String player_name) {
        return new File(UltiEconomyMain.getInstance().getDataFolder() + "/playerData", player_name + ".yml");
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getSavings() {
        return savings;
    }

    public void setSavings(Integer savings) {
        this.savings = savings;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
