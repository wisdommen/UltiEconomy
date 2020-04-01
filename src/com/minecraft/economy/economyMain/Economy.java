package com.minecraft.economy.economyMain;

import com.minecraft.economy.bank.bank;
import com.minecraft.economy.bank.ck;
import com.minecraft.economy.bank.qk;
import com.minecraft.economy.givemoney.Givemoney;
import com.minecraft.economy.givemoney.Pay;
import com.minecraft.economy.interest.Interest;
import com.minecraft.economy.money.Money;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.Objects;


// 这是我以前刚开始学Bukkit开发的时候写的，后来我重构过，相当于重置版，但是仍有许多写的垃圾的地方，请多包涵。
public class Economy extends JavaPlugin {

    private static Economy plugin;

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
        BukkitTask t1 = new Interest().runTaskTimer(this, 0, time * 20L);
        Objects.requireNonNull(this.getCommand("qk")).setExecutor(new qk());
        Objects.requireNonNull(this.getCommand("ck")).setExecutor(new ck());
        Objects.requireNonNull(this.getCommand("Givemoney")).setExecutor(new Givemoney());
        Objects.requireNonNull(this.getCommand("bank")).setExecutor(new bank());
        Objects.requireNonNull(this.getCommand("money")).setExecutor(new Money());
        Objects.requireNonNull(this.getCommand("pay")).setExecutor(new Pay());
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "经济插件已加载！");
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "作者wisdomme");
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD+"已开启利息！");
    }

    public static Economy getInstance() {
        return plugin;
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "经济插件已卸载！");
    }

}