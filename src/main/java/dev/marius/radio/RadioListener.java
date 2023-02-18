package dev.marius.radio;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.*;

import java.util.Optional;

public class RadioListener implements Listener {
    @EventHandler
    public void onQuit(@NotNull PlayerQuitEvent event) {
        Optional<ChannelManager.Channel> channelOptional = ChannelManager.getChannelOfPlayer(event.getPlayer());
        channelOptional.ifPresent(channel -> channel.removeMember(event.getPlayer()));
    }
}
