package com.minecraft.economy.money;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


/**
 * 玩家间付款
 */
public class Pay implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            UltiEconomy economy = UltiEconomyMain.getUltiEconomy();
            Player player = (Player) commandSender;

            if ("pay".equalsIgnoreCase(command.getName())) {
                if (strings.length == 2) {
                    if (economy.transferMoney(player.getName(), strings[0], Integer.parseInt(strings[1]))) {
                        try {
                            player.sendMessage(ChatColor.GOLD + "你已转账" + strings[1] + "枚金币给" + strings[0] + "！");
                            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                if (strings[0].equals(onlinePlayer.getName())) {
                                    onlinePlayer.sendMessage(ChatColor.GOLD + "你收到一笔来自" + player.getName() + "的" + strings[1] + "枚金币！");
                                }
                            }
                            return true;
                        } catch (NumberFormatException e) {
                            player.sendMessage(ChatColor.RED + "[警告]请输入正确的指令！");
                            player.sendMessage(ChatColor.RED + "用法：/pay 玩家名 数字");
                            return true;
                        }
                    } else {
                        player.sendMessage("收款人未开户或不存在！");
                        return true;
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "[警告]请输入正确的指令！");
                player.sendMessage(ChatColor.RED + "用法：/pay 玩家名 数字");
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player){
            if (args.length == 1){
                List<String> tabCommands = new ArrayList<>();
                for (OfflinePlayer offlinePlayer : UltiEconomyMain.getInstance().getServer().getOfflinePlayers()){
                    tabCommands.add(offlinePlayer.getName());
                }
                return tabCommands;
            }
        }
        return null;
    }
}
