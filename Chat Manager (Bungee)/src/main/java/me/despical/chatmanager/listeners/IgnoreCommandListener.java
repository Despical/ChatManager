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
 * Created at 2.03.2021
 */
public class IgnoreCommandListener  implements Listener {

    public IgnoreCommandListener(Main plugin) {
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        if (!event.getTag().equals("despical:ignore")) {
            return;
        }

        DataInputStream input = new DataInputStream(new ByteArrayInputStream(event.getData()));

        try {
            String channel = input.readUTF();
            String playerName = input.readUTF();
            String targetName = input.readUTF();
            String ignoredMessage = input.readUTF();
            String unignoredMessage = input.readUTF();
            String notFoundMessage = input.readUTF();

            if (!channel.equals("despicalignoremsg")) {
                return;
            }

            ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(targetName);
            ProxiedPlayer sender = ProxyServer.getInstance().getPlayer(playerName);
            Configuration config = Utils.getConfig("options.yml");
            String section = "Options." + playerName + ".ignoredList";

            if (!config.contains(section)) {
                config.set(section, new ArrayList<>());
            }

            List<String> ignoredList = config.getStringList(section);
            boolean ignored = ignoredList.contains(targetName);

            if (targetPlayer == null && sender != null) {
                sender.sendMessage(notFoundMessage);
                return;
            }

            if (ignored) {
                ignoredList.remove(targetName);
                config.set(section, ignoredList);

                if (sender != null) sender.sendMessage(unignoredMessage);
            } else {
                ignoredList.add(targetName);
                config.set(section, ignoredList);

                if (sender != null) sender.sendMessage(ignoredMessage);
            }

            Utils.saveConfig(config);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}