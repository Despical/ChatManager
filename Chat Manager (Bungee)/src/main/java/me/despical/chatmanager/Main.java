package me.despical.chatmanager;

import me.despical.chatmanager.events.ChatEvents;
import me.despical.chatmanager.listeners.*;
import me.despical.chatmanager.utils.Utils;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author Despical
 * <p>
 * Created at 26.02.2021
 */
public class Main extends Plugin {

    @Override
    public void onEnable() {
        getProxy().registerChannel("despical:custom");
        getProxy().registerChannel("despical:ignore");
        getProxy().registerChannel("despical:message");
        getProxy().registerChannel("despical:toggle");
        getProxy().registerChannel("despical:togglechat");
        getProxy().registerChannel("despical:around");
        getProxy().registerChannel("despical:ignoredbroadcast");
        getProxy().registerChannel("despical:clearchat");

        Utils.setPlugin(this);
        setupFiles();

        new MessageListener(this);
        new MessageCommandListener(this);
        new ToggleCommandListener(this);
        new IgnoreCommandListener(this);
        new ToggleChatCommandListener(this);
        new IgnoredMessageListener(this);
        new IgnoredBroadcastMessageListener(this);
        new ClearChatListener(this);

        new ChatEvents(this);
    }

    private void setupFiles() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "options.yml");

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("options.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}