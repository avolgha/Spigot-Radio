package dev.marius.radio;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Radio extends JavaPlugin {
    @Override
    public void onEnable() {
        fullRegisterCommand("channel", new ChannelCommand());
        fullRegisterCommand("s", new SpeakCommand());

        getServer().getPluginManager().registerEvents(new RadioListener(), this);
    }

    private <T extends CommandExecutor & TabCompleter> void fullRegisterCommand(String name, T command) {
        PluginCommand pluginCommand = getCommand(name);
        if (pluginCommand == null) return;

        pluginCommand.setExecutor(command);
        pluginCommand.setTabCompleter(command);
    }

    @Override
    public void onDisable() {
    }
}
