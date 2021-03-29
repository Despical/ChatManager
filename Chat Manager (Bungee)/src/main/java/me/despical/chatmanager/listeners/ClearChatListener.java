package me.despical.chatmanager.listeners;

import me.despical.chatmanager.Main;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Despical
 * <p>
 * Created at 29.03.2021
 */
public class ClearChatListener implements Listener {

    public ClearChatListener(Main plugin) {
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        if (!event.getTag().equals("despical:clearchat")) {
            return;
        }

        DataInputStream input = new DataInputStream(new ByteArrayInputStream(event.getData()));

        try {
            String channel = input.readUTF();
            String emptyMessage = input.readUTF();
            String clearMessage = input.readUTF();

            if (!channel.equals("despicalclearchat")) {
                return;
            }

            ProxyServer.getInstance().broadcast(emptyMessage);
            ProxyServer.getInstance().broadcast(clearMessage);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}