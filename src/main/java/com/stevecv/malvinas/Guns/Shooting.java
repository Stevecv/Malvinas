package com.stevecv.malvinas.Guns;

import com.stevecv.malvinas.Guns.gunData.ReadData;
import com.stevecv.malvinas.Main;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Shooting {
    public Main main;
    public Shooting(Main main) {
        this.main = main;
    }

    public void shoot(Player p) throws Exception {
        p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 3f, 3f);

        String gunName = p.getItemInHand().getItemMeta().getDisplayName();

        ReadData rd = new ReadData();

        double vel = (double) rd.readJson("src/main/java/com/stevecv/malvinas/Guns/gunData/" + gunName + ".json", "muzzleVelocity");
        double range = (double) rd.readJson("src/main/java/com/stevecv/malvinas/Guns/gunData/" + gunName + ".json", "range");
        double effectiveFiringRange = (double) rd.readJson("src/main/java/com/stevecv/malvinas/Guns/gunData/" + gunName + ".json", "effectiveFiringRange");

        double footNumber = 3.281;

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
            if (loc.getBlock().getType() == Material.WATER) {
                S = 8 / middleCalc/footNumber;
            } else if (loc.getBlock().getType() == Material.GLASS) {
                S = 4 /middleCalc/footNumber;
                loc.getBlock().setType(Material.AIR);
            } else if (loc.getBlock().getType() == Material.AIR) {
                S = middleCalc/footNumber;
            } else if (loc.getBlock().getType() == Material.GRASS || loc.getBlock().getType() == Material.TALL_GRASS) {
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
                    Bukkit.broadcastMessage(String.valueOf(e.getHealth()-damageAmount));

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
