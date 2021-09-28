package com.stevecv.malvinas.Guns;

import com.stevecv.malvinas.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RightClick implements Listener {
    public Main main;
    public RightClick(Main main) {
        this.main = main;
    }

    @EventHandler
    public void rightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack heldItem = p.getItemInHand();

        if (heldItem.getType() == Material.STICK) {
            new Shooting(main).shoot(p);
        }
    }
}
