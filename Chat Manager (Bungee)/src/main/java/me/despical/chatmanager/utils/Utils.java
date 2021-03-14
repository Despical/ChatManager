package me.despical.chatmanager.utils;

import me.despical.chatmanager.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author Despical
 * <p>
 * Created at 2.03.2021
 */
public class Utils {

    private static Main plugin;

    public static void setPlugin(Main plugin) {
        Utils.plugin = plugin;
    }

    public static Configuration getConfig(String name) {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), name));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public static void saveConfig(Configuration configuration) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(plugin.getDataFolder(), "options.yml"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static String colorize(String toColor) {
        return toColor == null ? "" : ChatColor.translateAlternateColorCodes('&', toColor);
    }
}