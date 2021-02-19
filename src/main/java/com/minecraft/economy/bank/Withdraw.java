package com.minecraft.economy.bank;

import com.minecraft.economy.AbstractClasses.AbstractPlayerCommandExecutor;
import com.minecraft.economy.apis.UltiEconomy;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;


/**
 * 取款指令
 */
public class Withdraw  extends AbstractPlayerCommandExecutor {
    @Override
    public boolean onPlayerCommand(@NotNull Command command, String[] strings, @NotNull Player player, @NotNull UltiEconomy economy) {
        if (!("qk".equalsIgnoreCase(command.getName()) && strings.length == 1)) {
            player.sendMessage(ChatColor.RED + "格式错误！");
            return false;
        }
        double withdraw;
        try {
            withdraw = Double.parseDouble(strings[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "格式错误！");
            return false;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (withdraw < 0) {
                    player.sendMessage(ChatColor.RED + "取款金额不可以是负数！");
                    return;
                }
                double currentSavings = economy.checkBank(player.getName());
                if (withdraw > currentSavings) {
                    player.sendMessage(ChatColor.RED + "你没有这么多钱！");
                    return;
                }
                if (!processWithdraw(economy, player, withdraw)) return;
                player.sendMessage(String.format(ChatColor.BLUE + "你已存入%.2f枚金币！\n" +
                                ChatColor.GREEN + "存款余额：%.2f\n" +
                                ChatColor.GREEN + "现金余额：%.2f",
                        withdraw, economy.checkBank(player.getName()), economy.checkMoney(player.getName())));
            }
        }.runTaskAsynchronously(UltiEconomyMain.getInstance());
        return true;
    }

    private boolean processWithdraw(UltiEconomy economy, Player player, double deposit) {
        double money_before = economy.checkMoney(player.getName());
        double bank_before = economy.checkBank(player.getName());
        if (!economy.transferBankToMoney(player.getName(), deposit)) {
            player.sendMessage(ChatColor.RED + "存款出现错误！");
            return false;
        }
        double money_after = economy.checkMoney(player.getName());
        double bank_after = economy.checkBank(player.getName());
        return checkWithdrawResult(economy, player, money_before, money_after, bank_after, bank_before);
    }

    private boolean checkWithdrawResult(UltiEconomy economy, Player player, double moneyBefore, double moneyAfter, double bankAfter, double bankBefore) {
        if ((moneyBefore == moneyAfter && bankAfter < bankBefore) || (moneyBefore < moneyAfter && bankAfter == bankBefore)) {
            if (bankAfter < bankBefore) {
                economy.addToBank(player.getName(), bankAfter - bankBefore);
            }
            if (moneyAfter > moneyBefore) {
                economy.takeFrom(player.getName(), moneyBefore - moneyAfter);
            }
            player.sendMessage(ChatColor.RED + "取款出现错误，请换个数额存款！");
            return true;
        }
        return true;
    }
}
