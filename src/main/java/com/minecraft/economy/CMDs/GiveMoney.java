package com.minecraft.economy.CMDs;

import com.minecraft.economy.AbstractClasses.AbstractConsoleCommandExecutor;
import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * 给玩家金币的指令
 */
public class GiveMoney extends AbstractConsoleCommandExecutor {
    @Override
    public boolean onConsoleCommand(@NotNull CommandSender commandSender, @NotNull Command command, String[] strings) {
        UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
        if (!("givemoney".equalsIgnoreCase(command.getName()) && strings.length == 2)) {
            return false;
        }

        int amount;
        try {
            amount = Integer.parseInt(strings[1]);
        } catch (NumberFormatException e) {
            commandSender.sendMessage(ChatColor.RED + "格式错误");
            return false;
        }

        if (!economy.addTo(strings[0], amount)) {
            commandSender.sendMessage(ChatColor.RED + "转账失败！");
            return false;
        }
        commandSender.sendMessage(String.format(ChatColor.GOLD + "你已转账%d枚金币给%s！", amount, strings[0]));
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (strings[0].equals(players.getName())) {
                players.sendMessage(String.format(ChatColor.GOLD + "你收到一笔腐竹转给你的%d枚金币！", amount));
            }
        }
        return true;
    }
}
