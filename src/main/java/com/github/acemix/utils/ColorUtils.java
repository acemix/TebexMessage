package com.github.acemix.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Acemix
 * Project: TebexMessage
 * Date: 6/23/2025 @ 15:01
 */
public class ColorUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern SPACE_PATTERN = Pattern.compile("<(\\d{1,2})>");
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static String Set(String message) {
        return TranslateColor(TranslateHex(SafeMiniMessage(message)));
    }

    public static String SetWithCenter(String message) {
        if (message == null || message.isEmpty()) return "";

        message = applySpacePlaceholders(message);
        message = applyCenter(message);
        message = SafeMiniMessage(message);
        message = TranslateHex(message);
        message = TranslateColor(message);

        return message;
    }

    public static String SetPlaceholders(Player player, String message) {
        if (player != null && message != null) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }
        return Set(message);
    }

    public static String TranslateColor(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String TranslateHex(String message) {
        final char colorChar = ChatColor.COLOR_CHAR;
        final Matcher matcher = HEX_PATTERN.matcher(message);
        final StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            final String group = matcher.group(1);
            matcher.appendReplacement(buffer, colorChar + "x"
                    + colorChar + group.charAt(0) + colorChar + group.charAt(1)
                    + colorChar + group.charAt(2) + colorChar + group.charAt(3)
                    + colorChar + group.charAt(4) + colorChar + group.charAt(5));
        }

        return matcher.appendTail(buffer).toString();
    }

    private static String applyCenter(String raw) {
        StringBuilder result = new StringBuilder();
        String[] lines = raw.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.contains("<center>") && line.contains("</center>")) {
                String content = line.replace("<center>", "").replace("</center>", "");
                String centered = CenterUtils.centerMessage(content);
                result.append(centered);
            } else {
                result.append(line);
            }

        }

        return result.toString();
    }

    public static String SafeMiniMessage(String message) {
        try {
            Component component = MINI_MESSAGE.deserialize(message);
            return LegacyComponentSerializer.legacySection().serialize(component);
        } catch (Exception e) {
            return message;
        }
    }

    private static String applySpacePlaceholders(String message) {
        Matcher matcher = SPACE_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            int spaceCount = Integer.parseInt(matcher.group(1));
            StringBuilder spacesBuilder = new StringBuilder();
            for (int i = 0; i < spaceCount; i++) {
                spacesBuilder.append(" ");
            }
            String spaces = spacesBuilder.toString();
            matcher.appendReplacement(buffer, spaces);
        }

        matcher.appendTail(buffer);
        return buffer.toString();
    }
}
