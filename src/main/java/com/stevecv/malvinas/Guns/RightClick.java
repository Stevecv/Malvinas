package com.stevecv.malvinas.Guns;

import com.stevecv.malvinas.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RightClick implements Listener {
    public Main main;
    public RightClick(Main main) {
        this.main = main;
    }

    @EventHandler
    public void rightClick(PlayerInteractEvent e) throws Exception {
        Player p = e.getPlayer();
        ItemStack heldItem = p.getItemInHand();

        if (heldItem.getType() != Material.STICK) { return; }
        Action a = e.getAction();
        if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
            new Shooting(main).shoot(p, heldItem);
        }
    }
}
