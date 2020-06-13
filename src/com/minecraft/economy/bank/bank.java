package com.minecraft.economy.bank;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class bank implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
            if (command.getName().equalsIgnoreCase("bank") && strings.length == 0) {
                if (economy.checkBank(player.getName()) >= 0) {
                    player.sendMessage(ChatColor.GOLD + "你的银行存款为" + economy.checkBank(player.getName()) + "枚金币！");
                    return true;
                }
            }
        }
        return false;
    }
}
