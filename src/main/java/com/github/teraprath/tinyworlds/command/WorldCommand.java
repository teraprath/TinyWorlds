package com.github.teraprath.tinyworlds.command;

import com.github.teraprath.tinyworlds.TinyWorlds;
import com.github.teraprath.tinyworlds.gui.MainGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class WorldCommand implements CommandExecutor {

    private final TinyWorlds plugin;

    public WorldCommand(@Nonnull TinyWorlds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (sender instanceof Player) {
            new MainGui((Player) sender, plugin).open();
        }
        return false;
    }
}
