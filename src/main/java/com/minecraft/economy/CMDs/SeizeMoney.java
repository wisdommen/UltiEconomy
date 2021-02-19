package com.minecraft.economy.CMDs;

import com.minecraft.economy.AbstractClasses.AbstractConsoleCommandExecutor;
import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;


/**
 * 没收玩家金币的指令
 */
public class SeizeMoney extends AbstractConsoleCommandExecutor {
    @Override
    public boolean onConsoleCommand(@NotNull CommandSender commandSender, @NotNull Command command, String[] strings) {
        UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
        if (strings.length != 2) {
            return false;
        }
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
                    commandSender.sendMessage(String.format(ChatColor.GOLD + "你已从%s夺取%.2f枚金币！", strings[0], amount));
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (strings[0].equals(players.getName())) {
                            players.sendMessage(String.format(ChatColor.GOLD + "你被腐竹没收了%.2f枚金币！", amount));
                        }
                    }
                } else if (economy.takeFromBank(strings[0], amount)) {
                    commandSender.sendMessage(String.format(ChatColor.GOLD + "你已从%s的银行里夺取%.2f枚金币！", strings[0], amount));
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (strings[0].equals(players.getName())) {
                            players.sendMessage(String.format(ChatColor.GOLD + "你的银行账户被腐竹没收了%.2f枚金币！", amount));
                        }
                    }
                } else {
                    commandSender.sendMessage(ChatColor.GOLD + "没收失败！可能是对方金币数量不足。");
                }
            }
        }.runTaskAsynchronously(UltiEconomyMain.getInstance());
        return true;
    }
}
