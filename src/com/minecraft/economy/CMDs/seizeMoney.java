package com.minecraft.economy.CMDs;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class seizeMoney implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
            if (command.getName().equalsIgnoreCase("takemoney") && strings.length == 2) {
                int amount = Integer.parseInt(strings[1]);

                if (economy.takeFrom(strings[0], amount)) {
                    commandSender.sendMessage(ChatColor.GOLD + "你已从" + strings[0] + "夺取" + amount + "枚金币！");
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (strings[0].equals(players.getName())) {
                            players.sendMessage(ChatColor.GOLD + "你被腐竹没收了" + amount + "枚金币！");
                        }
                    }
                    return true;
                } else if (economy.takeFromBank(strings[0], amount)) {
                    commandSender.sendMessage(ChatColor.GOLD + "你已从" + strings[0] + "的银行里夺取" + amount + "枚金币！");
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (strings[0].equals(players.getName())) {
                            players.sendMessage(ChatColor.GOLD + "你的银行账户被腐竹没收了" + amount + "枚金币！");
                        }
                    }
                    return true;
                } else {
                    commandSender.sendMessage(ChatColor.GOLD + "没收失败！");
                    return false;
                }
            }
        } else {
            Player player = (Player) commandSender;
            player.sendMessage(ChatColor.RED + "此指令只可以在后台运行！");
            return true;
        }
        return false;
    }
}
