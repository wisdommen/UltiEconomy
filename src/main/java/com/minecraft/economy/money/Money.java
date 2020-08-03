package com.minecraft.economy.money;


import com.minecraft.economy.AbstractClasses.AbstractPlayerCommandExecutor;
import com.minecraft.economy.apis.UltiEconomy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


/**
 * 查看金币
 */
public class Money extends AbstractPlayerCommandExecutor {

    @Override
    public boolean onPlayerCommand(@NotNull Command command, String[] strings, @NotNull Player player, @NotNull UltiEconomy economy) {
        if (!("money".equalsIgnoreCase(command.getName()) && strings.length == 0)) {
            return false;
        }
        player.sendMessage(String.format(ChatColor.GOLD + "你有%d枚金币！", economy.checkMoney(player.getName())));
        return true;
    }
}