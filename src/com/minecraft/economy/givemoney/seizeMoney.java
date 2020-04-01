package com.minecraft.economy.givemoney;

import com.minecraft.economy.apis.takeMoney;
import com.minecraft.economy.economyMain.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class seizeMoney implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            if (command.getName().equalsIgnoreCase("takemoney") && strings.length == 2) {
                int amount = Integer.parseInt(strings[1]);
                File player = new File(Economy.getInstance().getDataFolder() + "/playerData", strings[0] + ".yml");

                if (takeMoney.takeFrom(player.getName(), amount)) {
                    commandSender.sendMessage(ChatColor.GOLD + "你已从" + strings[0] + "夺取" + amount + "枚金币！");
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (strings[0].equals(players.getName())) {
                            players.sendMessage(ChatColor.GOLD + "你被腐竹没收了" + amount + "枚金币！");
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
