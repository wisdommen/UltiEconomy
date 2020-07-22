package com.minecraft.economy.money;


import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * 查看金币
 */
public class Money implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
            if ("money".equalsIgnoreCase(command.getName()) && strings.length == 0) {
                player.sendMessage(ChatColor.GOLD + "你有" + economy.checkMoney(player.getName()) + "枚金币！");
                return true;
            }
        }
        return false;
    }
}