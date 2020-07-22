package com.minecraft.economy.versionChecker;

import com.minecraft.economy.economyMain.UltiEconomyMain;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Config file check.
 */
public class ConfigFileCheck {

    private static Map<String, Object> getAll(){
        Map<String, Object> config = new HashMap<>();

        for (String key : UltiEconomyMain.getInstance().getConfig().getKeys(false)){
            Object value = UltiEconomyMain.getInstance().getConfig().get(key);
            config.put(key, value);
        }

        return config;
    }

    /**
     * Review config file.
     */
    public static void reviewConfigFile(){
        Map<String, Object> config = getAll();
        File file = new File(UltiEconomyMain.getInstance().getDataFolder(), "config.yml");

        if (file.delete()){
            UltiEconomyMain.getInstance().saveDefaultConfig();
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            for (String key : configuration.getKeys(false)) {
                if (config.containsKey(key)) {
                    configuration.set(key, config.get(key));
                }
            }
            try {
                configuration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
