package com.notmarra.notcredits.nms.versions;

import com.notmarra.notcredits.Notcredits;
import com.notmarra.notcredits.nms.NMSHandler;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class NMSHandler_HS extends NMSHandler {
    @Override
    public void sendChatMessage(Player player, String message) {
        if (message.startsWith("&")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        } else {
            Audience audience = Notcredits.getInstance().adventure.player(player);
            MiniMessage mm = MiniMessage.miniMessage();
            audience.sendMessage(mm.deserialize(message));
        }
    }
}
