package me.despical.chatmanager.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.despical.chatmanager.Main;
import me.despical.chatmanager.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Despical
 * <p>
 * Created at 2.03.2021
 */
public class IgnoreCommand implements CommandExecutor {

    private final Main plugin;

    public IgnoreCommand(Main plugin) {
        this.plugin = plugin;

        Optional.ofNullable(plugin.getCommand("engelle")).ifPresent(cmd -> cmd.setExecutor(this));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.colorize(plugin.getConfig().getString("Only-By-Players")));
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            sender.sendMessage(Utils.colorize(plugin.getConfig().getString("Ignore-Command-Usage")));
            return true;
        }

        if (args[0].equalsIgnoreCase(sender.getName())) {
            sender.sendMessage(Utils.colorize(plugin.getConfig().getString("Cant-Ignore-Yourself")));
            return true;
        }

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("despicalignoremsg");
        output.writeUTF(player.getName());
        output.writeUTF(args[0]);
        output.writeUTF(Utils.colorize(plugin.getConfig().getString("Ignored-Player").replace("%player%", args[0])));
        output.writeUTF(Utils.colorize(plugin.getConfig().getString("Unignored-Player").replace("%player%", args[0])));
        output.writeUTF(Utils.colorize(plugin.getConfig().getString("Player-Not-Found").replace("%player%", args[0])));

        Utils.sendToBungeeCord(player, output, "despical:ignore");
        return true;
    }
}