package me.despical.chatmanager.events;

import me.despical.chatmanager.Main;
import me.despical.chatmanager.utils.Utils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

/**
 * @author Despical
 * <p>
 * Created at 2.03.2021
 */
public class ChatEvents implements Listener {

    public ChatEvents(Main plugin) {
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (event.getMessage().startsWith("/")) return;

        Configuration config = Utils.getConfig("options.yml");
        String perm = config.getString("Bypass-Chat-Permission");
        String disabledMessage = config.getString("Cant-Speak-While-Disabled");
        boolean enabled = config.getBoolean("Chat");
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        if (!enabled) {
            if (!player.hasPermission(perm)) {
                event.setCancelled(true);
                player.sendMessage(Utils.colorize(disabledMessage));
            }
        }
    }
}