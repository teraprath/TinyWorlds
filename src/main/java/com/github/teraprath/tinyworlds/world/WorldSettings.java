package com.github.teraprath.tinyworlds.world;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;

@Getter
@Setter
public class WorldSettings {

    private Material icon;
    private Difficulty difficulty;
    private boolean pvp;
    private boolean keepInventory;
    private boolean mobGriefing;
    private boolean fireTick;

    public WorldSettings() {
        this.icon = Material.MAP;
        this.difficulty = Difficulty.NORMAL;
        this.pvp = true;
        this.keepInventory = false;
        this.mobGriefing = true;
        this.fireTick = false;
    }

}
