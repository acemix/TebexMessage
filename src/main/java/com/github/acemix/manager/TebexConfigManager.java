package com.github.acemix.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

/**
 * Created by Acemix
 * Project: TebexMessage
 * Date: 6/23/2025 @ 15:01
 */
public class TebexConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;

    public TebexConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }

    public List<String> getRequiredArgs() {
        List<String> args = config.getStringList("args-cmd");
        return args != null ? args : Collections.emptyList();
    }

    public List<String> getBroadcastMessages() {
        if (config.contains("broadcast.message")) {
            return config.getStringList("broadcast.message");
        }
        return Collections.emptyList();
    }

    public String getButtonText() {
        return config.getString("button.text");
    }

    public String getButtonHover() {
        return config.getString("button.hover");
    }

    public String getButtonUrl() {
        return config.getString("button.open_url");
    }

    public int getCenterButtonWidth() {
        return config.getInt("button.center-button");
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }
}
