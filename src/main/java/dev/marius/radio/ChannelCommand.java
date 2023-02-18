package dev.marius.radio;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.*;

import java.util.*;

public class ChannelCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        Optional<ChannelManager.Channel> current = ChannelManager.getChannelOfPlayer(player);

        if (args.length != 1) {
            player.sendMessage(
                    Component.join(JoinConfiguration.separator(Component.space()),
                            ChannelManager.Channel.prefixComponent,
                            Component.text("Usage: /channel <frequency> or /channel leave").color(NamedTextColor.RED)));
            return false;
        }

        String frequency = args[0];

        if (frequency.equalsIgnoreCase("leave")) {
            current.ifPresent(channel -> {
                channel.removeMember(player);
                player.sendMessage(
                        Component.join(JoinConfiguration.separator(Component.space()),
                                ChannelManager.Channel.prefixComponent,
                                Component.text("You disconnected from the channel.")));
            });
            return false;
        }

        if (!FrequencyHelper.validateFrequency(frequency)) {
            player.sendMessage(
                    Component.join(JoinConfiguration.separator(Component.space()),
                            ChannelManager.Channel.prefixComponent,
                            Component.text("The frequency is in the wrong format: 000.00").color(NamedTextColor.RED)));
            return false;
        }

        String parsedFrequency = FrequencyHelper.parseFrequency(frequency);
        String permission = ChannelManager.getPermissionForChannel(parsedFrequency);

        if (!checkPermission(player, permission)) {
            player.sendMessage(
                    Component.join(JoinConfiguration.separator(Component.space()),
                            ChannelManager.Channel.prefixComponent,
                            Component.text("You don't have the permission to enter that channel.").color(NamedTextColor.RED)));
            return false;
        }

        ChannelManager.getChannelByFrequency(parsedFrequency).addMember(player);
        current.ifPresent(channel -> channel.removeMember(player));

        player.sendMessage(
                Component.join(JoinConfiguration.separator(Component.space()),
                        ChannelManager.Channel.prefixComponent,
                        Component.text("You joined channel " + parsedFrequency)));

        return false;
    }

    private boolean checkPermission(Player player, String permission) {
        if (Objects.equals(permission, "non")) {
            return true;
        } else if (Objects.equals(permission, "op")) {
            return player.isOp();
        } else {
            return player.hasPermission(permission);
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("<frequency>", "leave");
        }

        return Collections.emptyList();
    }
}
