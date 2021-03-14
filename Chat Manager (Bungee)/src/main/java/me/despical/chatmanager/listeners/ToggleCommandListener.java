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
public class ToggleCommandListener implements Listener {

    public ToggleCommandListener(Main plugin) {
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        if (!event.getTag().equals("despical:toggle")) {
            return;
        }

        DataInputStream input = new DataInputStream(new ByteArrayInputStream(event.getData()));

        try {
            String channel = input.readUTF();
            String playerName = input.readUTF();
            String toggleEnabledMessage = input.readUTF();
            String toggleDisableMessage = input.readUTF();

            if (!channel.equals("despicaltogglemsg")) {
                return;
            }

            ProxiedPlayer sender = ProxyServer.getInstance().getPlayer(playerName);
            Configuration config = Utils.getConfig("options.yml");
            String section = "Options." + playerName + ".toggled";

            if (!config.contains(section)) {
                config.set(section, false);
            }

            boolean toggled = config.getBoolean(section);

            if (sender != null) {
                if (toggled) {
                    config.set(section, false);

                    sender.sendMessage(toggleDisableMessage);
                } else {
                    config.set(section, true);

                    sender.sendMessage(toggleEnabledMessage);
                }

                Utils.saveConfig(config);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}