package com.stevecv.malvinas.Guns;

import com.stevecv.malvinas.Guns.gunData.ReadData;
import org.bukkit.Bukkit;
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
            assert gun != null;
            String gunName = gun.getItemMeta().getDisplayName();
            if (gunName == null) { return; }

            double maxMagSize = Double.parseDouble((String) rd.readJson("plugins/Malvinas/Guns/" + gunName + ".json", "magSize"));
            String magName = (String) rd.readJson("plugins/Malvinas/Guns/" + gunName + ".json", "magName");

            for(ItemStack i: p.getInventory().getContents()) {
                if (i.getType() == Material.MAGMA_CREAM) {
                    if (i.getItemMeta().getDisplayName().equals(magName)) {
                        i.setAmount(i.getAmount() - 1);

                        break;
                    }
                }
            }
            //Remove mag

            ArrayList<String> lore = new ArrayList<>();
            lore.add(maxMagSize + "/" + maxMagSize);

            gun.setLore(lore);

            p.playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 3f, 3f);
        }
    }
}
