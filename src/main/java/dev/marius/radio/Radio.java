package dev.marius.radio;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Radio extends JavaPlugin {
    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("channel")).setExecutor(new ChannelCommand());
        Objects.requireNonNull(getCommand("s")).setExecutor(new SpeakCommand());

        getServer().getPluginManager().registerEvents(new RadioListener(), this);
    }

    @Override
    public void onDisable() {
    }
}
