package com.github.teraprath.tinyworlds.gui;

import com.github.teraprath.tinylib.item.TinyItem;
import com.github.teraprath.tinylib.lang.MultiLanguage;
import com.github.teraprath.tinylib.text.TinyText;
import com.github.teraprath.tinyworlds.TinyWorlds;
import com.github.teraprath.tinyworlds.world.WorldSettings;
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
        plugin.getServer().getWorlds().forEach((world -> {
            WorldSettings worldSettings = plugin.getWorldConfig().getWorlds().get(world);
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
        addItem(slot.get(), new Icon(Material.GREEN_STAINED_GLASS_PANE).setName(new TinyText(lang.getMessage(player, "gui_add_world")).toString()).onClick(clickEvent -> {
            clickEvent.setCancelled(true);
            new SetupGui(plugin, player, this).open();
        }));
    }

    private void updateSize() {
        if (plugin.getServer().getWorlds().size() >= 45) { sendSizeUpdate(6 * 9); }
        if (plugin.getServer().getWorlds().size() >= 36) { sendSizeUpdate(5 * 9); }
        if (plugin.getServer().getWorlds().size() >= 27) { sendSizeUpdate(4 * 9); }
        if (plugin.getServer().getWorlds().size() >= 18) { sendSizeUpdate(3 * 9); }
        if (plugin.getServer().getWorlds().size() >= 9) { sendSizeUpdate(2 * 9); }
    }


}
