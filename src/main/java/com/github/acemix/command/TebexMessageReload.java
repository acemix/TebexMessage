package com.github.acemix.command;

import com.github.acemix.manager.TebexConfigManager;
import com.github.acemix.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

/**
 * Created by Acemix
 * Project: TebexMessage
 * Date: 6/23/2025 @ 15:01
 */
public class TebexMessageReload implements CommandExecutor {

    private final TebexConfigManager configManager;

    public TebexMessageReload(TebexConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(ColorUtils.Set("&cThis command can only be run from the console"));
            return true;
        }

        configManager.reloadConfig();
        sender.sendMessage(ColorUtils.Set("&aConfiguration successfully reloaded"));

        return true;
    }
}
