package com.github.teraprath.tinyworlds.gui;

import com.github.teraprath.tinylib.item.TinyItem;
import com.github.teraprath.tinylib.lang.MultiLanguage;
import com.github.teraprath.tinylib.text.TinyText;
import com.github.teraprath.tinyworlds.TinyWorlds;
import com.github.teraprath.tinyworlds.utils.TitleUtils;
import com.github.teraprath.tinyworlds.world.WorldSettings;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.advancedslot.AdvancedSlotManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class SettingsGui extends Gui {

    private final TinyWorlds plugin;
    private final World world;
    private final MainGui previous;
    private final AdvancedSlotManager slotManager;
    private final MultiLanguage lang;
    public SettingsGui(@Nonnull TinyWorlds plugin, @NotNull Player player, @Nonnull World world, @Nonnull MainGui previousGui) {
        super(player, "settings", world.getName(), 6);
        this.plugin = plugin;
        this.world = world;
        this.previous = previousGui;
        this.slotManager = new AdvancedSlotManager(this);
        this.lang = plugin.getLanguage();
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {

        fillGui(new Icon(Material.BLACK_STAINED_GLASS_PANE).setName(new TinyText(lang.getMessage(player, "gui_placeholder")).toString()));

        addItem(49, new Icon(Material.RED_WOOL).setName(new TinyText(lang.getMessage(player, "gui_back")).toString()).onClick(clickEvent -> {
            this.previous.open();
        }));

        update();
    }

    private void loadPage() {

        WorldSettings settings = plugin.getWorldConfig().getWorlds().get(world);

        Icon placeholder = new Icon(new TinyItem(settings.getIcon()).get()).setName(new TinyText(lang.getMessage(player, "gui_settings_icon")).toString()).setLore("§7" + settings.getIcon().name(), new TinyText(lang.getMessage(player, "gui_drag_to_change")).toString());
        slotManager.addAdvancedIcon(10, placeholder).onPut((clickEvent, itemStack) -> {
            settings.setIcon(itemStack.getType());
            update();
        }).onPickup((clickEvent, itemStack) -> {
            settings.setIcon(Material.MAP);
            update();
        });

        Location loc = world.getSpawnLocation();
        String action = new TinyText(lang.getMessage(player, "gui_settings_not_in_world")).toString();
        if (player.getWorld().equals(world)) {
            action = new TinyText(lang.getMessage(player, "gui_click")).value("action", lang.getMessage(player, "gui_action_spawn")).toString();
        }

        Icon spawn = new Icon(new TinyItem(Material.ARMOR_STAND).addItemFlags(ItemFlag.values()).get()).setName(new TinyText(lang.getMessage(player, "gui_settings_spawn")).toString()).setLore("§7" + loc.getX() + "§8, §7" + loc.getY() +  "§8, §7" + loc.getZ(), action);
        addItem(12, spawn.onClick(clickEvent -> {
            if (player.getWorld().equals(world)) {
                world.setSpawnLocation(player.getLocation());
                update();
            }
        }));

        Icon fireTick = new Icon(new TinyItem(Material.BLAZE_POWDER).addItemFlags(ItemFlag.values()).get()).setName(new TinyText(lang.getMessage(player, "gui_settings_fire_tick")).toString()).setLore("§7" + settings.isFireTick(), new TinyText(lang.getMessage(player, "gui_click_to_change")).toString());
        addItem(14, fireTick.onClick(clickEvent -> {
            settings.setFireTick(!(settings.isFireTick()));
            update();
        }));

        Icon difficulty = new Icon(new TinyItem(Material.TOTEM_OF_UNDYING).addItemFlags(ItemFlag.values()).get()).setName(new TinyText(lang.getMessage(player, "gui_settings_difficulty")).toString()).setLore("§7" + settings.getDifficulty().name(), new TinyText(lang.getMessage(player, "gui_click_to_change")).toString());
        addItem(16, difficulty.onClick(clickEvent -> {
            switch (settings.getDifficulty()) {
                case EASY:
                    settings.setDifficulty(Difficulty.NORMAL);
                    break;
                case NORMAL:
                    settings.setDifficulty(Difficulty.HARD);
                    break;
                case HARD:
                    settings.setDifficulty(Difficulty.PEACEFUL);
                    break;
                case PEACEFUL:
                    settings.setDifficulty(Difficulty.EASY);
                    break;
            }
            update();
        }));

        Icon pvp = new Icon(new TinyItem(Material.SHIELD).addItemFlags(ItemFlag.values()).get()).setName(new TinyText(lang.getMessage(player, "gui_settings_pvp")).toString()).setLore("§7" + settings.isPvp(), new TinyText(lang.getMessage(player, "gui_click_to_change")).toString());
        addItem(28, pvp.onClick(clickEvent -> {
            settings.setPvp(!(settings.isPvp()));
            update();
        }));

        Icon keepInventory = new Icon(new TinyItem(Material.CHEST_MINECART).addItemFlags(ItemFlag.values()).get()).setName(new TinyText(lang.getMessage(player, "gui_settings_keep_inventory")).toString()).setLore("§7" + settings.isKeepInventory(), new TinyText(lang.getMessage(player, "gui_click_to_change")).toString());
        addItem(30, keepInventory.onClick(clickEvent -> {
            settings.setKeepInventory(!(settings.isKeepInventory()));
            update();
        }));

        Icon mobGriefing = new Icon(new TinyItem(Material.TNT).addItemFlags(ItemFlag.values()).get()).setName(new TinyText(lang.getMessage(player, "gui_settings_mob_griefing")).toString()).setLore("§7" + settings.isMobGriefing(), new TinyText(lang.getMessage(player, "gui_click_to_change")).toString());
        addItem(32, mobGriefing.onClick(clickEvent -> {
            settings.setMobGriefing(!(settings.isMobGriefing()));
            update();
        }));

        boolean isLevelName = plugin.getServer().getWorlds().get(0).equals(world);
        String lore = new TinyText(lang.getMessage(player, "gui_click")).value("action", lang.getMessage(player, "gui_action_unload")).toString();
        if (isLevelName) {
            lore = new TinyText(lang.getMessage(player, "gui_cannot_unload")).toString();
        }

        Icon unload = new Icon(new TinyItem(Material.STONECUTTER).addItemFlags(ItemFlag.values()).get()).setName(new TinyText(lang.getMessage(player, "gui_settings_unload")).toString()).setLore(lore);
        addItem(34, unload.onClick(clickEvent -> {
            clickEvent.setCancelled(true);
            if (!(isLevelName)) {
                TitleUtils.send(player, new TinyText(lang.getMessage(player, "title_unload_world")).value("world", world.getName()).toString());
                player.closeInventory();
                world.getPlayers().forEach(p -> {
                    p.teleport(plugin.getServer().getWorlds().get(0).getSpawnLocation());
                });
                plugin.getWorldConfig().getWorlds().get(world).setAutoLoad(false);
                plugin.saveConfig();
                plugin.getServer().unloadWorld(world, true);
                plugin.reloadConfig();
                new MainGui(player, plugin).open();
                TitleUtils.clear(player);
            }
        }));
    }

    private void update() {
        loadPage();
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        plugin.getWorldConfig().save();
        plugin.getWorldConfig().reload();
    }

}
