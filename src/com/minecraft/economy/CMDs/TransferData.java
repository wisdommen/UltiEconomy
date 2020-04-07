package com.minecraft.economy.CMDs;

import com.minecraft.economy.economyMain.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

import static com.minecraft.economy.database.MoveData.MoveDataFromLocal;
import static com.minecraft.economy.database.MoveData.MoveDataToLocal;

public class TransferData implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)){
            if (command.getName().equalsIgnoreCase("mvdb") && strings.length == 1) {
                File file = new File(Economy.getInstance().getDataFolder(), "config.yml");
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

                switch (strings[0]){
                    case "toLocal":
                        if (Economy.getInstance().getConfig().getBoolean("enableDataBase")) {
                            try {
                                MoveDataToLocal();
                                config.set("enableDataBase", false);
                                config.save(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            commandSender.sendMessage(ChatColor.RED + "经济插件已切换至YML方式储存数据！");
                            commandSender.sendMessage(ChatColor.RED + "已将玩家数据从数据库移动到本地！");

                        }else {
                            commandSender.sendMessage(ChatColor.RED + "玩家数据已经存在本地！");
                        }
                        return true;

                    case "toDatabase":
                        if (!Economy.getInstance().getConfig().getBoolean("enableDataBase")) {
                            try {
                                MoveDataFromLocal();
                                config.set("enableDataBase", true);
                                config.save(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            commandSender.sendMessage(ChatColor.RED + "经济插件已切换至数据库储存数据！");
                            commandSender.sendMessage(ChatColor.RED + "已将玩家数据从本地移动到数据库！");

                        }else {
                            commandSender.sendMessage(ChatColor.RED + "玩家数据已经存在数据库！");
                        }
                        return true;
                    default:
                        return false;
                }

            }
        }
        return false;
    }
}
