package com.github.teraprath.tinyworlds.utils;

import com.github.teraprath.tinylib.text.TinyText;
import com.github.teraprath.tinyworlds.TinyWorlds;
import com.github.teraprath.tinyworlds.gui.CreateGui;
import com.github.teraprath.tinyworlds.gui.SetupGui;
import io.github.rapha149.signgui.SignGUI;
import io.github.rapha149.signgui.SignGUIAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collections;

public class SignUtils {

    public static void send(@Nonnull TinyWorlds plugin, @Nonnull Player player, @Nonnull String key, @Nonnull SetupGui previous) {
        SignGUI input = SignGUI.builder()
                .setLine(3, new TinyText(plugin.getLanguage().getMessage(player, "gui_setup_" + key)).toString())
                .setType(Material.CHERRY_SIGN)
                .setHandler((p, result) -> {
                    String name = result.getLineWithoutColor(0);
                    if (name.isEmpty()) {
                        Bukkit.getServer().getScheduler().runTask(plugin, () -> {
                            previous.open();
                        });
                    } else {
                        if (plugin.getServer().getWorld(name) == null) {
                            Bukkit.getServer().getScheduler().runTask(plugin, () -> {
                                new CreateGui(plugin, player, name, previous).open();
                            });
                        } else {
                            reOpen(plugin, player, "already_exists", previous);
                        }
                    }
                    return Collections.emptyList();
                })
                .build();
        player.closeInventory();
        input.open(player);
    }

    private static void reOpen(@Nonnull TinyWorlds plugin, @Nonnull Player player, @Nonnull String message, @Nonnull SetupGui previous) {
        Bukkit.getServer().getScheduler().runTask(plugin, () -> {
            send(plugin, player, message, previous);
        });
    }

}
