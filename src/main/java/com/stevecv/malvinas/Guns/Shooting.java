package com.stevecv.malvinas.Guns;

import com.stevecv.malvinas.Guns.gunData.Converter;
import com.stevecv.malvinas.Guns.gunData.ReadData;
import com.stevecv.malvinas.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Shooting {
    public Main main;
    public Shooting(Main main) {
        this.main = main;
    }

    public void shoot(Player p, ItemStack item) throws Exception {
        ReadData rd = new ReadData();
        String gunName = item.getItemMeta().getDisplayName();
        DataHandling dh = new DataHandling(main);

        double rpm = Double.parseDouble((String) rd.readJson("plugins/Malvinas/Guns/" + gunName + ".json", "roundsPerMin"));

        double now = System.currentTimeMillis();
        if (!(now-dh.readDataDouble(p, "lastShotTime", new Converter().rpmToSpaceMs(rpm)) >= new Converter().rpmToSpaceMs(rpm))) { return; }
        dh.saveData(p, PersistentDataType.DOUBLE, "lastShotTime", now);

        double maxMagSize = Double.parseDouble((String) rd.readJson("plugins/Malvinas/Guns/" + gunName + ".json", "magSize"));
        if (item.getLore() == null) {
            ArrayList<String> lore = new ArrayList<>();
            lore.add(maxMagSize + "/" + maxMagSize);

            item.setLore(lore);
        }
        String bulletsS = item.getLore().get(0).split("/")[0];
        double bullets = Double.parseDouble(bulletsS);

        bullets = bullets-1;

        if (bullets <= 0.0) {
            p.playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 3f, 3f);
            return;
        }

        ArrayList<String> lore = new ArrayList<>();
        lore.add(bullets + "/" + maxMagSize);

        item.setLore(lore);



        p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 3f, 3f);

        double vel = Double.parseDouble((String) rd.readJson("plugins/Malvinas/Guns/" + gunName + ".json", "muzzleVelocity"));
        double range = Double.parseDouble((String) rd.readJson("plugins/Malvinas/Guns/" + gunName + ".json", "range"));
        double effectiveFiringRange = Double.parseDouble((String) rd.readJson("plugins/Malvinas/Guns/" + gunName + ".json", "effectiveFiringRange"));

        double footNumber = 3.281;

        if (range > 1500.0) {
            range = 1500.0;
        }

        //Smoke
        Location origin = p.getEyeLocation();
        Vector direction = origin.getDirection();
        direction.multiply(range);

        direction.normalize();
        for (int i = 0; i < range; i++) {
            Location loc = origin.add(direction);
        }

        double removeY = 0;
        range = 2500;
        origin = p.getEyeLocation();
        direction = origin.getDirection();
        direction.multiply(range);

        direction.normalize();
        double newDistance = 0.0;

        Location last = null;
        ArrayList<Location> raycast = new ArrayList<>();
        for (double i = 0.0; i < range; i++) {
            newDistance++;
            Location loc = origin.add(direction);

            if (raycast.size() > 1) {
                last = raycast.get(raycast.size()-2);
            }

            double S;
            double middleCalc = Math.pow(0.5 * 32 * (vel / i), 2);
            Block b = loc.getBlock();
            Material type = b.getType();
            if (type == Material.WATER) {
                S = 8 / middleCalc/footNumber;
            } else if (type == Material.GLASS) {
                S = 4 /middleCalc/footNumber;
                loc.getBlock().setType(Material.AIR);
            } else if (type == Material.GLASS_PANE) {
                S = 3 /middleCalc/footNumber;
                loc.getBlock().setType(Material.AIR);
            } else if (type == Material.AIR) {
                S = middleCalc/footNumber;
            } else if (type == Material.GRASS || loc.getBlock().getType() == Material.TALL_GRASS) {
                S = 2 /middleCalc/footNumber;
            } else {
                break;
            }

            if (last != null) {
                LivingEntity e = traceLocation(loc, last);
                if (e != null) {
                    double damageAmount = (effectiveFiringRange/i)+20;
                    if (e.getHealth()-damageAmount < 0) {
                        damageAmount = e.getHealth();
                    }

                    e.setHealth(e.getHealth() - damageAmount);
                }
            }

            raycast.add(loc.clone());
            if (S <= newDistance) {
                newDistance = 0.0;
                loc = loc.add(new Vector(0, -1, 0));
            }
        }
    }


    public LivingEntity traceLocation(Location from, Location to) {
        World w = from.getWorld();
        Vector vec = to.toVector().subtract(from.toVector());

        RayTraceResult result = w.rayTraceEntities(from, vec, 1.5);
        if (result != null) {
            return (LivingEntity) result.getHitEntity();
        } else {
            return null;
        }
    }
}
