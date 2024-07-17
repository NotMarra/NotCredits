package com.notmarra.notcredits.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.UUID;

public class NMSMessageSender {
    private final String version;

    public NMSMessageSender() {
        this.version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    public void sendChatMessage(Player player, String message) {
        try {
            Object packet = createChatPacket(message, player.getUniqueId());
            sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to Bukkit method if NMS fails
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    private Object createChatPacket(String message, UUID playerUUID) throws Exception {
        Class<?> iChatBaseComponentClass = getNmsClass("IChatBaseComponent");
        Class<?> chatComponentTextClass = getNmsClass("ChatComponentText");
        Class<?> packetPlayOutChatClass = getNmsClass("PacketPlayOutChat");

        Object chatComponent;
        if (version.startsWith("v1_8_R")) {
            Method chatSerializer = getNmsClass("IChatBaseComponent$ChatSerializer").getMethod("a", String.class);
            chatComponent = chatSerializer.invoke(null, "{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', message) + "\"}");
        } else {
            Constructor<?> chatComponentConstructor = chatComponentTextClass.getConstructor(String.class);
            chatComponent = chatComponentConstructor.newInstance(ChatColor.translateAlternateColorCodes('&', message));
        }

        try {
            // 1.8 - 1.15
            Constructor<?> packetConstructor = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, byte.class);
            return packetConstructor.newInstance(chatComponent, (byte) 0);
        } catch (NoSuchMethodException e) {
            try {
                // 1.16+
                Class<?> chatMessageTypeClass = getNmsClass("ChatMessageType");
                Object chatMessageType = chatMessageTypeClass.getMethod("valueOf", String.class).invoke(null, "SYSTEM");
                Constructor<?> packetConstructor = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass, chatMessageTypeClass, UUID.class);
                return packetConstructor.newInstance(chatComponent, chatMessageType, playerUUID);
            } catch (ClassNotFoundException | NoSuchMethodException ex) {
                // 1.19+
                Constructor<?> packetConstructor = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass);
                return packetConstructor.newInstance(chatComponent);
            }
        }
    }

    private void sendPacket(Player player, Object packet) throws Exception {
        Method getHandleMethod = player.getClass().getMethod("getHandle");
        Object nmsPlayer = getHandleMethod.invoke(player);

        Object playerConnection;
        try {
            // 1.8 - 1.16
            playerConnection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
        } catch (NoSuchFieldException e) {
            // 1.17+
            playerConnection = nmsPlayer.getClass().getField("b").get(nmsPlayer);
        }

        Method sendPacketMethod = playerConnection.getClass().getMethod("sendPacket", getNmsClass("Packet"));
        sendPacketMethod.invoke(playerConnection, packet);
    }

    private Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException {
        if (version.startsWith("v1_17") || version.startsWith("v1_18") || version.startsWith("v1_19") || version.startsWith("v1_20")) {
            return Class.forName("net.minecraft." + nmsClassName);
        }
        return Class.forName("net.minecraft.server." + version + "." + nmsClassName);
    }
}