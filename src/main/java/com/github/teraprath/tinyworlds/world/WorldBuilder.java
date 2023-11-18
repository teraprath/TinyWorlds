package com.github.teraprath.tinyworlds.world;

import com.github.teraprath.tinyworlds.TinyWorlds;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;

import javax.annotation.Nonnull;

@Getter
@Setter
public class WorldBuilder {

    private final TinyWorlds plugin;
    private final String name;
    private final WorldSettings settings;
    private WorldType worldType;
    private World.Environment environment;
    private ChunkGenerator generator;

    public WorldBuilder(@Nonnull TinyWorlds plugin, @Nonnull String name, @Nonnull WorldSettings settings) {
        this.name = name;
        this.settings = settings;
        this.plugin = plugin;
        this.worldType = WorldType.NORMAL;
        this.environment = World.Environment.NORMAL;
    }

    public void createWorld() {
        WorldCreator creator = new WorldCreator(this.name);
        creator.environment(this.environment);
        creator.type(this.worldType);
        if (this.generator != null) { creator.generator(this.generator); }
        plugin.getWorldConfig().getWorlds().put(Bukkit.getServer().createWorld(creator), settings);
        plugin.getWorldConfig().save();
        plugin.getWorldConfig().reload();
    }


}
