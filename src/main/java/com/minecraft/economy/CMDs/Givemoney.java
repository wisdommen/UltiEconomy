package com.minecraft.economy.CMDs;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 给玩家金币的指令
 */
public class Givemoney implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
            if ("givemoney".equalsIgnoreCase(command.getName()) && strings.length == 2) {
                Integer amount = Integer.parseInt(strings[1]);

                if (economy.addTo(strings[0], amount)) {
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
