package com.minecraft.economy.money;


import com.minecraft.economy.apis.checkMoney;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Money implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (command.getName().equalsIgnoreCase("money") && strings.length == 0) {
                if (checkMoney.check(player.getName()) >= 0) {
                    player.sendMessage(ChatColor.GOLD + "你有" + checkMoney.check(player.getName()) + "枚金币！");
                    return true;
                }
            }
        }
        return false;
    }
}