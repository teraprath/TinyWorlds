package com.github.teraprath.tinyworlds.world;

import com.github.teraprath.tinylib.config.TinyConfig;
import com.github.teraprath.tinylib.text.TinyText;
import com.github.teraprath.tinyworlds.TinyWorlds;
import lombok.Getter;
import mc.obliviate.inventory.Icon;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

@Getter
public class WorldConfig extends TinyConfig {

    private final TinyWorlds plugin;
    private HashMap<World, WorldSettings> worlds = new HashMap<>();

    public WorldConfig(@NotNull TinyWorlds plugin) {
        super(plugin, "worlds");
        this.plugin = plugin;
    }

    @Override
    public void onLoad(FileConfiguration config) {
        worlds = new HashMap<>();
        File[] worlds = Bukkit.getWorldContainer().listFiles();
        for (int i = 0; i < worlds.length; i++) {
            File file = worlds[i];
            if (file.isDirectory() && Arrays.asList(file.list()).contains("level.dat") && Bukkit.getWorld(file.getName()) == null) {
                if (plugin.getServer().getWorld(file.getName()) == null && config.getBoolean(file.getName() + ".auto_load")) {
                    plugin.getServer().createWorld(new WorldCreator(file.getName()).environment(World.Environment.valueOf(config.getString(file.getName() + ".environment"))));
                }
            }
        }

        plugin.getServer().getWorlds().forEach(world -> {
            WorldSettings settings = new WorldSettings();

            if (config.get(world.getName()) != null) {
                String icon = config.getString(world.getName() + ".icon");

                if (icon != null) {
                    settings.setIcon(Material.valueOf(icon.toUpperCase()));
                }

                String difficulty = config.getString(world.getName() + ".difficulty");
                if (difficulty != null) {
                    settings.setDifficulty(Difficulty.valueOf(difficulty.toUpperCase()));
                }

                settings.setPvp(config.getBoolean(world.getName() + ".pvp"));
                settings.setKeepInventory(config.getBoolean(world.getName() + ".keep_inventory"));
                settings.setMobGriefing(config.getBoolean(world.getName() + ".mob_griefing"));
                settings.setFireTick(config.getBoolean(world.getName() + ".fire_tick"));

            }
            this.worlds.put(world, settings);
        });

        this.worlds.forEach((world, worldSettings) -> {
            world.setDifficulty(worldSettings.getDifficulty());
            world.setPVP(worldSettings.isPvp());
            world.setGameRule(GameRule.MOB_GRIEFING, worldSettings.isMobGriefing());
            world.setGameRule(GameRule.KEEP_INVENTORY, worldSettings.isKeepInventory());
            world.setGameRule(GameRule.DO_FIRE_TICK, worldSettings.isFireTick());
        });
    }

    @Override
    public void onPreSave(FileConfiguration config) {
        this.worlds.forEach((world, settings) -> {
            config.set(world.getName() + ".auto_load", settings.isAutoLoad());
            config.set(world.getName() + ".environment", world.getEnvironment().name());
            config.set(world.getName() + ".icon", settings.getIcon().name());
            config.set(world.getName() + ".difficulty", settings.getDifficulty().name());
            config.set(world.getName() + ".pvp", settings.isPvp());
            config.set(world.getName() + ".keep_inventory", settings.isKeepInventory());
            config.set(world.getName() + ".mob_griefing", settings.isMobGriefing());
            config.set(world.getName() + ".fire_tick", settings.isFireTick());
        });
    }

}
