package com.minecraft.economy.apis;

import com.minecraft.economy.database.DataBase;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

public class UltiEconomy implements UltiEconomyAPI{

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
    public Integer checkMoney(String player_name) {
        if (!UltiEconomyMain.getIsVaultInstalled()) {
            DataBase dataBase = UltiEconomyMain.dataBase;

            if (!UltiEconomyMain.getInstance().getConfig().getBoolean("enableDataBase")) {
                if (playerFileExists(player_name)) {
                    YamlConfiguration config = loadConfig(getPlayerFile(player_name));
                    return config.getInt("money");
                }
            } else {
                dataBase.connect();
                if (dataBase.isExist(player_name)) {
                    return Integer.parseInt((String) dataBase.getData(player_name, "Money"));
                }
                dataBase.close();
            }
            return -1;
        } else {
            if (Bukkit.getPlayer(player_name) != null) {
                String money = Pattern.compile("[^0-9]").matcher(UltiEconomyMain.getEcon().format(UltiEconomyMain.getEcon().getBalance(Bukkit.getPlayer(player_name)))).replaceAll("").trim();
                return Integer.parseInt(money);
            } else {
                return -1;
            }
        }
    }

    @Override
    public Integer checkBank(String player_name) {
        DataBase dataBase = UltiEconomyMain.dataBase;
        if (!UltiEconomyMain.getInstance().getConfig().getBoolean("enableDataBase")) {
            if (playerFileExists(player_name)) {
                YamlConfiguration config = loadConfig(getPlayerFile(player_name));
                return config.getInt("bank");
            }
        } else {
            dataBase.connect();
            if (dataBase.isExist(player_name)) {
                return Integer.parseInt((String) dataBase.getData(player_name, "Bank"));
            }
            dataBase.close();
        }
        return -1;
    }

    @Override
    public Boolean addTo(String player_name, Integer amount) {
        if (!UltiEconomyMain.getIsVaultInstalled()) {
            try {
                assert amount >= 0;
                DataBase dataBase = UltiEconomyMain.dataBase;

                if (!UltiEconomyMain.getInstance().getConfig().getBoolean("enableDataBase")) {
                    if (playerFileExists(player_name)) {
                        YamlConfiguration config = loadConfig(getPlayerFile(player_name));
                        config.set("money", checkMoney(player_name) + amount);
                        config.save(getPlayerFile(player_name));
                    }
                } else {
                    dataBase.connect();
                    if (dataBase.isExist(player_name)) {
                        dataBase.increaseData(player_name, "Money", amount, "int");
                    }
                    dataBase.close();
                }
                return true;

            } catch (AssertionError e) {
                System.out.println("数额异常:" + e);
            } catch (IOException e) {
                System.out.println("保存数据异常：" + e);
            }
            return false;
        }else {
            if (Bukkit.getPlayer(player_name)!=null) {
                EconomyResponse r = UltiEconomyMain.getEcon().depositPlayer(Bukkit.getPlayer(player_name), amount);
                return r.transactionSuccess();
            }else {
                return false;
            }
        }
    }

    @Override
    public Boolean addToBank(String player_name, Integer amount) {
        try {
            assert amount >= 0;
            DataBase dataBase = UltiEconomyMain.dataBase;

            if (!UltiEconomyMain.getInstance().getConfig().getBoolean("enableDataBase")) {
                if (playerFileExists(player_name)) {
                    YamlConfiguration config = loadConfig(getPlayerFile(player_name));
                    config.set("bank", checkBank(player_name) + amount);
                    config.save(getPlayerFile(player_name));
                }
            }else {
                dataBase.connect();
                if (dataBase.isExist(player_name)) {
                    dataBase.increaseData(player_name, "Bank", amount, "int");
                }
                dataBase.close();
            }
            return true;

        } catch (AssertionError e) {
            System.out.println("数额异常:" + e);
        } catch (IOException e) {
            System.out.println("保存数据异常：" + e);
        }
        return false;
    }

    @Override
    public Boolean takeFrom(String player_name, Integer amount) {
        if (!UltiEconomyMain.getIsVaultInstalled()) {
            try {
                DataBase dataBase = UltiEconomyMain.dataBase;
                assert amount >= 0;
                if (!UltiEconomyMain.getInstance().getConfig().getBoolean("enableDataBase")) {
                    if (checkMoney(player_name) >= amount) {
                        YamlConfiguration config = loadConfig(getPlayerFile(player_name));
                        config.set("money", checkMoney(player_name) - amount);
                        config.save(getPlayerFile(player_name));
                        return true;
                    }
                } else {
                    dataBase.connect();
                    if (dataBase.isExist(player_name)) {
                        dataBase.reduceData(player_name, "Money", amount, "int");
                    }
                    dataBase.close();
                    return true;
                }
            } catch (AssertionError e) {
                System.out.println("数额异常:" + e);
            } catch (IOException e) {
                System.out.println("保存数据异常：" + e);
            }
            return false;
        } else {
            if (Bukkit.getPlayer(player_name) != null && UltiEconomyMain.getEcon().has(Bukkit.getOfflinePlayer(Objects.requireNonNull(Bukkit.getPlayer(player_name)).getUniqueId()), amount)) {
                EconomyResponse r = UltiEconomyMain.getEcon().withdrawPlayer(Bukkit.getPlayer(player_name), amount);
                return r.transactionSuccess();
            } else {
                return false;
            }
        }
    }

    @Override
    public Boolean takeFromBank(String player_name, Integer amount) {
        try {
            assert amount >= 0;
            DataBase dataBase = UltiEconomyMain.dataBase;

            if (!UltiEconomyMain.getInstance().getConfig().getBoolean("enableDataBase")) {
                if (playerFileExists(player_name)) {
                    if (checkMoney(player_name) >= amount) {
                        YamlConfiguration config = loadConfig(getPlayerFile(player_name));
                        config.set("bank", checkBank(player_name) - amount);
                        config.save(getPlayerFile(player_name));
                        return true;
                    }
                }
            } else {
                dataBase.connect();
                if (dataBase.isExist(player_name)) {
                    dataBase.reduceData(player_name, "Bank", amount, "int");
                }
                dataBase.close();
                return true;
            }
        } catch (AssertionError e) {
            System.out.println("数额异常:" + e);
        } catch (IOException e) {
            System.out.println("保存数据异常：" + e);
        }
        return false;
    }

    @Override
    public Boolean transferMoney(String payer, String payee, Integer amount) {
        try {
            assert amount >= 0;
            return addTo(payee, amount) && takeFrom(payer, amount);
        } catch (AssertionError e) {
            System.out.println("数额异常:" + e);
        }
        return false;
    }

    @Override
    public Boolean transferMoneyToBank(String player_name, Integer amount) {
        try {
            assert amount >= 0;
            return addToBank(player_name, amount) && takeFrom(player_name, amount);
        } catch (AssertionError e) {
            System.out.println("数额异常:" + e);
        }
        return false;
    }

    @Override
    public Boolean transferBankToMoney(String player_name, Integer amount) {
        try {
            assert amount >= 0;
            return addTo(player_name, amount) && takeFromBank(player_name, amount);
        } catch (AssertionError e) {
            System.out.println("数额异常:" + e);
        }
        return false;
    }
}
