package com.stevecv.malvinas.Guns;

import com.stevecv.malvinas.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DataHandling {
    public Main main;
    public DataHandling(Main main) {
        this.main = main;
    }

    public void saveData(Entity e, PersistentDataType type, String keyName, Object data) {
        NamespacedKey key = new NamespacedKey(main, keyName);
        PersistentDataContainer container = e.getPersistentDataContainer();

        container.set(key, type, data);
    }



    public Object readData(Entity e, PersistentDataType type, String keyName) {
        NamespacedKey key = new NamespacedKey(main, keyName);
        PersistentDataContainer container = e.getPersistentDataContainer();

        return container.get(key, type);
    }


    public String readDataString(Entity e, String keyName) {
        PersistentDataType type = PersistentDataType.STRING;
        NamespacedKey key = new NamespacedKey(main, keyName);
        PersistentDataContainer container = e.getPersistentDataContainer();

        return (String) container.get(key, type);
    }


    public double readDataDouble(Entity e, String keyName, double def) {
        PersistentDataType type = PersistentDataType.DOUBLE;
        NamespacedKey key = new NamespacedKey(main, keyName);
        PersistentDataContainer container = e.getPersistentDataContainer();

        if (container.get(key, type) == null) {
            saveData(e, PersistentDataType.DOUBLE, keyName, def);
        }
        return (double) container.get(key, type);
    }

    public void addToDataDouble(Entity e, String keyName, double def, double amount) {
        double currentAmount = readDataDouble(e, keyName, def);
        double newAmount = currentAmount + amount;

        saveData(e, PersistentDataType.DOUBLE, keyName, newAmount);
    }
}
