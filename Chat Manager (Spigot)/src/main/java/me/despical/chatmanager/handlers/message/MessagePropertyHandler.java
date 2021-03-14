package me.despical.chatmanager.handlers.message;

import me.despical.chatmanager.Main;
import me.despical.commons.configuration.ConfigUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Despical
 * <p>
 * Created at 27.02.2021
 */
public class MessagePropertyHandler implements MessageProperties {

    private final Player player;
    private final List<String> sections;
    private final FileConfiguration config;

    private List<Integer> priority;

    public MessagePropertyHandler(Player player, Main plugin) {
        this.player = player;
        this.sections = new ArrayList<>();
        this.config = ConfigUtils.getConfig(plugin, "settings");

        registerPermissions();
    }

    @Override
    public String getMessage() {
        return sections.stream().filter(key -> {
            String newKey = "Settings." + key + ".";
            String perm = config.getString(newKey + "permission");
            return priority.stream().anyMatch(p -> p == config.getInt(newKey + "priority")) && (player.isOp() || (perm.isEmpty() || player.hasPermission(perm)));
        }).map(key -> config.getString("Settings." + key + ".message")).collect(Collectors.toList()).get(0);
    }

    private void registerPermissions() {
        ConfigurationSection section = config.getConfigurationSection("Settings");

        if (section == null) {
            return;
        }

        sections.addAll(section.getKeys(false));

        this.priority = sections.stream().map(key -> config.getInt("Settings." + key + ".priority")).sorted().collect(Collectors.toList());
    }
}