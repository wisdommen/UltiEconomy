package com.minecraft.economy.givemoney;

import com.minecraft.economy.economyMain.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Pay implements CommandExecutor {

    //这个类我以前刚学Bukkit的时候写的，现在懒得重构了，如果你愿意可以帮我改一下，反正不影响使用
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player commandsender = (Player) commandSender;
            Player player = (Player) commandSender;
            File folder = new File(Economy.getInstance().getDataFolder() + "/playerData");
            File file = new File(folder, player.getName() + ".yml");
            YamlConfiguration config;
            if (!file.exists()) {
                config = new YamlConfiguration();
            } else {
                config = YamlConfiguration.loadConfiguration(file);
            }
            if (command.getName().equalsIgnoreCase("pay")) {
                if (strings.length == 2) {
                    File paidperson = new File(Economy.getInstance().getDataFolder() + "/playerData", strings[0] + ".yml");
                    if (paidperson.exists()) {
                        int money_have = config.getInt("money");
                        try {
                            int money_paid = Integer.parseInt(strings[1]);
                            if (money_paid > config.getInt("money")) {
                                commandsender.sendMessage(ChatColor.GOLD + "你没有那么多钱！");
                            } else {
                                if (player.getName().equals(strings[0])) {
                                    commandsender.sendMessage(ChatColor.RED + "给自己转账就不要麻烦银行了！");
                                    return true;
                                } else {
                                    config.set("money", money_have - money_paid);
                                    try {
                                        config.save(file);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    commandsender.sendMessage(ChatColor.GOLD + "你已转账" + money_paid + "枚金币给" + strings[0] + "！");
                                    FileConfiguration config2 = YamlConfiguration.loadConfiguration(paidperson);
                                    int money_paidperson_have = config2.getInt("money");
                                    config2.set("money", money_paidperson_have + money_paid);
                                    try {
                                        config2.save(paidperson);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
                                        if (strings[0].equals(onlineplayer.getName())) {
                                            onlineplayer.sendMessage(ChatColor.GOLD + "你收到一笔来自" + player.getName() + "的" + money_paid + "枚金币！");
                                        }
                                    }
                                    return true;
                                }
                            }
                            return true;
                        } catch (NumberFormatException e) {
                            commandsender.sendMessage(ChatColor.RED + "[警告]请输入正确的指令！");
                            commandsender.sendMessage(ChatColor.RED + "用法：/pay 玩家名 数字");
                            return true;
                        }
                    } else {
                        commandSender.sendMessage("收款人未开户或不存在！");
                        return true;
                    }

                }
            }else {
                commandsender.sendMessage(ChatColor.RED + "[警告]请输入正确的指令！");
                commandsender.sendMessage(ChatColor.RED + "用法：/pay 玩家名 数字");
                return true;
            }
        }
        return false;
    }
}
