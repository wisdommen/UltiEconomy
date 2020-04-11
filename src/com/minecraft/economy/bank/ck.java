package com.minecraft.economy.bank;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.minecraft.economy.apis.checkMoney.checkbank;
import static com.minecraft.economy.apis.checkMoney.checkmoney;
import static com.minecraft.economy.apis.transfer.transferMoneyToBank;

public class ck implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (command.getName().equalsIgnoreCase("ck")) {
                if (strings.length == 1) {
                    try {
                        int ckmoney = Integer.parseInt(strings[0]);
                        if (ckmoney >= 1000) {
                            int currentMoney = checkmoney(player.getName());
                            if (currentMoney - ckmoney >= 0) {
                                transferMoneyToBank(player.getName(), ckmoney);
                                player.sendMessage(ChatColor.BLUE + "你已存入" + ckmoney + "枚金币！");
                                player.sendMessage(ChatColor.GREEN + "存款余额：" + checkbank(player.getName()));
                                player.sendMessage(ChatColor.GREEN + "现金余额：" + checkmoney(player.getName()));
                                return true;
                            } else {
                                player.sendMessage(ChatColor.RED + "你没有这么多钱！");
                                return true;
                            }
                        }else {
                            player.sendMessage(ChatColor.GOLD+"无法存入小于1000的数额！");
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "格式错误！");
                        return false;
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "格式错误！");
                return false;
            }
        }
        return false;
    }
}
