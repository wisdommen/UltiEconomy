package com.minecraft.economy.vault;

import com.minecraft.economy.economyMain.UltiEconomyMain;
import com.minecraft.economy.utils.DatabasePlayerTools;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EconomyImplementer implements Economy {

    private static final UltiEconomyMain plugin = UltiEconomyMain.getInstance();

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "UltiEconomy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        return null;
    }

    @Override
    public String currencyNamePlural() {
        return null;
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Override
    public boolean hasAccount(String playerName) {
        if (!UltiEconomyMain.isDatabaseEnabled) {
            return new File(UltiEconomyMain.getInstance().getDataFolder() + "/playerData", playerName + ".yml").exists();
        } else {
            return DatabasePlayerTools.isPlayerExist(playerName);
        }
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return hasAccount(player.getName());
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return false;
    }

    @Override
    public double getBalance(String playerName) {
        if (!hasAccount(playerName)){
            return 0;
        }
        if (!UltiEconomyMain.isDatabaseEnabled) {
            YamlConfiguration config = loadConfig(getPlayerFile(playerName));
            return config.getInt("money");
        } else {
            return Math.round(Float.parseFloat(DatabasePlayerTools.getPlayerData(playerName, "Money")));
        }
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return getBalance(player.getName());
    }

    @Override
    public double getBalance(String playerName, String world) {
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return 0;
    }

    @Override
    public boolean has(String playerName, double amount) {
        return getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return has(player.getName(), amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        if (!hasAccount(playerName)){
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, null);
        }
        try {
            assert amount >= 0;
        } catch (AssertionError e) {
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "数额异常");
        }
        if (!UltiEconomyMain.isDatabaseEnabled) {
            if (getBalance(playerName) < amount) {
                return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "现金不足！");
            }
            YamlConfiguration config = loadConfig(getPlayerFile(playerName));
            config.set("money", getBalance(playerName) - amount);
            try {
                config.save(getPlayerFile(playerName));
                return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, null);
            } catch (IOException e) {
                return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "保存数据异常！");
            }
        }
        if (DatabasePlayerTools.decreasePlayerData("Money", playerName, amount)){
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, null);
        }
        return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return withdrawPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return new EconomyResponse(amount, getBalance(player.getName()), EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        if (!hasAccount(playerName)){
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, null);
        }
        try {
            assert amount >= 0;
        } catch (AssertionError e) {
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "数额异常");
        }
        if (!UltiEconomyMain.isDatabaseEnabled) {
            YamlConfiguration config = loadConfig(getPlayerFile(playerName));
            config.set("money", getBalance(playerName) + amount);
            try {
                config.save(getPlayerFile(playerName));
                return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, null);
            } catch (IOException e) {
                return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, "保存数据异常！");
            }
        } else {
            if (DatabasePlayerTools.increasePlayerData("Money", playerName, amount)){
                return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, null);
            }
            return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.FAILURE, null);
        }
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return depositPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return new EconomyResponse(amount, getBalance(player.getName()), EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        double money = UltiEconomyMain.getInstance().getConfig().getDouble("initial_money");
        if (!UltiEconomyMain.isDatabaseEnabled) {
            File file = getPlayerFile(player);
            if (!file.exists()) {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.set("money", money);
                config.set("bank", 0);
                try {
                    config.save(file);
                } catch (IOException e) {
                    return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
                }
            }
            return new EconomyResponse(0, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
        }
        if (!DatabasePlayerTools.isPlayerExist(player)) {
            Map<String, String> data = new HashMap<>();
            data.put("Name", player);
            data.put("Money", String.valueOf(money));
            data.put("Bank", String.valueOf(0));
            DatabasePlayerTools.insertPlayerData(data);
            return new EconomyResponse(0, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return createBank(name, player.getName());
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        if (!UltiEconomyMain.isDatabaseEnabled) {
            if (hasAccount(name)) {
                YamlConfiguration config = loadConfig(getPlayerFile(name));
                return new EconomyResponse(0, config.getInt("bank"), EconomyResponse.ResponseType.SUCCESS, null);
            }
        } else {
            if (DatabasePlayerTools.isPlayerExist(name)) {
                return new EconomyResponse(0, Math.round(Float.parseFloat(DatabasePlayerTools.getPlayerData(name, "Bank"))), EconomyResponse.ResponseType.SUCCESS, null);
            }
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        if (bankBalance(name).balance>=amount){
            return new EconomyResponse(amount, bankBalance(name).balance, EconomyResponse.ResponseType.SUCCESS, null);
        }
        return new EconomyResponse(amount, bankBalance(name).balance, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        if (!hasAccount(name)) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
        }
        try {
            assert amount >= 0;
        } catch (AssertionError e) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
        }
        if (!UltiEconomyMain.getInstance().getConfig().getBoolean("enableDataBase")) {
            YamlConfiguration config = loadConfig(getPlayerFile(name));
            config.set("bank", bankBalance(name).balance + amount);
            try {
                config.save(getPlayerFile(name));
                return new EconomyResponse(amount, bankBalance(name).balance, EconomyResponse.ResponseType.SUCCESS, null);
            } catch (IOException e) {
                System.out.println(ChatColor.RED+"[WARNING]保存数据异常：" +ChatColor.WHITE+ e);
                return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
            }
        }
        if (DatabasePlayerTools.increasePlayerData("Bank", name, amount)){
            return new EconomyResponse(amount, bankBalance(name).balance, EconomyResponse.ResponseType.SUCCESS, null);
        }
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, null);
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, null);
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        double money = UltiEconomyMain.getInstance().getConfig().getDouble("initial_money");
        if (!UltiEconomyMain.isDatabaseEnabled) {
            File file = getPlayerFile(playerName);
            if (!file.exists()) {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.set("money", money);
                config.set("bank", 0);
                try {
                    config.save(file);
                } catch (IOException e) {
                    return false;
                }
            }
            return true;
        }
        if (!DatabasePlayerTools.isPlayerExist(playerName)) {
            Map<String, String> data = new HashMap<>();
            data.put("Name", playerName);
            data.put("Money", String.valueOf(money));
            data.put("Bank", String.valueOf(0));
            DatabasePlayerTools.insertPlayerData(data);
            return true;
        }
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return createPlayerAccount(player.getName());
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return false;
    }

    private YamlConfiguration loadConfig(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }

    private File getPlayerFile(String player_name) {
        return new File(UltiEconomyMain.getInstance().getDataFolder() + "/playerData", player_name + ".yml");
    }
}
