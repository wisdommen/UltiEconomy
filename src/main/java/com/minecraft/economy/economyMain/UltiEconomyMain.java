package com.minecraft.economy.economyMain;

import com.minecraft.economy.CMDs.MoneyOperateCommands;
import com.minecraft.economy.CMDs.TransferData;
import com.minecraft.economy.register.CommandRegister;
import com.minecraft.economy.vault.EconomyImplementer;
import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.bank.Bank;
import com.minecraft.economy.bank.Deposit;
import com.minecraft.economy.bank.Withdraw;
import com.minecraft.economy.money.Pay;
import com.minecraft.economy.interest.Interest;
import com.minecraft.economy.money.Money;
import com.minecraft.economy.money.OnJoin;
import com.minecraft.economy.placeholderExpension.UltiEconomyExpansion;
import com.minecraft.economy.utils.DatabaseUtils;
import com.minecraft.economy.vault.VaultHook;
import com.minecraft.economy.versionChecker.ConfigFileCheck;
import com.minecraft.economy.versionChecker.Metrics;
import com.minecraft.economy.versionChecker.VersionChecker;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.Objects;


/**
 * UltiEconomy主类
 */
// 这是我以前刚开始学Bukkit开发的时候写的，后来我重构过，相当于重置版，但是仍有许多写的垃圾的地方，请多包涵。
public class UltiEconomyMain extends JavaPlugin {

    private static UltiEconomyMain plugin;

    private static Economy econ = null;
    private static Boolean isVaultInstalled;
    public static boolean isDatabaseEnabled;
    private VaultHook vaultHook;
    public EconomyImplementer economyImplementer;

    private static final UltiEconomy ultiEconomy = new UltiEconomy();

    private boolean setupEconomy() {
        return getServer().getPluginManager().getPlugin("Vault") != null;
    }

    /**
     * 获取Vault经济实例
     *
     * @return the econ
     */
    public static Economy getEcon() {
        return econ;
    }

    /**
     * 获取Vault是否安装
     *
     * @return the is vault installed
     */
    public static Boolean getIsVaultInstalled() {
        return isVaultInstalled;
    }

    /**
     * 获取UltiEconomy经济对象实例
     *
     * @return the ultiEconomy
     */
    public static UltiEconomy getUltiEconomy() {
        return ultiEconomy;
    }

    @Override
    public void onEnable() {
        if (!setupEconomy() ) {
            getLogger().warning("[UltiEconomy] 未找到Vault前置插件，关闭中...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        plugin = this;
        economyImplementer = new EconomyImplementer();
        vaultHook = new VaultHook();
        vaultHook.hook();
        startBStates();
        File folder = new File(String.valueOf(getDataFolder()));
        File playerDataFolder = new File(getDataFolder() + "/playerData");
        File config_file = new File(getDataFolder(), "config.yml");
        if (!folder.exists() || !config_file.exists()) {
            saveDefaultConfig();
        }
        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
        }
        ConfigFileCheck.reviewConfigFile();
        int time = getConfig().getInt("interestTime");

        // 检查是否安装了vault
        isVaultInstalled = setupEconomy();

        isDatabaseEnabled = getConfig().getBoolean("enableDataBase");

        //是否启用数据库
        if (isDatabaseEnabled) {
            String table = "player_economy_data";
            getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "经济插件正在初始化数据库...");
            if(DatabaseUtils.createTable(table, new String[]{"Name", "Money", "Bank"})){
                getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "经济插件接入数据库成功！");
            }else {
                getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "经济插件接入数据库失败！");
                getConfig().set("enableDataBase", false);
                saveConfig();
                reloadConfig();
            }
        }
        //利息任务
        if (getConfig().getBoolean("enableInterest")) {
            BukkitTask t1 = new Interest().runTaskTimerAsynchronously(this, 0, time * 20L);
            getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "已开启利息！");
        }
        //新玩家入服检测
        getServer().getPluginManager().registerEvents(new OnJoin(), this);
        //注册命令
        Objects.requireNonNull(this.getCommand("qk")).setExecutor(new Withdraw());
        Objects.requireNonNull(this.getCommand("ck")).setExecutor(new Deposit());
        Objects.requireNonNull(this.getCommand("givemoney")).setExecutor(new MoneyOperateCommands());
        Objects.requireNonNull(this.getCommand("takemoney")).setExecutor(new MoneyOperateCommands());
        Objects.requireNonNull(this.getCommand("setmoney")).setExecutor(new MoneyOperateCommands());
        Objects.requireNonNull(this.getCommand("bank")).setExecutor(new Bank());
        Objects.requireNonNull(this.getCommand("money")).setExecutor(new Money());
        CommandRegister.registerCommand(this, new Pay(), "", "付款指令", "ultipay", "upay", "pay");
        Objects.requireNonNull(this.getCommand("mvdb")).setExecutor(new TransferData());

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new UltiEconomyExpansion().register();
        }

        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "经济插件已加载！");
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "作者wisdomme");

        //检查更新
        if (getConfig().getBoolean("enable_version_check")) {
            VersionChecker.setupThread();
        }
    }

    /**
     * 获取主类
     *
     * @return the instance
     */
    public static UltiEconomyMain getInstance() {
        return plugin;
    }

    @Override
    public void onDisable() {
        vaultHook.unhook();
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "经济插件已卸载！");
    }

    private static void startBStates(){
        // All you have to do is adding the following two lines in your onEnable method.
        // You can find the plugin ids of your plugins on the page https://bstats.org/what-is-my-plugin-id
        int pluginId = 8690; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(UltiEconomyMain.getInstance(), pluginId);
    }
}