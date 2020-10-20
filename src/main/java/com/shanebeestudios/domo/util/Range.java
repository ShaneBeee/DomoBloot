package com.shanebeestudios.domo.util;

import java.util.Random;

public class Range {

    private final float min, max;

    public Range(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public float get() {
        Random random = new Random();
        return min + random.nextFloat() * (max - min);
    }

}
