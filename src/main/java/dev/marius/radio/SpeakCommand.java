package dev.marius.radio;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.*;

public class SpeakCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (!ChannelManager.getChannelOfPlayer(player).isPresent()) {
            player.sendMessage(
                    Component.join(JoinConfiguration.separator(Component.space()),
                            ChannelManager.Channel.prefixComponent,
                            Component.text("You are not connected to any radio. Please connect through /channel <frequency>").color(NamedTextColor.RED)));
            return false;
        }

        if (args.length < 1) {
            player.sendMessage(
                    Component.join(JoinConfiguration.separator(Component.space()),
                            ChannelManager.Channel.prefixComponent,
                            Component.text("Please provide some message you want to speak into the radio.").color(NamedTextColor.RED)));
            return false;
        }

        String message = String.join(" ", args);
        ChannelManager.sendPlayerMessage(player, message);

        return false;
    }
}
