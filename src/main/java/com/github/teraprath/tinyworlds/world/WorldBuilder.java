package com.github.teraprath.tinyworlds.world;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.WorldType;

import javax.annotation.Nonnull;

@Getter
@Setter
public class WorldBuilder {

    private final String name;
    private final WorldSettings settings;
    private WorldType worldType;

    public WorldBuilder(@Nonnull String name, @Nonnull WorldSettings settings) {
        this.name = name;
        this.settings = settings;
    }



}
