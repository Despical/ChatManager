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
public class MessageListener implements Listener {

    public MessageListener(Main plugin) {
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equals("despical:message")) {
            return;
        }

        DataInputStream input = new DataInputStream(new ByteArrayInputStream(event.getData()));

        try {
            String channel = input.readUTF();
            String message = input.readUTF();
            String format = input.readUTF();
            String senderName = input.readUTF();

            if (!channel.equals("despicalalert")) {
                return;
            }

            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(senderName);
            String server = player == null ? "Global" : player.getServer().getInfo().getName();

            if (player != null) {
                Configuration config = Utils.getConfig("options.yml");

                if (config == null) return;

                player.getServer().getInfo().getPlayers().stream().filter(p -> !config.getStringList("Options." + p.getName() + ".ignoredList").contains(senderName) && !player.equals(p))
                        .forEach(p -> p.sendMessage(message.replace("%server%", format.replace("%server%", server))));
                player.sendMessage(message.replace("%server%", format.replace("%server%", server)));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}