package com.github.acemix;

import com.github.acemix.command.CommandConsole;
import com.github.acemix.command.TebexMessageReload;
import com.github.acemix.manager.TebexConfigManager;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Acemix
 * Project: TebexMessage
 * Date: 6/23/2025 @ 15:01
 */
public final class TebexMessage extends JavaPlugin {

    private static TebexConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new TebexConfigManager(this);

        if (getCommand("tebexmessage") != null) {
            getCommand("tebexmessage").setExecutor(new CommandConsole(configManager));
        } else {
            getLogger().severe("Error could not register this command");
        }

        if (getCommand("tebexmessagereload") != null) {
            getCommand("tebexmessagereload").setExecutor(new TebexMessageReload(configManager));
        } else {
            getLogger().severe("Error could not register this command");
        }
    }


    @Override
    public void onDisable() {
    }
}