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
public class ToggleChatCommand implements CommandExecutor {

    private final Main plugin;

    public ToggleChatCommand(Main plugin) {
        this.plugin = plugin;

        Optional.ofNullable(plugin.getCommand("sohbet")).ifPresent(cmd -> cmd.setExecutor(this));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.colorize(plugin.getConfig().getString("Only-By-Players")));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(plugin.getConfig().getString("Toggle-Chat-Permission")) || !player.isOp()) {
            sender.sendMessage(Utils.colorize(plugin.getConfig().getString("No-Permission")));
            return true;
        }

        if (args.length > 0 && !(args[0].equalsIgnoreCase("aç") || args[0].equalsIgnoreCase("kapat"))) {
            sender.sendMessage(Utils.colorize(plugin.getConfig().getString("Toggle-Chat-Command-Usage")));
            return true;
        }

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("despicaltogglechat");
        output.writeUTF(player.getName());
        output.writeUTF(args[0]);
        output.writeUTF(Utils.colorize(plugin.getConfig().getString("Enabled-Chat").replace("%player%", sender.getName())));
        output.writeUTF(Utils.colorize(plugin.getConfig().getString("Disabled-Chat").replace("%player%", sender.getName())));
        output.writeUTF(Utils.colorize(plugin.getConfig().getString("Already-Enabled").replace("%player%", sender.getName())));
        output.writeUTF(Utils.colorize(plugin.getConfig().getString("Already-Disabled").replace("%player%", sender.getName())));
        output.writeUTF(plugin.getConfig().getString("Bypass-Chat-Permission", "chatmanager.op")); // kapattıktan sonra açılmıyor if message == "/" eklemek lazım return sonra colorize ekle finito

        Utils.sendToBungeeCord(player, output, "despical:togglechat");
        return true;
    }
}