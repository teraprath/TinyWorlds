package com.github.teraprath.tinyworlds.gui;

import com.github.teraprath.tinylib.lang.MultiLanguage;
import com.github.teraprath.tinylib.text.TinyText;
import com.github.teraprath.tinyworlds.TinyWorlds;
import com.github.teraprath.tinyworlds.utils.TitleUtils;
import com.github.teraprath.tinyworlds.world.WorldBuilder;
import com.github.teraprath.tinyworlds.world.WorldSettings;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateGui extends Gui {

    private final TinyWorlds plugin;
    private final WorldBuilder builder;
    private final SetupGui previous;
    private final MultiLanguage lang;
    private List<String> generatorNames;
    private List<ChunkGenerator> generators;
    private int currentGenerator;

    public CreateGui(@Nonnull TinyWorlds plugin, @NotNull Player player, @Nonnull String worldName, @Nonnull SetupGui previous) {
        super(player, "create", new TinyText(plugin.getLanguage().getMessage(player, "gui_setup")).add(": ").add(worldName).toString(), 4);
        this.plugin = plugin;
        this.previous = previous;
        this.lang = plugin.getLanguage();
        this.builder = new WorldBuilder(plugin, worldName, new WorldSettings());
        loadGenerators();
        currentGenerator = 0;
    }


    @Override
    public void onOpen(InventoryOpenEvent e) {
        fillGui(new Icon(Material.BLACK_STAINED_GLASS_PANE).setName(new TinyText(lang.getMessage(player, "gui_placeholder")).toString()));

        addItem(30, new Icon(Material.RED_WOOL).setName(new TinyText(lang.getMessage(player, "gui_back")).toString()).onClick(clickEvent -> {
            this.previous.open();
        }));

        String action = "gui_setup_create";
        File[] worlds = Bukkit.getWorldContainer().listFiles();
        boolean isUnloaded = false;
        if (worlds != null) {
            for (File file : worlds) {
                if (file.getName().equals(builder.getName()) && file.isDirectory() && Arrays.asList(file.list()).contains("level.dat") && Bukkit.getWorld(file.getName()) == null) {
                    action = "gui_setup_import";
                    isUnloaded = true;
                    break;
                }
            }
        }

        boolean finalIsUnloaded = isUnloaded;
        addItem(32, new Icon(Material.CRAFTING_TABLE).setName(new TinyText(lang.getMessage(player, action)).toString()).onClick(clickEvent -> {
            if (finalIsUnloaded) {
                TitleUtils.send(player, new TinyText(lang.getMessage(player, "title_import_world")).value("world", builder.getName()).toString());
            } else {
                TitleUtils.send(player, new TinyText(lang.getMessage(player, "title_create_world")).value("world", builder.getName()).toString());
            }
            this.player.closeInventory();
            builder.createWorld();
            new MainGui(player, plugin).open();
            TitleUtils.clear(player);
        }));

        update();
    }

    private void loadPage() {
        addItem(11, new Icon(Material.GLOW_ITEM_FRAME).setName(new TinyText(lang.getMessage(player, "gui_setup_environment")).toString()).setLore("§7" + builder.getEnvironment().name()).onClick(clickEvent -> {
            clickEvent.setCancelled(true);
            switch (builder.getEnvironment()) {
                case CUSTOM:
                    builder.setEnvironment(World.Environment.NETHER);
                    break;
                case NETHER:
                    builder.setEnvironment(World.Environment.NORMAL);
                    break;
                case NORMAL:
                    builder.setEnvironment(World.Environment.THE_END);
                    break;
                case THE_END:
                    builder.setEnvironment(World.Environment.CUSTOM);
                    break;
            }
            update();
        }));

        addItem(13, new Icon(Material.FLOWER_POT).setName(new TinyText(lang.getMessage(player, "gui_setup_type")).toString()).setLore("§7" + builder.getWorldType().name()).onClick(clickEvent -> {
            clickEvent.setCancelled(true);
            switch (builder.getWorldType()) {
                case NORMAL:
                    builder.setWorldType(WorldType.FLAT);
                    break;
                case FLAT:
                    builder.setWorldType(WorldType.AMPLIFIED);
                    break;
                case AMPLIFIED:
                    builder.setWorldType(WorldType.LARGE_BIOMES);
                    break;
                case LARGE_BIOMES:
                    builder.setWorldType(WorldType.NORMAL);
                    break;
            }
            update();
        }));

        addItem(15, new Icon(Material.LARGE_AMETHYST_BUD).setName(new TinyText(lang.getMessage(player, "gui_setup_generator")).toString()).setLore("§7" + generatorNames.get(currentGenerator)).onClick(clickEvent -> {
            clickEvent.setCancelled(true);
            currentGenerator++;
            if (currentGenerator >= generators.size()) {
                currentGenerator = 0;
                builder.setGenerator(generators.get(currentGenerator));
            }
            update();
        }));
    }

    private void loadGenerators() {
        Plugin[] plugins = this.plugin.getServer().getPluginManager().getPlugins();
        List<String> generatorNames = new ArrayList<>();
        List<ChunkGenerator> generators = new ArrayList<>();
        generatorNames.add("NORMAL");
        generators.add(plugin.getDefaultWorldGenerator("world", ""));
        for (Plugin p : plugins) {
            if (p.isEnabled() && p.getDefaultWorldGenerator("world", "") != null) {
                generatorNames.add(p.getDescription().getName());
                generators.add(p.getDefaultWorldGenerator("world", ""));
            }
        }
        this.generatorNames = generatorNames;
        this.generators = generators;
    }


    private void update() {
        loadPage();
    }

}
