package com.minecraft.economy.bank;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * 取款指令
 */
public class qk implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
            if ("qk".equalsIgnoreCase(command.getName())) {
                if (strings.length == 1) {
                    try {
                        int qkmoney = Integer.parseInt(strings[0]);
                        if (qkmoney >= 0) {
                            int currentCkMoney = economy.checkBank(player.getName());
                            if (qkmoney <= currentCkMoney) {
                                int money_before = economy.checkMoney(player.getName());
                                int bank_before = economy.checkBank(player.getName());
                                if (economy.transferBankToMoney(player.getName(), qkmoney)) {
                                    int money_after = economy.checkMoney(player.getName());
                                    int bank_after = economy.checkBank(player.getName());
                                    if ((money_before == money_after && bank_after < bank_before) || (money_before < money_after && bank_after == bank_before)) {
                                        if (bank_after < bank_before) {
                                            economy.addToBank(player.getName(), bank_after - bank_before);
                                        }
                                        if (money_after > money_before) {
                                            economy.takeFrom(player.getName(), money_before - money_after);
                                        }
                                        player.sendMessage(ChatColor.RED + "取款出现错误，请换个数额存款！");
                                        return true;
                                    }
                                    player.sendMessage(ChatColor.BLUE + "你已取出" + qkmoney + "枚金币！");
                                    player.sendMessage(ChatColor.GREEN + "存款余额：" + economy.checkBank(player.getName()));
                                    player.sendMessage(ChatColor.GREEN + "现金余额：" + economy.checkMoney(player.getName()));
                                }else {
                                    player.sendMessage(ChatColor.RED + "取款出现错误！");
                                    return true;
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "你没有这么多钱！");
                            }
                            return true;
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "格式错误！");
                        return false;
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "格式错误！");
                    return false;
                }
            }
        }

        return false;
    }
}
