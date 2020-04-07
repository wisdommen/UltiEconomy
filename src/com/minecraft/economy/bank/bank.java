package com.minecraft.economy.bank;

import com.minecraft.economy.apis.checkMoney;
import com.minecraft.economy.economyMain.Economy;
import com.minecraft.economy.database.DataBase;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

import static com.minecraft.economy.apis.checkMoney.checkbank;

public class bank implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (command.getName().equalsIgnoreCase("bank") && strings.length == 0) {
                if (checkbank(player.getName()) >= 0) {
                    player.sendMessage(ChatColor.GOLD + "你的银行存款为" + checkbank(player.getName()) + "枚金币！");
                    return true;
                }
            }
        }
        return false;
    }
}
