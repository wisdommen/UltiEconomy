package com.minecraft.economy.CMDs;

import com.minecraft.economy.AbstractClasses.AbstractConsoleCommandExecutor;
import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import static com.minecraft.economy.database.MoveData.MoveDataFromLocal;
import static com.minecraft.economy.database.MoveData.MoveDataToLocal;

/**
 * 数据库和本地储存的转换
 */
public class TransferData extends AbstractConsoleCommandExecutor {

    @Override
    public boolean onConsoleCommand(@NotNull CommandSender commandSender, @NotNull Command command, String[] strings) {
        if (!(command.getName().equalsIgnoreCase("mvdb") && strings.length == 1)) {
            return false;
        }
        File file = new File(UltiEconomyMain.getInstance().getDataFolder(), "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        switch (strings[0]) {
            case "toLocal":
                if (UltiEconomyMain.getInstance().getConfig().getBoolean("enableDataBase")) {
                    commandSender.sendMessage(ChatColor.RED + "玩家数据已经存在本地！");
                    return true;
                }
                try {
                    MoveDataToLocal();
                    config.set("enableDataBase", false);
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                commandSender.sendMessage(ChatColor.RED + "经济插件已切换至YML方式储存数据！\n已将玩家数据从数据库移动到本地！");
                UltiEconomyMain.getInstance().reloadConfig();
                return true;

            case "toDataBase":
                if (!UltiEconomyMain.getInstance().getConfig().getBoolean("enableDataBase")) {
                    commandSender.sendMessage(ChatColor.RED + "玩家数据已经存在数据库！");
                    return true;
                }
                try {
                    MoveDataFromLocal();
                    config.set("enableDataBase", true);
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                commandSender.sendMessage(ChatColor.RED + "经济插件已切换至数据库储存数据！\n已将玩家数据从本地移动到数据库！");
                UltiEconomyMain.getInstance().reloadConfig();
                return true;
            default:
                return false;
        }
    }
}
