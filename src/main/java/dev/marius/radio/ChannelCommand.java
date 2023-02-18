package dev.marius.radio;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.*;

import java.util.Optional;
import java.util.regex.Pattern;

public class ChannelCommand implements CommandExecutor {
    public static final Pattern frequencyPattern = Pattern.compile("^\\d{1,3}(.\\d{1,2})?$");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        Optional<ChannelManager.Channel> current = ChannelManager.getChannelOfPlayer(player);

        if (args.length != 1) {
            player.sendMessage(
                    Component.join(JoinConfiguration.separator(Component.space()),
                            ChannelManager.Channel.prefixComponent,
                            Component.text("Usage: /channel <frequency>").color(NamedTextColor.RED)));
            return false;
        }

        String frequency = args[0];
        if (!validateFrequency(frequency)) {
            player.sendMessage(
                    Component.join(JoinConfiguration.separator(Component.space()),
                            ChannelManager.Channel.prefixComponent,
                            Component.text("The frequency is in the wrong format: 000.00").color(NamedTextColor.RED)));
            return false;
        }

        String parsedFrequency = parseFrequency(frequency);
        ChannelManager.getChannelByFrequency(parsedFrequency).addMember(player);

        current.ifPresent(channel -> channel.removeMember(player));

        player.sendMessage(
                Component.join(JoinConfiguration.separator(Component.space()),
                        ChannelManager.Channel.prefixComponent,
                        Component.text("You joined channel " + parsedFrequency).color(NamedTextColor.RED)));

        return false;
    }

    // we have to "parse" the frequency to eliminate doubled channels:
    // 01 -> 1
    private @NotNull String parseFrequency(String frequency) {
        return String.valueOf(Float.parseFloat(frequency));
    }

    private boolean validateFrequency(@NotNull String frequency) {
        return frequency.matches(frequencyPattern.pattern());
    }
}
