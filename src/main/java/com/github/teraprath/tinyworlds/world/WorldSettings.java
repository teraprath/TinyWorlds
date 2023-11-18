package com.github.teraprath.tinyworlds.world;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Difficulty;
import org.bukkit.Material;

@Getter
@Setter
public class WorldSettings {

    private Material icon;
    private Difficulty difficulty;
    private boolean pvp;
    private boolean keepInventory;
    private boolean mobGriefing;
    private boolean fireTick;
    private boolean autoLoad;

    public WorldSettings() {
        this.icon = Material.MAP;
        this.difficulty = Difficulty.NORMAL;
        this.pvp = true;
        this.keepInventory = false;
        this.mobGriefing = true;
        this.fireTick = false;
        this.autoLoad = true;
    }

}
