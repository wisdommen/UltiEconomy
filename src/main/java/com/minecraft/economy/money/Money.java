package com.minecraft.economy.money;


import com.minecraft.economy.AbstractClasses.AbstractPlayerCommandExecutor;
import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;


/**
 * 查看货币
 */
public class Money extends AbstractPlayerCommandExecutor {

    @Override
    public boolean onPlayerCommand(@NotNull Command command, String[] strings, @NotNull Player player, @NotNull UltiEconomy economy) {
        if (!("money".equalsIgnoreCase(command.getName()) && strings.length == 0)) {
            return false;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage(String.format(ChatColor.GOLD + "你有%.2f%s！", economy.checkMoney(player.getName()), UltiEconomyMain.getCurrencyName()));
            }
        }.runTaskAsynchronously(UltiEconomyMain.getInstance());
        return true;
    }
}