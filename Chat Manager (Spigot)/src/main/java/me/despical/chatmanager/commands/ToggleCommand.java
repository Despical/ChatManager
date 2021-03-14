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
 * Created at 1.03.2021
 */
public class ToggleCommand implements CommandExecutor {

    private final Main plugin;

    public ToggleCommand(Main plugin) {
        this.plugin = plugin;

        Optional.ofNullable(plugin.getCommand("togglemsg")).ifPresent(cmd -> cmd.setExecutor(this));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.colorize(plugin.getConfig().getString("Only-By-Players")));
            return true;
        }

        Player player = (Player) sender;

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("despicaltogglemsg");
        output.writeUTF(player.getName());
        output.writeUTF(Utils.colorize(plugin.getConfig().getString("Toggled-Enabled")));
        output.writeUTF(Utils.colorize(plugin.getConfig().getString("Toggle-Disabled")));

        Utils.sendToBungeeCord(player, output, "despical:toggle");
        return true;
    }
}