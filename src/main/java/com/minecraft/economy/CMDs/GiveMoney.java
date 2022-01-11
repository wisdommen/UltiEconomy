package com.minecraft.economy.CMDs;

import com.minecraft.economy.AbstractClasses.AbstractConsoleCommandExecutor;
import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import com.minecraft.economy.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * 给玩家货币的指令
 */
public class GiveMoney extends AbstractConsoleCommandExecutor {
    @Override
    public boolean onConsoleCommand(@NotNull CommandSender commandSender, @NotNull Command command, String[] strings) {
        return Utils.giveMoney(commandSender, strings);
    }
}
