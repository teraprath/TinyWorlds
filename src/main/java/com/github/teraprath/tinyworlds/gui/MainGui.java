package com.github.teraprath.tinyworlds.gui;

import com.github.teraprath.tinylib.item.TinyItem;
import com.github.teraprath.tinylib.lang.MultiLanguage;
import com.github.teraprath.tinylib.text.TinyText;
import com.github.teraprath.tinyworlds.TinyWorlds;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class MainGui extends Gui {

    private final TinyWorlds plugin;
    public MainGui(@NotNull Player player, TinyWorlds plugin) {
        super(player, "worlds", plugin.getName() + " v" + plugin.getDescription().getVersion(), 1);
        this.plugin = plugin;
        updateSize();
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        MultiLanguage lang = plugin.getLanguage();
        fillGui(new Icon(Material.BLACK_STAINED_GLASS_PANE).setName(new TinyText(lang.getMessage(player, "gui_placeholder")).toString()));
        AtomicInteger slot = new AtomicInteger();
        plugin.getWorldConfig().getWorlds().forEach(((world, worldSettings) -> {
            addItem(slot.get(), new Icon(new TinyItem(worldSettings.getIcon()).get()).setName(new TinyText(lang.getMessage(player, "gui_world_name")).value("world", world.getName()).toString()).setLore(new TinyText(lang.getMessage(player, "gui_world_online_players")).value("amount", world.getPlayerCount()).toString(), new TinyText(lang.getMessage(player, "gui_left_click")).value("action", lang.getMessage(player, "gui_action_settings")).toString(), new TinyText(lang.getMessage(player, "gui_right_click")).value("action", lang.getMessage(player, "gui_action_teleport")).toString()).onClick(clickEvent -> {
                clickEvent.setCancelled(true);
                if (clickEvent.isRightClick()) {
                    player.teleport(world.getSpawnLocation());
                } else if (clickEvent.isLeftClick()) {
                    new SettingsGui(plugin, player, world, this).open();
                }
            }));
            slot.getAndIncrement();
        }));
        addItem(slot.get(), new Icon(Material.GREEN_STAINED_GLASS_PANE).setName(new TinyText(lang.getMessage(player, "gui_new_world")).toString()).onClick(clickEvent -> {
            clickEvent.setCancelled(true);
            player.closeInventory();
        }));
    }

    private void updateSize() {
        if (plugin.getWorldConfig().getWorlds().size() > 44) { setSize(6); }
        if (plugin.getWorldConfig().getWorlds().size() > 35) { setSize(5); }
        if (plugin.getWorldConfig().getWorlds().size() > 26) { setSize(4); }
        if (plugin.getWorldConfig().getWorlds().size() > 17) { setSize(3); }
        if (plugin.getWorldConfig().getWorlds().size() > 8) { setSize(2); }
    }


}
