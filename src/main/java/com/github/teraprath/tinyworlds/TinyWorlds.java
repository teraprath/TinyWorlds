package com.github.teraprath.tinyworlds;

import com.github.teraprath.tinylib.lang.MultiLanguage;
import com.github.teraprath.tinyworlds.command.WorldCommand;
import com.github.teraprath.tinyworlds.world.WorldConfig;
import lombok.Getter;
import mc.obliviate.inventory.InventoryAPI;
import org.bukkit.plugin.java.JavaPlugin;

public final class TinyWorlds extends JavaPlugin {

    @Getter private final WorldConfig worldConfig = new WorldConfig(this);
    @Getter private final MultiLanguage language = new MultiLanguage(this, "lang", "en_us");

    @Override
    public void onEnable() {

        new InventoryAPI(this).init();

        this.worldConfig.init();
        this.language.init();

        getCommand("worlds").setExecutor(new WorldCommand(this));
    }

}
