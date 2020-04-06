package com.minecraft.economy.givemoney;

import com.minecraft.economy.apis.addMoney;
import com.minecraft.economy.economyMain.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class Givemoney implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            if (command.getName().equalsIgnoreCase("givemoney") && strings.length == 2) {
                Integer amount = Integer.parseInt(strings[1]);

                if (addMoney.addTo(strings[0], amount)) {
                    commandSender.sendMessage(ChatColor.GOLD + "你已转账" + amount + "枚金币给" + strings[0] + "！");
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (strings[0].equals(players.getName())) {
                            players.sendMessage(ChatColor.GOLD + "你收到一笔腐竹转给你的" + amount + "枚金币！");
                        }
                    }
                    return true;
                } else {
                    commandSender.sendMessage(ChatColor.GOLD + "转账失败！");
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
