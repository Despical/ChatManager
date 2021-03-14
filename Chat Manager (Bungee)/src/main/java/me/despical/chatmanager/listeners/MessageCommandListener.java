package me.despical.chatmanager.listeners;

import me.despical.chatmanager.Main;
import me.despical.chatmanager.utils.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Despical
 * <p>
 * Created at 28.02.2021
 */
public class MessageCommandListener implements Listener {

    public MessageCommandListener(Main plugin) {
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        if (!event.getTag().equals("despical:custom")) {
            return;
        }

        DataInputStream input = new DataInputStream(new ByteArrayInputStream(event.getData()));

        try {
            String channel = input.readUTF();
            String playerName = input.readUTF();
            String targetName = input.readUTF();
            String message = input.readUTF();
            String playerNotFound = input.readUTF();
            String toggledMessage = input.readUTF();
            String ignoredMessage = input.readUTF();
            String targetIgnoredMessage = input.readUTF();

            if (!channel.equals("despicalprivatemsg")) {
                return;
            }

            ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(targetName);
            ProxiedPlayer sender = ProxyServer.getInstance().getPlayer(playerName);
            Configuration config = Utils.getConfig("options.yml");

            if (targetPlayer == null && sender != null) {
                sender.sendMessage(playerNotFound);
                return;
            }

            if (config.getBoolean("Options." + targetName + ".toggled") && sender != null) {
                sender.sendMessage(toggledMessage);
                return;
            }

            if (config.getStringList("Options." + playerName + ".ignoredList").contains(targetName) && sender != null) {
                sender.sendMessage(ignoredMessage);
                return;
            }

            if (config.getStringList("Options." + targetName + ".ignoredList").contains(playerName) && sender != null) {
                sender.sendMessage(targetIgnoredMessage);
                return;
            }

            if (targetPlayer != null) {
                targetPlayer.sendMessage(message);
            }

            if (sender != null) {
                sender.sendMessage(message);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}