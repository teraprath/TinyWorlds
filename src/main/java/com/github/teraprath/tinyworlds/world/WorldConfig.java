package com.github.teraprath.tinyworlds.world;

import com.github.teraprath.tinylib.config.TinyConfig;
import com.github.teraprath.tinyworlds.TinyWorlds;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

@Getter
public class WorldConfig extends TinyConfig {

    private final TinyWorlds plugin;
    private final HashMap<World, WorldSettings> worlds = new HashMap<>();

    public WorldConfig(@NotNull TinyWorlds plugin) {
        super(plugin, "worlds");
        this.plugin = plugin;
    }

    @Override
    public void onLoad(FileConfiguration config) {
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
            config.set(world.getName() + ".icon", settings.getIcon().name());
            config.set(world.getName() + ".difficulty", settings.getDifficulty().name());
            config.set(world.getName() + ".pvp", settings.isPvp());
            config.set(world.getName() + ".keep_inventory", settings.isKeepInventory());
            config.set(world.getName() + ".mob_griefing", settings.isMobGriefing());
            config.set(world.getName() + ".fire_tick", settings.isFireTick());
        });
    }

}
