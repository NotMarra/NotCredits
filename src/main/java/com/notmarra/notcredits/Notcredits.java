package com.notmarra.notcredits;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.notmarra.notcredits.events.Economy_NotCredits;
import com.notmarra.notcredits.events.Placeholders;
import com.notmarra.notcredits.events.PlayerJoin;
import com.notmarra.notcredits.util.*;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class NotCredits extends JavaPlugin {

    private static NotCredits instance;
    FileConfiguration config = this.getConfig();
    Updater updater;

    @Override
    public void onEnable() {
        instance = this;
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveDefaultConfig();

        this.updater = new Updater(this, "NotCredits", this.getDescription().getVersion(), "1", "1", "https://github.com/NotMarra/NotCredits/releases");

        DatabaseManager.getInstance(this).setupDB();

        LangFiles.createLang();
        LanguageManager.loadMessages();

        updater.checkFilesAndUpdate();

        this.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);

        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            CommandCreator commandCreator = new CommandCreator();

            commands.register(Commands.literal("credits")
                    .then(
                            Commands.argument("action", new com.notmarra.notcredits.util.Arguments())
                                    .then(
                                            Commands.argument("player", StringArgumentType.word())
                                                    .then(
                                                            Commands.argument("amount", DoubleArgumentType.doubleArg())
                                                    )
                                    )
                                    .executes(context -> {
                                        commandCreator.execute(context.getSource(), new String[]{"argument1", "argument2"});
                                        return Command.SINGLE_SUCCESS;
                                    })
                    ).build()
            );
            commands.register("nc", "Base command of NotCredits", commandCreator);
            commands.register("notcredits", "Base command of NotCredits", commandCreator);
        });

        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            this.updater.checkForUpdates();
        }, 0L, 432000L);

        if (this.config.getBoolean("vault")) {
            if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
                Bukkit.getServicesManager().register(Economy.class, new Economy_NotCredits(), this, ServicePriority.Normal);
                this.getLogger().info("Successfully connected to plugin vault!");
            } else {
                this.getLogger().severe("Vault is enabled in config, but not found");
                this.getLogger().severe("Shutting down the plugin...");
                this.getServer().getPluginManager().disablePlugin(this);
            }
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            (new Placeholders(this)).register();
            this.getLogger().info("Successfully loaded placeholders!");
        } else {
            this.getLogger().info("PlaceholderAPI not found, not loading placeholders!");
        }

        Metrics metrics = new Metrics(this, 18464);

        metrics.addCustomChart(new SimplePie("language", () -> {
            return config.getString("lang", "unknown").toUpperCase();
        }));

        metrics.addCustomChart(new SimplePie("database_type", () -> {
            return config.getString("data.type", "unknown").toUpperCase();
        }));

        this.getLogger().info("Enabled successfully!");
    }

    @Override
    public void onDisable() {
        if (DatabaseManager.getInstance(this).isConnected()) {
            DatabaseManager.getInstance(this).close();
        }
        this.getLogger().info("Disabled successfully!");
    }

    public static NotCredits getInstance() {
        return instance;
    }

    public static final List<String> SUPPORTED_LANGUAGES = Arrays.asList(
            "en", "cz", "zhcn", "ptbr"
    );

    public void reload() {
        this.reloadConfig();
        this.config = this.getConfig();
        LanguageManager.loadMessages();
        this.getLogger().info("Plugin reloaded successfully!");
    }

    public enum Arguments {
        ADD,
        REMOVE,
        SET,
        RELOAD,
        HELP
    }

    /**
     * Sets the balance for a player identified by their unique UUID.
     * Parameters:
     * @param uuid: Unique identifier for the player.
     * @param amount: The amount to set as the player's new balance.
     */
    public void setBalance(String uuid, double amount) {
        DatabaseManager.getInstance(this).setBalance(uuid, amount);
    }

    /**
     * Gets the balance for a player identified by their unique UUID.
     * @param uuid
     * @return The balance of the player.
     */

    public double getBalance(String uuid) {
        return DatabaseManager.getInstance(this).getBalance(uuid);
    }

    /**
     * Sets the balance for a player identified by their Name.
     * @param name
     * @return The balance of the player.
     */
    public void setBalanceByName(String name, double amount) {
        DatabaseManager.getInstance(this).setBalanceByPlayerName(name, amount);
    }

    /**
     * Gets the balance for a player identified by their Name.
     * @param name
     * @return The balance of the player.
     */
    public double getBalanceByName(String name) {
        return DatabaseManager.getInstance(this).getBalanceByPlayerName(name);
    }

    /**
     * Retrieves the balance of a player based on their position in the balance ranking.
     * @param order
     * @return The balance of the player.
     */
    public double getBalanceByOrder(int order) {
        return DatabaseManager.getInstance(this).getBalanceByOrder(order);
    }

    /**
     * Retrieves the name of the player based on their position in the balance ranking.
     * @param order
     * @return The name of the player.
     */
    public String getTopPlayerName(int order) {
        return DatabaseManager.getInstance(this).getPlayerByBalance(order);
    }

    /**
     * Retrieves a list of players and their balances starting from a specified rank position.
     * @param position
     * @param amount
     * @return List<Map<String, Double>>
     */
    public List<Map<String, Double>> getTopPlayersWithBalance(int position, int amount) {
        return DatabaseManager.getInstance(this).getPlayersByBalance(position, amount);
    }
}
