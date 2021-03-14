package me.despical.chatmanager.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.despical.chatmanager.Main;
import me.despical.chatmanager.handlers.message.MessageProperties;
import me.despical.chatmanager.handlers.message.MessagePropertyHandler;
import me.despical.chatmanager.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Despical
 * <p>
 * Created at 28.02.2021
 */
public class ChatListener implements Listener {

    private final Main plugin;
    private final Map<Player, Long> cooldowns = new HashMap<>();
    private final Map<Player, Long> globalCooldowns = new HashMap<>();

    public ChatListener(Main plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();
        MessageProperties properties = new MessagePropertyHandler(player, plugin);
        String propertyMessage = Utils.colorize(properties.getMessage());
        String formattedMessage = Utils.formatMessage(propertyMessage, player, message);

        int startsWith = Utils.startsWith(message);
        int cooldown = plugin.getConfig().getInt("Chat-Cooldown", 10);
        int globalCooldown = plugin.getConfig().getInt("Global-Cooldown", 30);
        String cooldownMessage = plugin.getConfig().getString("Chat-Cooldown-Reached");
        String cooldownBypass = plugin.getConfig().getString("Chat-Cooldown-Permission");

        if (cooldowns.containsKey(player) && startsWith != 2) {
            if (cooldown > 0 && ((System.currentTimeMillis() - cooldowns.get(player)) / 1000) % 60 <= cooldown) {
                player.sendMessage(Utils.colorize(cooldownMessage.replace("%cooldown%", Long.toString((cooldown - ((System.currentTimeMillis() - cooldowns.get(player)) / 1000) % 60)))));
                event.setCancelled(true);
                return;
            } else {
                cooldowns.remove(player);
            }
        } else if (!player.isOp() || !player.hasPermission(cooldownBypass)) {
            cooldowns.put(player, System.currentTimeMillis());
        }

        if (startsWith == 0) {
            event.setCancelled(true);
            formattedMessage = formattedMessage.replace("%server%", plugin.getConfig().getString("Server-Format.Only-Radius"));

            ByteArrayDataOutput output = ByteStreams.newDataOutput();
            output.writeUTF("despicalaroundignored");
            output.writeUTF(Utils.colorize(formattedMessage));
            output.writeUTF(player.getName());

            Bukkit.getScheduler().runTask(plugin, () -> {
                String playersAround = player.getWorld().getNearbyEntities(player.getLocation(), 30, 30, 30).stream().map(e -> {
                    if (e instanceof Player) {
                        return e.getName();
                    }

                    return "";
                }).collect(Collectors.joining(","));

                output.writeUTF(playersAround);

                Utils.sendToBungeeCord(player, output, "despical:around");
            });
        } else if (startsWith == 1) {
            event.setCancelled(true);
            formattedMessage = formattedMessage.replace("%server%", plugin.getConfig().getString("Server-Format.Current-Server").replace("%server%", player.getServer().getName()));

            ByteArrayDataOutput output = ByteStreams.newDataOutput();
            output.writeUTF("despicalignoredbroadcast");
            output.writeUTF(Utils.colorize(formattedMessage));
            output.writeUTF(player.getName());

            Utils.sendToBungeeCord(player, output, "despical:ignoredbroadcast");
        } else if (startsWith == 2) {
            if (globalCooldowns.containsKey(player)) {
                if (globalCooldown > 0 && ((System.currentTimeMillis() - globalCooldowns.get(player)) / 1000) % 60 <= globalCooldown) {
                    player.sendMessage(Utils.colorize(cooldownMessage.replace("%cooldown%", Long.toString((globalCooldown - ((System.currentTimeMillis() - globalCooldowns.get(player)) / 1000) % 60)))));
                    event.setCancelled(true);
                    return;
                } else {
                    globalCooldowns.remove(player);
                }
            } else if (!player.isOp() || !player.hasPermission(cooldownBypass)) {
                globalCooldowns.put(player, System.currentTimeMillis());
            }

            event.setCancelled(true);

            ByteArrayDataOutput output = ByteStreams.newDataOutput();
            output.writeUTF("despicalalert");
            output.writeUTF(formattedMessage);
            output.writeUTF(Utils.colorize(plugin.getConfig().getString("Server-Format.Global-Servers")));
            output.writeUTF(player.getName());

            Utils.sendToBungeeCord(player, output, "despical:message");
        }
    }
}