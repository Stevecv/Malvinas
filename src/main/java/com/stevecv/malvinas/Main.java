package com.stevecv.malvinas;

import com.stevecv.malvinas.Guns.Reload;
import com.stevecv.malvinas.Guns.RightClick;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {

        Bukkit.getServer().getPluginManager().registerEvents(new RightClick(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Reload(), this);

        Bukkit.broadcastMessage("Checking folders...");
        File file = new File("plugins/Malvinas/");
        if (!file.exists()) {
            file.mkdir();
        }

        File gunFile = new File("plugins/Malvinas/Guns/");
        if (!gunFile.exists()) {
            gunFile.mkdir();
        }
    }
}
