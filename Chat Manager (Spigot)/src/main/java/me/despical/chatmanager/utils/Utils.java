package me.despical.chatmanager.utils;

import com.google.common.io.ByteArrayDataOutput;
import me.clip.placeholderapi.PlaceholderAPI;
import me.despical.chatmanager.Main;
import me.despical.commons.compat.VersionResolver;
import me.despical.commons.string.StringMatcher;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Despical
 * <p>
 * Created at 28.02.2021
 */
public class Utils {

    private static final Main plugin = JavaPlugin.getPlugin(Main.class);

    public static void sendToBungeeCord(Player p, ByteArrayDataOutput output, String channel) {
        p.sendPluginMessage(plugin, channel, output.toByteArray());
    }

    public static int startsWith(String toCheck) {
        if (toCheck.startsWith("!!")) return 2;
        return toCheck.startsWith("!") ? 1 : 0;
    }

    public static String formatMessage(String message, Player player, String current) {
        if (message == null) return "";

        if (plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }

        String formatted = message;

        formatted = StringUtils.replace(formatted, "%player%", player.getName());
        formatted = StringUtils.replace(formatted, "%message%", current.substring(startsWith(current)));

        return formatted;
    }

    public static String colorize(String toColor) {
        if (toColor == null) return "";
        if (VersionResolver.isCurrentEqualOrHigher(VersionResolver.ServerVersion.v1_16_R1) && toColor.contains("#")) {
            toColor = StringMatcher.matchColorRegex(toColor);
        }

        return ChatColor.translateAlternateColorCodes('&', toColor);
    }
}