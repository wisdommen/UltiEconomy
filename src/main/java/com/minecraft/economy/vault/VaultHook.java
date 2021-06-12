package com.minecraft.economy.vault;

import com.minecraft.economy.economyMain.UltiEconomyMain;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.ServicePriority;

public class VaultHook {

    private final UltiEconomyMain plugin = UltiEconomyMain.getInstance();

    private Economy provider;

    public void hook() {
        provider = plugin.economyImplementer;
        Bukkit.getServicesManager().register(Economy.class, this.provider, this.plugin, ServicePriority.Normal);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[UltiEconomy] 已连接 VaultAPI");
    }

    public void unhook() {
        Bukkit.getServicesManager().unregister(Economy.class, this.provider);
    }
}
