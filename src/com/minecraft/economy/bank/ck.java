package com.minecraft.economy.bank;

import com.minecraft.economy.apis.checkMoney;
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
            File file = checkMoney.get_player_file(player.getName());
            YamlConfiguration config = checkMoney.load_config(file);
            if (command.getName().equalsIgnoreCase("ck")) {
                if (strings.length == 1) {
                    try {
                        int ckmoney = Integer.parseInt(strings[0]);
                        int currentCkmoney = config.getInt("bank");
                        if (ckmoney >= 0) {
                            int currentMoney = config.getInt("money");
                            if (currentMoney - ckmoney >= 0) {
                                config.set("bank", currentCkmoney + ckmoney);
                                config.set("money", currentMoney - ckmoney);
                                try {
                                    config.save(file);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                player.sendMessage(ChatColor.BLUE + "你已存入" + ckmoney + "枚金币！");
                                player.sendMessage(ChatColor.GREEN + "存款余额：" + config.getInt("bank"));
                                player.sendMessage(ChatColor.GREEN + "现金余额：" + config.getInt("money"));
                                return true;
                            } else {
                                player.sendMessage(ChatColor.RED + "你没有这么多钱！");
                                return true;
                            }
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
