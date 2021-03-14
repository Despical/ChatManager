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
 * Created at 2.03.2021
 */
public class ToggleChatCommandListener implements Listener {

    public ToggleChatCommandListener(Main plugin) {
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        if (!event.getTag().equals("despical:togglechat")) {
            return;
        }

        DataInputStream input = new DataInputStream(new ByteArrayInputStream(event.getData()));

        try {
            String channel = input.readUTF();
            String playerName = input.readUTF();
            final String toggle = input.readUTF();
            String enabledMessage = input.readUTF();
            String disabledMessage = input.readUTF();
            String alreadyEnabledMessage = input.readUTF();
            String alreadyDisabledMessage = input.readUTF();

            if (!channel.equals("despicaltogglechat")) {
                return;
            }

            ProxiedPlayer sender = ProxyServer.getInstance().getPlayer(playerName);
            Configuration config = Utils.getConfig("options.yml");
            String section = "Chat";

            if (!config.contains(section)) {
                config.set(section, true);
            }

            boolean toggled = config.getBoolean(section);

            if (toggle.equalsIgnoreCase("aç")) {
                if (toggled && sender != null) {
                    sender.sendMessage(alreadyEnabledMessage);
                    return;
                }

                config.set(section, true);
                ProxyServer.getInstance().broadcast(enabledMessage);
                Utils.saveConfig(config);
            } else if (toggle.equalsIgnoreCase("kapat")) {
                if (!toggled && sender != null) {
                    sender.sendMessage(alreadyDisabledMessage);
                    return;
                }

                config.set(section, false);
                ProxyServer.getInstance().broadcast(disabledMessage);
                Utils.saveConfig(config);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}