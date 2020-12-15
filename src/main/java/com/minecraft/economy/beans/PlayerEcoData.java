package com.minecraft.economy.beans;

import com.minecraft.economy.economyMain.UltiEconomyMain;
import com.minecraft.economy.utils.DatabasePlayerTools;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class PlayerEcoData {

    OfflinePlayer player;
    Double money;
    Double savings;
    Double points;

    public PlayerEcoData(@NotNull OfflinePlayer player) {
        this.player = player;
        this.money = checkMoney(player.getName());
        this.savings = checkBank(player.getName());
    }

    private Double checkMoney(String player_name) {
            if (!UltiEconomyMain.isDatabaseEnabled) {
                if (playerFileExists(player_name)) {
                    YamlConfiguration config = loadConfig(getPlayerFile(player_name));
                    return config.getDouble("money");
                }
            } else {
                if (DatabasePlayerTools.isPlayerExist(player_name)) {
                    return Double.parseDouble(DatabasePlayerTools.getPlayerData(player_name, "Money"));
                }
            }
            return -1.0;
    }

    private Double checkBank(String player_name) {
        if (!UltiEconomyMain.isDatabaseEnabled) {
            if (playerFileExists(player_name)) {
                YamlConfiguration config = loadConfig(getPlayerFile(player_name));
                return config.getDouble("bank");
            }
        } else {
            if (DatabasePlayerTools.isPlayerExist(player_name)) {
                return Double.parseDouble(DatabasePlayerTools.getPlayerData(player_name, "Bank"));
            }
        }
        return -1.0;
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

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getSavings() {
        return savings;
    }

    public void setSavings(Double savings) {
        this.savings = savings;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }
}
