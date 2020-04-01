package com.minecraft.economy.money;


import com.minecraft.economy.apis.checkMoney;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Money implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (command.getName().equalsIgnoreCase("money") && strings.length == 0) {
                File file = checkMoney.get_player_file(player.getName());
                YamlConfiguration config = checkMoney.load_config(file);
                if (checkMoney.check(player.getName()) > 0) {
                    player.sendMessage(ChatColor.GOLD + "你有" + checkMoney.check(player.getName()) + "枚金币！");
                    return true;
                } else {
                    config.set("money", 1000);
                    config.set("bank", 0);
                    try {
                        config.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.sendMessage(ChatColor.GOLD + "您还没有银行账户！");
                    player.sendMessage(ChatColor.GOLD + "正在为您开户...");
                    player.sendMessage(ChatColor.GOLD + "已成功开户！");
                    player.sendMessage(ChatColor.RED + "收到来自腐竹的1000金币转账！");
                    return true;
                }
            }
        }
        return false;
    }
}