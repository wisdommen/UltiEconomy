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
import org.jetbrains.annotations.NotNull;


/**
 * 没收玩家金币的指令
 */
public class SeizeMoney extends AbstractConsoleCommandExecutor {
    @Override
    public boolean onConsoleCommand(@NotNull CommandSender commandSender, @NotNull Command command, String[] strings) {
        UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
        if (!(command.getName().equalsIgnoreCase("takemoney") && strings.length == 2)) {
            return false;
        }
        int amount;
        try {
            amount = Integer.parseInt(strings[1]);
        } catch (NumberFormatException e) {
            commandSender.sendMessage(ChatColor.RED + "格式错误");
            return false;
        }

        if (economy.takeFrom(strings[0], amount)) {
            commandSender.sendMessage(String.format(ChatColor.GOLD + "你已从%s夺取%d枚金币！", strings[0], amount));
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (strings[0].equals(players.getName())) {
                    players.sendMessage(String.format(ChatColor.GOLD + "你被腐竹没收了%d枚金币！", amount));
                }
            }
            return true;
        } else if (economy.takeFromBank(strings[0], amount)) {
            commandSender.sendMessage(String.format(ChatColor.GOLD + "你已从%s的银行里夺取%d枚金币！", strings[0], amount));
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (strings[0].equals(players.getName())) {
                    players.sendMessage(String.format(ChatColor.GOLD + "你的银行账户被腐竹没收了%d枚金币！", amount));
                }
            }
            return true;
        } else {
            commandSender.sendMessage(ChatColor.GOLD + "没收失败！");
            return false;
        }
    }
}
