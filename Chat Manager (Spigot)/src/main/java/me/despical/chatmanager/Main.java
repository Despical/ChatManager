package me.despical.chatmanager;

import me.despical.chatmanager.commands.*;
import me.despical.chatmanager.listeners.ChatListener;
import me.despical.chatmanager.utils.ExceptionLogHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * @author Despical
 * <p>
 * Created at 26.02.2021
 */
public class Main extends JavaPlugin {

    private ExceptionLogHandler exceptionLogHandler;

    @Override
    public void onEnable() {
         this.exceptionLogHandler = new ExceptionLogHandler(this);

        saveDefaultConfig();
        setupFiles();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "despical:message");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "despical:custom");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "despical:ignore");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "despical:toggle");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "despical:togglechat");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "despical:around");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "despical:ignoredbroadcast");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "despical:clearchat");

        new MessageCommand(this);
        new IgnoreCommand(this);
        new ToggleCommand(this);
        new ChatListener(this);
        new ToggleChatCommand(this);
        new ClearChatCommand(this);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().removeHandler(exceptionLogHandler);
    }

    private void setupFiles() {
        String[] files = {"settings"};

        for (String fileName : files) {
            File file = new File(getDataFolder() + File.separator + fileName + ".yml");

            if (!file.exists()) {
                saveResource(fileName + ".yml", false);
            }
        }
    }
}