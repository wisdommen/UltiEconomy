package com.minecraft.economy.money;

import com.minecraft.economy.AbstractClasses.AbstractTabExecutor;
import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * 玩家间付款
 */
public class Pay extends AbstractTabExecutor {

    @Override
    public boolean onPlayerCommand(@NotNull Command command, String[] strings, @NotNull Player player, @NotNull UltiEconomy economy) {
        if (!"ultipay".equalsIgnoreCase(command.getName()) || strings.length != 2) {
            player.sendMessage(ChatColor.RED + "[警告]请输入正确的指令！");
            player.sendMessage(ChatColor.RED + "用法：/ultipay 玩家名 数字");
            return true;
        }
        double amount;
        try {
            amount = Double.parseDouble(strings[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "[警告]请输入正确的指令！");
            player.sendMessage(ChatColor.RED + "用法：/ultipay 玩家名 数字");
            return true;
        }
        if (amount < 0){
            player.sendMessage(ChatColor.RED + "[警告]转账数额必须大于0！");
            return true;
        }
        if (!economy.transferMoney(player.getName(), strings[0], amount)) {
            player.sendMessage(ChatColor.RED+"转账失败！");
            return true;
        }
        player.sendMessage(String.format(ChatColor.GOLD + "你已转账%s枚金币给%s！", strings[1], strings[0]));
        Player receiver = Bukkit.getPlayerExact(strings[0]);
        if (receiver != null && receiver.isOnline()) {
            receiver.sendMessage(String.format(ChatColor.GOLD + "你收到一笔来自%s的%s枚金币！", player.getName(), strings[1]));
        }
        return true;
    }

    @Override
    public List<String> onPlayerTabComplete(@NotNull Command command, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> tabCommands = new ArrayList<>();
            for (OfflinePlayer offlinePlayer : UltiEconomyMain.getInstance().getServer().getOfflinePlayers()) {
                tabCommands.add(offlinePlayer.getName());
            }
            return tabCommands;
        }
        return null;
    }
}
