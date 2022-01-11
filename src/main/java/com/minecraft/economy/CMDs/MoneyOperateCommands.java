package com.minecraft.economy.CMDs;

import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import com.minecraft.economy.utils.Utils;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MoneyOperateCommands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.isOp()) {
                if (UltiEconomyMain.getInstance().getConfig().getBoolean("op_operate_money")) {
                    UltiEconomy ultiEconomy = UltiEconomyMain.getUltiEconomy();
                    if ("checkmoney".equalsIgnoreCase(command.getName())){
                        Double money = ultiEconomy.checkMoney(strings[0]);
                        if (money < 0){
                            player.sendMessage(ChatColor.RED + "不存在此玩家！");
                            return true;
                        }
                        player.sendMessage(ChatColor.GOLD + String.format("%s拥有%.2f%s", strings[0], money, UltiEconomyMain.getCurrencyName()));
                        return true;
                    }else if ("checkbank".equalsIgnoreCase(command.getName())){
                        Double money = ultiEconomy.checkBank(strings[0]);
                        if (money < 0){
                            player.sendMessage(ChatColor.RED + "不存在此玩家！");
                            return true;
                        }
                        player.sendMessage(ChatColor.GOLD + String.format("%s拥有%.2f存款", strings[0], money));
                        return true;
                    }
                    return execute(player, command, strings);
                } else {
                    player.sendMessage(ChatColor.RED + "你不能在游戏内执行这个命令！");
                    return true;
                }
            }else {
                player.sendMessage(ChatColor.RED+"你没有权限使用这个指令！");
                return true;
            }
        }
        return execute(sender, command, strings);
    }

    private boolean execute(CommandSender sender, Command command, String[] strings){
        if (strings.length != 2){
            return false;
        }
        if ("givemoney".equalsIgnoreCase(command.getName())) {
            return Utils.giveMoney(sender, strings);
        }else if ("takemoney".equalsIgnoreCase(command.getName())){
            return Utils.takeMoney(sender, strings);
        } else if ("setmoney".equalsIgnoreCase(command.getName())){
            return Utils.setMoney(sender, strings);
        }
        return false;
    }
}
