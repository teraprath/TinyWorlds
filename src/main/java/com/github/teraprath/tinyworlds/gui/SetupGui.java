package com.github.teraprath.tinyworlds.gui;

import com.github.teraprath.tinylib.lang.MultiLanguage;
import com.github.teraprath.tinylib.text.TinyText;
import com.github.teraprath.tinyworlds.TinyWorlds;
import com.github.teraprath.tinyworlds.utils.SignUtils;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Arrays;

public class SetupGui extends Gui {

    private final TinyWorlds plugin;
    private final MainGui previous;
    private final MultiLanguage lang;

    public SetupGui(@Nonnull TinyWorlds plugin, @NotNull Player player, @Nonnull MainGui previous) {
        super(player, "setup", new TinyText(plugin.getLanguage().getMessage(player, "gui_setup")).toString(), 4);
        this.plugin = plugin;
        this.previous = previous;
        this.lang = plugin.getLanguage();
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

        boolean hasUnloadedWorlds = false;
        File[] worlds = Bukkit.getWorldContainer().listFiles();

        fillRow(new Icon(Material.BLACK_STAINED_GLASS_PANE).setName(new TinyText(lang.getMessage(player, "gui_placeholder")).toString()), 3);

        assert worlds != null;
        for (File file : worlds) {
            if (file.isDirectory() && Arrays.asList(file.list()).contains("level.dat") && Bukkit.getWorld(file.getName()) == null) {
                if (!(hasUnloadedWorlds)) {
                    hasUnloadedWorlds = true;
                }
                this.addItem(new Icon(Material.MAP).setName(new TinyText(lang.getMessage(player, "gui_unloaded_world_name")).value("world", file.getName()).toString()).setLore(new TinyText(lang.getMessage(player, "gui_left_click")).value("action", lang.getMessage(player, "gui_action_import")).toString(), new TinyText(lang.getMessage(player, "gui_right_click")).value("action", lang.getMessage(player, "gui_action_delete")).toString()).onClick((event) -> {
                    event.setCancelled(true);
                    if (event.isRightClick()) {
                        new ConfirmGui(plugin, player, file, this).open();
                    } else {
                        new CreateGui(plugin, player, file.getName(), this).open();
                    }
                }));
            }
        }

        addItem(30, new Icon(Material.RED_WOOL).setName(new TinyText(lang.getMessage(player, "gui_back")).toString()).onClick(clickEvent -> {
            this.previous.open();
        }));

        addItem(32, new Icon(Material.CARTOGRAPHY_TABLE).setName(new TinyText(lang.getMessage(player, "gui_setup_new_world")).toString()).onClick(clickEvent -> {
            SignUtils.send(plugin, player, "enter_name", this);
        }));

        if (!(hasUnloadedWorlds)) {
            addItem(13, new Icon(Material.BARRIER).setName(new TinyText(lang.getMessage(player, "gui_no_unloaded_worlds")).toString()));
        }
    }


}
