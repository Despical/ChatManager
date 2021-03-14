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
import java.util.ArrayList;
import java.util.List;

/**
 * @author Despical
 * <p>
 * Created at 6.03.2021
 */
public class IgnoredMessageListener implements Listener {

    public IgnoredMessageListener(Main plugin) {
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equals("despical:around")) {
            return;
        }

        DataInputStream input = new DataInputStream(new ByteArrayInputStream(event.getData()));

        try {
            String channel = input.readUTF();
            String message = input.readUTF();
            String senderName = input.readUTF();
            String playersAround = input.readUTF();

            if (!channel.equals("despicalaroundignored")) {
                return;
            }

            ProxiedPlayer sender = ProxyServer.getInstance().getPlayer(senderName);

            if (sender == null) return;

            List<ProxiedPlayer> newSenders = new ArrayList<>();
            for (String name : playersAround.split(",")) {
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);

                if (player != null) {
                    Configuration config = Utils.getConfig("options.yml");

                    if (config == null) return;
                    if (player.equals(sender)) continue;

                    if (!config.getStringList("Options." + player.getName() + ".ignoredList").contains(senderName)) {
                        newSenders.add(player);
                    }
                }
            }

            newSenders.forEach(p -> p.sendMessage(message));
            sender.sendMessage(message);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}