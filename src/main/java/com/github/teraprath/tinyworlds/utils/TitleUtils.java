package com.github.teraprath.tinyworlds.utils;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class TitleUtils {

    public static void send(@Nonnull Player player, @Nonnull String message) {
        player.sendTitle("", message, 0, 999, 0);
    }

    public static void clear(@Nonnull Player player) {
        player.sendTitle("", "");
    }

}
