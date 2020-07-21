package com.minecraft.economy.apis;

import com.minecraft.economy.economyMain.UltiEconomyMain;
import com.minecraft.economy.utils.DatabasePlayerTools;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
    public Integer checkMoney(String player_name) {
        if (!UltiEconomyMain.getIsVaultInstalled()) {
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
        } else {
            if (Bukkit.getPlayer(player_name) != null) {
                String money = getNumber(UltiEconomyMain.getEcon().format(UltiEconomyMain.getEcon().getBalance(Bukkit.getPlayer(player_name))));
                if (money.contains(".")) {
                    return Math.round(Float.parseFloat(money));
                }
                return Integer.parseInt(money);
            } else {
                return -1;
            }
        }
    }

    public static String getNumber(String str) {
        List<String> list = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ".");
        String[] strings = str.split("");
        String result = "";
        for (String each : strings) {
            if (list.contains(each)) {
                result = result + each;
            }
        }
        return result;
    }

    @Override
    public Integer checkBank(String player_name) {
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

    @Override
    public Boolean addTo(String player_name, Integer amount) {
        if (!UltiEconomyMain.getIsVaultInstalled()) {
            try {
                assert amount >= 0;

                if (!UltiEconomyMain.isDatabaseEnabled) {
                    if (playerFileExists(player_name)) {
                        YamlConfiguration config = loadConfig(getPlayerFile(player_name));
                        config.set("money", checkMoney(player_name) + amount);
                        config.save(getPlayerFile(player_name));
                        return true;
                    }
                } else {
                    if (DatabasePlayerTools.isPlayerExist(player_name)) {
                        return DatabasePlayerTools.increasePlayerData("Money", player_name, amount);
                    }
                }
            } catch (AssertionError e) {
                System.out.println("数额异常:" + e);
            } catch (IOException e) {
                System.out.println("保存数据异常：" + e);
            }
            return false;
        } else {
            if (Bukkit.getPlayer(player_name) != null) {
                EconomyResponse r = UltiEconomyMain.getEcon().depositPlayer(Bukkit.getPlayer(player_name), amount);
                return r.transactionSuccess();
            } else {
                return false;
            }
        }
    }

    @Override
    public Boolean addToBank(String player_name, Integer amount) {
        try {
            assert amount >= 0;

            if (!UltiEconomyMain.getInstance().getConfig().getBoolean("enableDataBase")) {
                if (playerFileExists(player_name)) {
                    YamlConfiguration config = loadConfig(getPlayerFile(player_name));
                    config.set("bank", checkBank(player_name) + amount);
                    config.save(getPlayerFile(player_name));
                    return true;
                }
            } else {
                if (DatabasePlayerTools.isPlayerExist(player_name)) {
                    return DatabasePlayerTools.increasePlayerData("Bank", player_name, amount);
                }
            }
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
                assert amount >= 0;
                if (!UltiEconomyMain.getInstance().getConfig().getBoolean("enableDataBase")) {
                    if (checkMoney(player_name) >= amount) {
                        YamlConfiguration config = loadConfig(getPlayerFile(player_name));
                        config.set("money", checkMoney(player_name) - amount);
                        config.save(getPlayerFile(player_name));
                        return true;
                    }
                } else {
                    if (DatabasePlayerTools.isPlayerExist(player_name)) {
                        return DatabasePlayerTools.decreasePlayerData("Money", player_name, amount);
                    }
                }
            } catch (AssertionError e) {
                System.out.println("数额异常:" + e);
            } catch (IOException e) {
                System.out.println("保存数据异常：" + e);
            }
        } else {
            if (Bukkit.getPlayer(player_name) != null) {
                if (UltiEconomyMain.getEcon().has(Bukkit.getOfflinePlayer(Objects.requireNonNull(Bukkit.getPlayer(player_name)).getUniqueId()), amount)) {
                    EconomyResponse r = UltiEconomyMain.getEcon().withdrawPlayer(Bukkit.getPlayer(player_name), amount);
                    return r.transactionSuccess();
                }
            }
        }
        return false;
    }

    @Override
    public Boolean takeFromBank(String player_name, Integer amount) {
        try {
            assert amount >= 0;

            if (!UltiEconomyMain.isDatabaseEnabled) {
                if (playerFileExists(player_name)) {
                    if (checkBank(player_name) >= amount) {
                        YamlConfiguration config = loadConfig(getPlayerFile(player_name));
                        config.set("bank", checkBank(player_name) - amount);
                        config.save(getPlayerFile(player_name));
                        return true;
                    }
                }
            } else {
                if (DatabasePlayerTools.isPlayerExist(player_name)) {
                    return DatabasePlayerTools.decreasePlayerData("Bank", player_name, amount);
                }
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
            if (!UltiEconomyMain.isDatabaseEnabled) {
                if (playerFileExists(payer) && playerFileExists(payee)) {
                    if (checkMoney(payer) >= amount) {
                        YamlConfiguration payer_config = loadConfig(getPlayerFile(payer));
                        payer_config.set("money", checkBank(payer) - amount);
                        YamlConfiguration payee_config = loadConfig(getPlayerFile(payee));
                        payee_config.set("money", checkBank(payee) + amount);
                        try {
                            payer_config.save(getPlayerFile(payer));
                            payee_config.save(getPlayerFile(payee));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                }
            } else {
                return DatabasePlayerTools.increasePlayerData("Money", payee, amount) && DatabasePlayerTools.decreasePlayerData("Money", payer, amount);
            }
        } catch (AssertionError e) {
            System.out.println("数额异常:" + e);
        }
        return false;
    }

    @Override
    public Boolean transferMoneyToBank(String player_name, Integer amount) {
        try {
            assert amount >= 0;
            return takeFrom(player_name, amount) && addToBank(player_name, amount);
        } catch (AssertionError e) {
            System.out.println("数额异常:" + e);
        }
        return false;
    }

    @Override
    public Boolean transferBankToMoney(String player_name, Integer amount) {
        try {
            assert amount >= 0;
            return  takeFromBank(player_name, amount) && addTo(player_name, amount);
        } catch (AssertionError e) {
            System.out.println("数额异常:" + e);
        }
        return false;
    }
}
