package me.despical.chatmanager.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.despical.chatmanager.Main;
import me.despical.chatmanager.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Despical
 * <p>
 * Created at 29.03.2021
 */
public class ClearChatCommand implements CommandExecutor {

    private final Main plugin;

    public ClearChatCommand(Main plugin) {
        this.plugin = plugin;

        Optional.ofNullable(plugin.getCommand("sohbetsil")).ifPresent(cmd -> cmd.setExecutor(this));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.colorize(plugin.getConfig().getString("Only-By-Players")));
            return true;
        }

        if (!sender.hasPermission(plugin.getConfig().getString("Clear-Chat-Permission")) || !sender.isOp()) {
            sender.sendMessage(Utils.colorize(plugin.getConfig().getString("No-Permission")));
            return true;
        }

        Player player = (Player) sender;

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("despicalclearchat");
        output.writeUTF(StringUtils.repeat(" \n", 100));
        output.writeUTF(Utils.colorize(plugin.getConfig().getString("Cleared-Chat").replace("%player%", args[0])));

        Utils.sendToBungeeCord(player, output, "despical:clearchat");
        return true;
    }
}