package com.minecraft.economy.apis;

import com.minecraft.economy.economyMain.UltiEconomyMain;
import com.minecraft.economy.utils.DatabasePlayerTools;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * UltiEconomyAPI 实现类
 */
public class UltiEconomy implements UltiEconomyAPI {

    @Override
    public File getPlayerFile(String player_name) {
        return new File(UltiEconomyMain.getInstance().getDataFolder() + "/playerData", player_name + ".yml");
    }

    @Override
    public YamlConfiguration loadConfig(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public Boolean playerFileExists(String player_name) {
        return getPlayerFile(player_name).exists();
    }

    @Override
    public Double checkMoney(String player_name) {
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

    /**
     * Gets number.
     *
     * @param str the str
     * @return the number
     */
    public static String getNumber(@NotNull String str) {
        List<String> list = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ".");
        String[] strings = str.split("");
        StringBuilder result = new StringBuilder();
        for (String each : strings) {
            if (list.contains(each)) {
                result.append(each);
            }
        }
        return result.toString();
    }

    @Override
    public Double checkBank(String player_name) {
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

    @Override
    public Boolean setMoney(String player_name, Double amount) {
        try {
            assert amount >= 0;
        } catch (AssertionError e) {
            System.out.println("数额异常:" + e);
            return false;
        }
        if (!UltiEconomyMain.isDatabaseEnabled) {
            if (!playerFileExists(player_name)) {
                return false;
            }
            YamlConfiguration config = loadConfig(getPlayerFile(player_name));
            config.set("money", amount);
            try {
                config.save(getPlayerFile(player_name));
                return true;
            } catch (IOException e) {
                System.out.println(ChatColor.RED+"[WARNING]保存数据异常：" +ChatColor.WHITE+ e);
                return false;
            }
        } else {
            if (DatabasePlayerTools.isPlayerExist(player_name)) {
                return DatabasePlayerTools.updatePlayerData(player_name, "Money", String.valueOf(amount));
            }
        }
        return false;
    }

    @Override
    public Boolean setBank(String player_name, Double amount) {
        try {
            assert amount >= 0;
        } catch (AssertionError e) {
            System.out.println("数额异常:" + e);
            return false;
        }
        if (!UltiEconomyMain.isDatabaseEnabled) {
            if (!playerFileExists(player_name)) {
                return false;
            }
            YamlConfiguration config = loadConfig(getPlayerFile(player_name));
            config.set("bank", amount);
            try {
                config.save(getPlayerFile(player_name));
                return true;
            } catch (IOException e) {
                System.out.println(ChatColor.RED+"[WARNING]保存数据异常：" +ChatColor.WHITE+ e);
                return false;
            }
        } else {
            if (DatabasePlayerTools.isPlayerExist(player_name)) {
                return DatabasePlayerTools.updatePlayerData(player_name, "Bank", String.valueOf(amount));
            }
        }
        return false;
    }

    @Override
    public Boolean addTo(String player_name, Double amount) {
        try {
            assert amount >= 0;
        } catch (AssertionError e) {
            System.out.println("数额异常:" + e);
            return false;
        }
        if (!UltiEconomyMain.isDatabaseEnabled) {
            if (!playerFileExists(player_name)) {
                return false;
            }
            YamlConfiguration config = loadConfig(getPlayerFile(player_name));
            config.set("money", checkMoney(player_name) + amount);
            try {
                config.save(getPlayerFile(player_name));
                return true;
            } catch (IOException e) {
                System.out.println(ChatColor.RED+"[WARNING]保存数据异常：" +ChatColor.WHITE+ e);
                return false;
            }
        } else {
            if (DatabasePlayerTools.isPlayerExist(player_name)) {
                return DatabasePlayerTools.increasePlayerData("Money", player_name, amount);
            }
        }
        return false;
    }

    @Override
    public Boolean addToBank(String player_name, Double amount) {
        try {
            assert amount >= 0;
        } catch (AssertionError e) {
            System.out.println("数额异常:" + e);
        }
        if (!UltiEconomyMain.getInstance().getConfig().getBoolean("enableDataBase")) {
            if (!playerFileExists(player_name)) {
                return false;
            }
            YamlConfiguration config = loadConfig(getPlayerFile(player_name));
            config.set("bank", checkBank(player_name) + amount);
            try {
                config.save(getPlayerFile(player_name));
                return true;
            } catch (IOException e) {
                System.out.println(ChatColor.RED+"[WARNING]保存数据异常：" +ChatColor.WHITE+ e);
                return false;
            }
        }
        if (!DatabasePlayerTools.isPlayerExist(player_name)) {
            return false;
        }
        return DatabasePlayerTools.increasePlayerData("Bank", player_name, amount);
    }

    @Override
    public Boolean takeFrom(String player_name, Double amount) {
        try {
            assert amount >= 0;
        } catch (AssertionError e) {
            System.out.println("数额异常:" + e);
            return false;
        }
        if (!UltiEconomyMain.getInstance().getConfig().getBoolean("enableDataBase")) {
            if (checkMoney(player_name) < amount) {
                return false;
            }
            YamlConfiguration config = loadConfig(getPlayerFile(player_name));
            config.set("money", checkMoney(player_name) - amount);
            try {
                config.save(getPlayerFile(player_name));
                return true;
            } catch (IOException e) {
                System.out.println(ChatColor.RED+"[WARNING]保存数据异常：" +ChatColor.WHITE+ e);
                return false;
            }
        }
        if (!DatabasePlayerTools.isPlayerExist(player_name)) {
            return false;
        }
        return DatabasePlayerTools.decreasePlayerData("Money", player_name, amount);
    }

    @Override
    public Boolean takeFromBank(String player_name, Double amount) {
        try {
            assert amount >= 0;
        } catch (AssertionError e) {
            System.out.println("数额异常:" + e);
            return false;
        }

        if (!UltiEconomyMain.isDatabaseEnabled) {
            if (!(playerFileExists(player_name) && checkBank(player_name) >= amount)) {
                return false;
            }
            YamlConfiguration config = loadConfig(getPlayerFile(player_name));
            config.set("bank", checkBank(player_name) - amount);
            try {
                config.save(getPlayerFile(player_name));
                return true;
            } catch (IOException e) {
                System.out.println(ChatColor.RED+"[WARNING]保存数据异常：" +ChatColor.WHITE+ e);
                return false;
            }
        } else {
            if (DatabasePlayerTools.isPlayerExist(player_name)) {
                return DatabasePlayerTools.decreasePlayerData("Bank", player_name, amount);
            }
        }
        return false;
    }

    @Override
    public Boolean transferMoney(String payer, String payee, Double amount) {
        try {
            assert amount >= 0;
        } catch (AssertionError e) {
            System.out.println(ChatColor.RED+"[WARNING]保存数据异常：" +ChatColor.WHITE+ e);
            return false;
        }
        if (!UltiEconomyMain.isDatabaseEnabled) {
            if (!(playerFileExists(payer) && playerFileExists(payee) && checkMoney(payer) >= amount)) {
                return false;
            }
            return takeFrom(payer, amount) && addTo(payee, amount);
        } else {
            List<UUID> others = new ArrayList<>();
            UUID payeeUUID = DatabasePlayerTools.increasePlayerDataStandby("Money", payee, amount);
            others.add(payeeUUID);
            return DatabasePlayerTools.decreasePlayerData("Money", payer, amount, others);
        }
    }

    @Override
    public Boolean transferMoneyToBank(String player_name, Double amount) {
        try {
            assert amount >= 0;
        } catch (AssertionError e) {
            System.out.println("数额异常:" + e);
            return false;
        }
        if (!UltiEconomyMain.isDatabaseEnabled) {
            return takeFrom(player_name, amount) && addToBank(player_name, amount);
        } else {
            List<UUID> others = new ArrayList<>();
            UUID uuid = DatabasePlayerTools.increasePlayerDataStandby("Bank", player_name, amount);
            others.add(uuid);
            return DatabasePlayerTools.decreasePlayerData("Money", player_name, amount, others);
        }
    }

    @Override
    public Boolean transferBankToMoney(String player_name, Double amount) {
        try {
            assert amount >= 0;
        } catch (AssertionError e) {
            System.out.println("数额异常:" + e);
            return false;
        }
        if (!UltiEconomyMain.isDatabaseEnabled) {
            return takeFromBank(player_name, amount) && addTo(player_name, amount);
        } else {
            List<UUID> others = new ArrayList<>();
            UUID uuid = DatabasePlayerTools.decreasePlayerDataStandby("Bank", player_name, amount);
            others.add(uuid);
            return DatabasePlayerTools.increasePlayerData("Money", player_name, amount, others);
        }
    }
}
