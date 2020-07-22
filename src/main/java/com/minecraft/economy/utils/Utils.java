package com.minecraft.economy.utils;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Random;

/**
 * The type Utils.
 */
public class Utils {

    /**
     * Get config file file.
     *
     * @return the file
     */
    public static File getConfigFile(){
        return new File(UltiEconomyMain.getInstance().getDataFolder(), "config.yml");
    }

    /**
     * Get config yaml configuration.
     *
     * @param file the file
     * @return the yaml configuration
     */
    public static YamlConfiguration getConfig(File file){
        return YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Check online time integer.
     *
     * @param player the player
     * @return the integer
     */
    public static Integer checkOnlineTime(Player player){
        return getConfig(new File(UltiEconomyMain.getInstance().getDataFolder()+"/playerData", player.getName()+".yml")).getInt("online_time");
    }

    /**
     * Convert minutes to regular time string.
     *
     * @param minutes the minutes
     * @return the string
     */
    public static String convertMinutesToRegularTime(Integer minutes){
        if (minutes>60) {
            int hours = minutes / 60;
            int minute = minutes % 60;
            return String.format("%02d小时 %02d分钟", hours, minute);
        }else {
            return String.format("0小时 %02d分钟", minutes);
        }
    }

    /**
     * Get random number integer.
     *
     * @param range the range
     * @return the integer
     */
    public static Integer getRandomNumber(int range){
        final long l = System.currentTimeMillis();
        final int i = (int) (l % 100);
        Random random = new Random(i);
        return random.nextInt(range);
    }

    /**
     * Get economy config file configuration.
     *
     * @return the file configuration
     */
    public static FileConfiguration getEconomyConfig(){
        return UltiEconomyMain.getInstance().getConfig();
    }
}
