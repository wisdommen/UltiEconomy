package com.minecraft.economy.apis;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public interface UltiEconomyAPI {

    public  File getPlayerFile(String player_name);

    public YamlConfiguration loadConfig(File file);

    public Boolean playerFileExists(String player_name);

    public Integer checkMoney(String player_name);

    public  Integer checkBank(String player_name);

    public Boolean addTo(String player_name, Integer amount);

    public Boolean addToBank(String player_name, Integer amount);

    public Boolean takeFrom(String player_name, Integer amount);

    public Boolean takeFromBank(String player_name, Integer amount);

    public Boolean transferMoney(String payer, String payee, Integer amount);

    public Boolean transferMoneyToBank(String player_name, Integer amount);

    public Boolean transferBankToMoney(String player_name, Integer amount);
}
