package com.notmarra.notcredits.nms.versions;

import com.notmarra.notcredits.nms.NMSHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class NMSHandler_Legacy extends NMSHandler {
    @Override
    public void sendChatMessage(Player player, String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        player.sendMessage(message);
    }
}
