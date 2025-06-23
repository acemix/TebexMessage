package com.github.acemix.command;

import com.github.acemix.manager.TebexConfigManager;
import com.github.acemix.utils.ColorUtils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Acemix
 * Project: TebexMessage
 * Date: 6/23/2025 @ 15:01
 */
public class CommandConsole implements CommandExecutor {

    private final TebexConfigManager configManager;

    public CommandConsole(TebexConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(ColorUtils.Set("&cThis command can only be run from the console"));
            return true;
        }

        String joinedArgs = String.join(" ", args);
        Pattern pattern = Pattern.compile("(\\w+)\\(([^)]*)\\)");
        Matcher matcher = pattern.matcher(joinedArgs);

        Map<String, String> parsedArgs = new HashMap<>();

        while (matcher.find()) {
            String key = matcher.group(1).toLowerCase();
            String value = matcher.group(2);
            parsedArgs.put(key, value);
        }

        List<String> requiredArgs = configManager.getRequiredArgs();
        for (String required : requiredArgs) {
            if (!parsedArgs.containsKey(required.toLowerCase())) {
                sender.sendMessage(ColorUtils.Set("&cError: Required argument is missing: ") + required);
                return true;
            }
        }

        List<String> messages = configManager.getBroadcastMessages();
        for (String line : messages) {
            if (line.equalsIgnoreCase("<empty>")) {
                Bukkit.broadcastMessage(ColorUtils.SetWithCenter(" "));
                continue;
            }

            if (line.contains("%button%")) {
                TextComponent button = buildButtonCentered(configManager);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.spigot().sendMessage(button);
                }
                continue;
            }

            String formatted = line;
            for (Map.Entry<String, String> entry : parsedArgs.entrySet()) {
                formatted = formatted.replace("%" + entry.getKey() + "%", entry.getValue());
            }

            formatted = ColorUtils.SetWithCenter(formatted);

            Bukkit.broadcastMessage(formatted);
        }

        return true;
    }

    private TextComponent buildButtonCentered(TebexConfigManager config) {
        String text = config.getButtonText();
        String hover = config.getButtonHover();
        String url = config.getButtonUrl();
        int centerWidth = config.getCenterButtonWidth();

        String textColored = org.bukkit.ChatColor.translateAlternateColorCodes('&', text);
        String hoverColored = org.bukkit.ChatColor.translateAlternateColorCodes('&', hover);

        int textLength = org.bukkit.ChatColor.stripColor(textColored).length();
        int spacesCount = (centerWidth - textLength) / 2;
        String padding = " ".repeat(Math.max(0, spacesCount));

        TextComponent paddingComponent = new TextComponent(padding);
        TextComponent buttonComponent = new TextComponent(textColored);

        buttonComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverColored).create()));
        buttonComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));

        paddingComponent.addExtra(buttonComponent);

        return paddingComponent;
    }
}