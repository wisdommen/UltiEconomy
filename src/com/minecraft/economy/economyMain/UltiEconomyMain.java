package com.minecraft.economy.economyMain;

import com.minecraft.economy.CMDs.TransferData;
import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.bank.bank;
import com.minecraft.economy.bank.ck;
import com.minecraft.economy.bank.qk;
import com.minecraft.economy.CMDs.Givemoney;
import com.minecraft.economy.money.Pay;
import com.minecraft.economy.interest.Interest;
import com.minecraft.economy.money.Money;
import com.minecraft.economy.money.onJoin;
import com.minecraft.economy.database.DataBase;
import com.minecraft.economy.database.LinkedDataBase;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.Objects;


// 这是我以前刚开始学Bukkit开发的时候写的，后来我重构过，相当于重置版，但是仍有许多写的垃圾的地方，请多包涵。
public class UltiEconomyMain extends JavaPlugin {

    private static UltiEconomyMain plugin;

    public static String username, host, password, database, table;
    public static int port;

    private static Economy econ = null;
    private static Boolean isVaultInstalled;

    private static UltiEconomy ultiEconomy = new UltiEconomy();

    public static DataBase dataBase;

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEcon() {
        return econ;
    }

    public static Boolean getIsVaultInstalled() {
        return isVaultInstalled;
    }

    public static UltiEconomy getUltiEconomy() {
        return ultiEconomy;
    }

    @Override
    public void onEnable() {
        plugin = this;
        File folder = new File(String.valueOf(getDataFolder()));
        File playerDataFolder = new File(getDataFolder() + "/playerData");
        if (!folder.exists()) {
            saveDefaultConfig();
        }
        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
        }
        int time = getConfig().getInt("interestTime");

        // 检查是否安装了vault，两个经济插件蛮怪的，8过没办法，球球大家用俺的吧
        isVaultInstalled = setupEconomy();

        //是否启用数据库
        if (getConfig().getBoolean("enableDataBase")) {
            username = getConfig().getString("username");
            password = getConfig().getString("password");
            host = getConfig().getString("host");
            database = getConfig().getString("database");
            port = getConfig().getInt("port");
            table = "player_economy_data";

            dataBase = new LinkedDataBase(new String[]{"Name", "Money", "Bank"});

            dataBase.login(host, String.valueOf(port), username, password, database, table);
            getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "经济插件正在接入数据库...");
            dataBase.connect();
            getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "经济插件正在初始化数据库...");
            dataBase.createTable();
            dataBase.close();
            getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "经济插件已接入数据库！");
        }
        //利息任务
        if (getConfig().getBoolean("enableInterest")) {
            BukkitTask t1 = new Interest().runTaskTimer(this, 0, time * 20L);
            getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "已开启利息！");
        }
        //新玩家入服检测
        getServer().getPluginManager().registerEvents(new onJoin(), this);
        //注册命令
        Objects.requireNonNull(this.getCommand("qk")).setExecutor(new qk());
        Objects.requireNonNull(this.getCommand("ck")).setExecutor(new ck());
        Objects.requireNonNull(this.getCommand("givemoney")).setExecutor(new Givemoney());
        Objects.requireNonNull(this.getCommand("bank")).setExecutor(new bank());
        Objects.requireNonNull(this.getCommand("money")).setExecutor(new Money());
        Objects.requireNonNull(this.getCommand("pay")).setExecutor(new Pay());
        Objects.requireNonNull(this.getCommand("mvdb")).setExecutor(new TransferData());

        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "经济插件已加载！");
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "作者wisdomme");
    }

    public static UltiEconomyMain getInstance() {
        return plugin;
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "经济插件已卸载！");
    }

}