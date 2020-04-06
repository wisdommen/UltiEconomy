package com.minecraft.economy.money;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;

import static com.minecraft.economy.apis.checkMoney.get_player_file;

public class onJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        Player player = event.getPlayer();
        File file = get_player_file(player.getName());
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            config.set("money", 1000);
            config.set("bank", 0);
            config.save(file);
        }
    }
}
