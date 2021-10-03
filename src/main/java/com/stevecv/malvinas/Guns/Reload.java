package com.stevecv.malvinas.Guns;

import com.stevecv.malvinas.Guns.gunData.ReadData;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Reload implements Listener {
    @EventHandler
    public void reloadGun(PlayerInteractEvent e) throws Exception {
        Action a = e.getAction();
        Player p = e.getPlayer();
        if (a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK) {
            ItemStack gun = e.getItem();

            ReadData rd = new ReadData();
            String gunName = gun.getItemMeta().getDisplayName();

            double maxMagSize = Double.parseDouble((String) rd.readJson("plugins/Malvinas/Guns/" + gunName + ".json", "magSize"));
            String magName = (String) rd.readJson("plugins/Malvinas/Guns/" + gunName + ".json", "magName");

            ItemStack item = new ItemStack(Material.MAGMA_CREAM);
            ItemMeta meta = item.getItemMeta();
            meta.setLocalizedName(magName);
            item.setItemMeta(meta);

            Inventory inv = p.getInventory();
            if (!inv.contains(item)) { return; }
            inv.remove(item);

            ArrayList<String> lore = new ArrayList<>();
            lore.add(maxMagSize + "/" + maxMagSize);

            item.setLore(lore);

            p.playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 3f, 3f);
        }
    }
}
