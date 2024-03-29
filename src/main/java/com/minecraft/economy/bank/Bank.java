package com.minecraft.economy.bank;

import com.minecraft.economy.AbstractClasses.AbstractPlayerCommandExecutor;
import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * 查看存款指令
 */
public class Bank extends AbstractPlayerCommandExecutor {
    @Override
    public boolean onPlayerCommand(@NotNull Command command, @NotNull String[] strings, @NotNull Player player, @NotNull UltiEconomy economy) {
        if ("bank".equalsIgnoreCase(command.getName()) && strings.length == 0 && economy.checkBank(player.getName()) >= 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    double money = economy.checkBank(player.getName());
                    player.sendMessage(String.format((ChatColor.GOLD + "你的银行存款为%.2f%s！") , money, UltiEconomyMain.getCurrencyName()));
                }
            }.runTaskAsynchronously(UltiEconomyMain.getInstance());
        }
        return true;
    }
}
