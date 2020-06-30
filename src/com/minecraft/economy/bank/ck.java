package com.minecraft.economy.bank;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class ck implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
            if (command.getName().equalsIgnoreCase("ck")) {
                if (strings.length == 1) {
                    try {
                        int ckmoney = Integer.parseInt(strings[0]);
                        if (ckmoney >= 1000) {
                            int currentMoney = economy.checkMoney(player.getName());
                            if (currentMoney - ckmoney >= 0) {
                                int money_before = economy.checkMoney(player.getName());
                                int bank_before = economy.checkBank(player.getName());
                                if (economy.transferMoneyToBank(player.getName(), ckmoney)) {
                                    int money_after = economy.checkMoney(player.getName());
                                    int bank_after = economy.checkBank(player.getName());
                                    if ((money_before == money_after && bank_after > bank_before) || (money_before > money_after && bank_after == bank_before)) {
                                        if (bank_after > bank_before) {
                                            economy.takeFromBank(player.getName(), bank_after - bank_before);
                                        }
                                        if (money_after < money_before) {
                                            economy.addTo(player.getName(), money_before - money_after);
                                        }
                                        player.sendMessage(ChatColor.RED + "存款出现错误，请换个数额存款！");
                                        return true;
                                    }
                                    player.sendMessage(ChatColor.BLUE + "你已存入" + ckmoney + "枚金币！");
                                    player.sendMessage(ChatColor.GREEN + "存款余额：" + economy.checkBank(player.getName()));
                                    player.sendMessage(ChatColor.GREEN + "现金余额：" + economy.checkMoney(player.getName()));
                                }else {
                                    player.sendMessage(ChatColor.RED + "存款出现错误！");
                                    return true;
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "你没有这么多钱！");
                            }
                            return true;
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
