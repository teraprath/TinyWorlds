package com.github.teraprath.tinyworlds.gui;

import com.github.teraprath.tinylib.lang.MultiLanguage;
import com.github.teraprath.tinylib.text.TinyText;
import com.github.teraprath.tinyworlds.TinyWorlds;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.codehaus.plexus.util.FileUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

public class ConfirmGui extends Gui {

    private final TinyWorlds plugin;
    private final File file;
    private final SetupGui previous;

    public ConfirmGui(@Nonnull TinyWorlds plugin, @NotNull Player player, @Nonnull File file, @Nonnull SetupGui previous) {
        super(player, "confirm_delete", new TinyText(plugin.getLanguage().getMessage(player, "gui_delete")).value("world", file.getName()).toString(), InventoryType.HOPPER);
        this.plugin = plugin;
        this.file = file;
        this.previous = previous;
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        MultiLanguage lang = plugin.getLanguage();
        fillGui(new Icon(Material.BLACK_STAINED_GLASS_PANE).setName(new TinyText(lang.getMessage(player, "gui_placeholder")).toString()));

        addItem(1, new Icon(Material.RED_WOOL).setName(new TinyText(lang.getMessage(player, "gui_cancel")).toString()).onClick(clickEvent -> {
            clickEvent.setCancelled(true);
            this.previous.open();
        }));
        addItem(3, new Icon(Material.LIME_WOOL).setName(new TinyText(lang.getMessage(player, "gui_confirm")).toString()).onClick(clickEvent -> {
            clickEvent.setCancelled(true);
            try {
                FileUtils.deleteDirectory(file);
                this.previous.open();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }));

    }

}
