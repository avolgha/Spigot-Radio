package dev.marius.radio;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.*;

import java.util.*;

public final class ChannelManager {
    public static final Map<String, Channel> channelStorage = new LinkedHashMap<>();
    public static final Map<String, String> permissionStorage = new LinkedHashMap<>();

    private ChannelManager() {
    }

    public static @NotNull Channel getChannelByFrequency(String frequency) {
        Optional<Channel> channel = Optional.ofNullable(channelStorage.get(frequency));
        if (channel.isPresent()) {
            return channel.get();
        }

        Channel newChannel = new Channel(frequency);
        channelStorage.put(frequency, newChannel);
        return newChannel;
    }

    public static void sendMessageToChannel(String frequency, String message) {
        Channel channel = getChannelByFrequency(frequency);
        if (channel.getMembers().isEmpty()) {
            return;
        }

        channel.sendMessage(message);
    }

    public static void sendPlayerMessage(Player player, String message) {
        Optional<Channel> channel = getChannelOfPlayer(player);
        channel.ifPresent(value -> sendMessageToChannel(value.getFrequency(), message));
    }

    public static Optional<Channel> getChannelOfPlayer(Player player) {
        for (Channel channel : channelStorage.values()) {
            if (channel.getMembers().contains(player)) {
                return Optional.of(channel);
            }
        }
        return Optional.empty();
    }

    public static String getPermissionForChannel(String frequency) {
        if (permissionStorage.containsKey(frequency)) {
            return permissionStorage.get(frequency);
        }

        String wildcardPermission = frequency.substring(0, frequency.indexOf('.') + 1) + "x";
        if (permissionStorage.containsKey(wildcardPermission)) {
            return permissionStorage.get(wildcardPermission);
        }

        return "non";
    }

    public static void setupPermissions(@NotNull ConfigurationSection channels) {
        for (String key : channels.getKeys(true)) {
            if (channels.get(key) instanceof MemorySection) continue;

            if (FrequencyHelper.validateFrequency(key)) {
                String parsed = FrequencyHelper.parseFrequency(key);
                String permission = channels.getString(key);

                if (isInvalidPermission(permission)) {
                    System.err.printf("[WARN RadioPlugin] unknown format for channel permission: `%s`%n", permission);
                    continue;
                }

                permissionStorage.put(parsed, permission);
                continue;
            }

            if (!key.endsWith("x") || !key.contains(".") || !key.matches(FrequencyHelper.wildcardPattern.pattern())) {
                System.err.printf("[WARN RadioPlugin] unknown format for channel frequency: `%s`%n", key);
                continue;
            }

            String permission = channels.getString(key);

            if (isInvalidPermission(permission)) {
                System.err.printf("[WARN RadioPlugin] unknown format for channel permission: `%s`%n", permission);
                continue;
            }

            permissionStorage.put(key, permission);
        }
    }

    private static boolean isInvalidPermission(String permission) {
        return permission == null || permission.length() < 1;
    }

    public static class Channel {
        public static final Component prefixComponent = Component.text("[Radio]").color(NamedTextColor.BLUE);

        String frequency;
        List<Player> members;

        public Channel(String frequency) {
            this.frequency = frequency;
            this.members = new ArrayList<>();
        }

        public String getFrequency() {
            return frequency;
        }

        public List<Player> getMembers() {
            return members;
        }

        public void addMember(Player player) {
            this.members.add(player);
        }

        public void removeMember(Player player) {
            this.members.remove(player);

            if (this.members.isEmpty()) {
                ChannelManager.channelStorage.remove(this.frequency);
            }
        }

        public void sendMessage(String message) {
            Component messageComponent = Component.join(
                    JoinConfiguration.separator(Component.space()),
                    prefixComponent,
                    Component.text(message).color(NamedTextColor.GRAY)
            );
            this.members.forEach(member -> member.sendMessage(messageComponent));
        }
    }
}
