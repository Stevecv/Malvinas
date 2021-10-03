package com.stevecv.malvinas.Guns.gunData;

public class Converter {
    public double rpmToSpaceMs(double rpm) {
        double rps = rpm/60.0;
        double spaceSeconds = 1.0/rps;

        return spaceSeconds*1000.0;
    }
}
