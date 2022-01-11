package com.minecraft.economy.utils;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    public static @NotNull File getConfigFile(){
        return new File(UltiEconomyMain.getInstance().getDataFolder(), "config.yml");
    }

    /**
     * Get config yaml configuration.
     *
     * @param file the file
     * @return the yaml configuration
     */
    public static @NotNull YamlConfiguration getConfig(File file){
        return YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Check online time integer.
     *
     * @param player the player
     * @return the integer
     */
    public static @NotNull Integer checkOnlineTime(@NotNull Player player){
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
    public static @NotNull Integer getRandomNumber(int range){
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
    public static @NotNull FileConfiguration getEconomyConfig(){
        return UltiEconomyMain.getInstance().getConfig();
    }

    /**
     * @param path folder path
     * @return list of files
     */
    public static @Nullable List<File> getFiles(String path) {
        File folder = new File(path);
        if (folder.listFiles() != null) {
            return Arrays.asList(Objects.requireNonNull(folder.listFiles()));
        }
        return null;
    }

    public static boolean setMoney(CommandSender commandSender, String[] strings){
        UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
        double amount;
        try {
            amount = Double.parseDouble(strings[1]);
        } catch (NumberFormatException e) {
            commandSender.sendMessage(ChatColor.RED + "格式错误");
            return false;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (amount < 0){
                    commandSender.sendMessage(ChatColor.RED + "[警告]设置数额必须大于0！");
                    return;
                }
                if (!economy.setMoney(strings[0], amount)) {
                    commandSender.sendMessage(ChatColor.RED + "操作失败！");
                    return;
                }
                commandSender.sendMessage(String.format(ChatColor.GOLD + "你已将%s的现金余额设置为%.2f%s！", strings[0], amount, UltiEconomyMain.getCurrencyName()));
            }
        }.runTaskAsynchronously(UltiEconomyMain.getInstance());
        return true;
    }

    public static boolean giveMoney(CommandSender commandSender, String[] strings){
        UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
        double amount;
        try {
            amount = Double.parseDouble(strings[1]);
        } catch (NumberFormatException e) {
            commandSender.sendMessage(ChatColor.RED + "格式错误");
            return false;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (amount < 0){
                    commandSender.sendMessage(ChatColor.RED + "[警告]转账数额必须大于0！");
                    return;
                }
                if (!economy.addTo(strings[0], amount)) {
                    commandSender.sendMessage(ChatColor.RED + "转账失败！");
                    return;
                }
                commandSender.sendMessage(String.format(ChatColor.GOLD + "你已转账%.2f%s给%s！", amount, UltiEconomyMain.getCurrencyName(), strings[0]));
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (strings[0].equals(players.getName())) {
                        players.sendMessage(String.format(ChatColor.GOLD + "你收到一笔%s转给你的%.2f%s！", commandSender.getName(), amount, UltiEconomyMain.getCurrencyName()));
                    }
                }
            }
        }.runTaskAsynchronously(UltiEconomyMain.getInstance());
        return true;
    }

    public static boolean takeMoney(CommandSender commandSender, String[] strings){
        UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
        double amount;
        try {
            amount = Double.parseDouble(strings[1]);
        } catch (NumberFormatException e) {
            commandSender.sendMessage(ChatColor.RED + "格式错误");
            return false;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (amount < 0){
                    commandSender.sendMessage(ChatColor.RED + "[警告]数额必须大于0！");
                    return;
                }
                if (economy.takeFrom(strings[0], amount)) {
                    commandSender.sendMessage(String.format(ChatColor.GOLD + "你已从%s夺取%.2f%s！", strings[0], amount, UltiEconomyMain.getCurrencyName()));
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (strings[0].equals(players.getName())) {
                            players.sendMessage(String.format(ChatColor.GOLD + "你被没收了%.2f%s！", amount, UltiEconomyMain.getCurrencyName()));
                        }
                    }
                } else if (economy.takeFromBank(strings[0], amount)) {
                    commandSender.sendMessage(String.format(ChatColor.GOLD + "你已从%s的银行里夺取%.2f%s！", strings[0], amount, UltiEconomyMain.getCurrencyName()));
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (strings[0].equals(players.getName())) {
                            players.sendMessage(String.format(ChatColor.GOLD + "你的银行账户被没收了%.2f%s！", amount, UltiEconomyMain.getCurrencyName()));
                        }
                    }
                } else {
                    commandSender.sendMessage(ChatColor.GOLD + "没收失败！可能是对方货币数量不足。");
                }
            }
        }.runTaskAsynchronously(UltiEconomyMain.getInstance());
        return true;
    }
}
