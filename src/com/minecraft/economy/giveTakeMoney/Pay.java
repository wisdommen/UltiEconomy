package com.minecraft.economy.giveTakeMoney;

import com.minecraft.economy.economyMain.Economy;
import com.minecraft.economy.database.DataBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.minecraft.economy.apis.transfer.transferMoney;

public class Pay implements CommandExecutor {

    //这个类我以前刚学Bukkit的时候写的，现在懒得重构了，如果你愿意可以帮我改一下，反正不影响使用
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (command.getName().equalsIgnoreCase("pay")) {
                if (strings.length == 2) {
                    if (transferMoney(player.getName(), strings[0], Integer.parseInt(strings[1]))) {
                        try {
                            player.sendMessage(ChatColor.GOLD + "你已转账" + strings[1] + "枚金币给" + strings[0] + "！");
                            for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
                                if (strings[0].equals(onlineplayer.getName())) {
                                    onlineplayer.sendMessage(ChatColor.GOLD + "你收到一笔来自" + player.getName() + "的" + strings[1] + "枚金币！");
                                }
                            }
                            return true;
                        } catch (NumberFormatException e) {
                            player.sendMessage(ChatColor.RED + "[警告]请输入正确的指令！");
                            player.sendMessage(ChatColor.RED + "用法：/pay 玩家名 数字");
                            return true;
                        }
                    } else {
                        player.sendMessage("收款人未开户或不存在！");
                        return true;
                    }

                }
            } else {
                player.sendMessage(ChatColor.RED + "[警告]请输入正确的指令！");
                player.sendMessage(ChatColor.RED + "用法：/pay 玩家名 数字");
                return true;
            }
        }
        return false;
    }
}
