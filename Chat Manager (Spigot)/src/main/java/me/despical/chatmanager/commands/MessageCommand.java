package me.despical.chatmanager.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.despical.chatmanager.Main;
import me.despical.chatmanager.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Despical
 * <p>
 * Created at 28.02.2021
 */
public class MessageCommand implements CommandExecutor {

    private final Main plugin;

    public MessageCommand(Main plugin) {
        this.plugin = plugin;

        Optional.ofNullable(plugin.getCommand("msg")).ifPresent(cmd -> cmd.setExecutor(this));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.colorize(plugin.getConfig().getString("Only-By-Players")));
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            sender.sendMessage(Utils.colorize(plugin.getConfig().getString("Message-Command-Usage")));
            return true;
        }

        if (args[0].equalsIgnoreCase(sender.getName())) {
            sender.sendMessage(Utils.colorize(plugin.getConfig().getString("Cant-Send-Yourself")));
            return true;
        }

        String notFound = Utils.colorize(plugin.getConfig().getString("Player-Not-Found").replace("%player%", args[0]));
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        String toggledMessages = Utils.colorize(plugin.getConfig().getString("Player-Toggled-Messages")).replace("%player%", args[0]);
        String ignoredMessage = Utils.colorize(plugin.getConfig().getString("Cant-Send-To-Ignored").replace("%player%", args[0]));
        String targetIgnoredMessage = Utils.colorize(plugin.getConfig().getString("Player-Ignored-You").replace("%player%", args[0]));

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("despicalprivatemsg");
        output.writeUTF(sender.getName());
        output.writeUTF(args[0]);
        output.writeUTF(formatString(message, player, args[0]));
        output.writeUTF(notFound);
        output.writeUTF(toggledMessages);
        output.writeUTF(ignoredMessage);
        output.writeUTF(targetIgnoredMessage);

        Utils.sendToBungeeCord(player, output, "despical:custom");
        return true;
    }

    private String formatString(String message, Player from, String to) {
        if (message == null) return "";

        String formatted = plugin.getConfig().getString("Message-Format-Sent");
        formatted = StringUtils.replace(formatted, "%from%", from.getName());
        formatted = StringUtils.replace(formatted, "%to%", to);
        formatted = Utils.colorize(formatted);
        formatted = StringUtils.replace(formatted, "%message%", ChatColor.stripColor(message));

        return formatted;
    }
}