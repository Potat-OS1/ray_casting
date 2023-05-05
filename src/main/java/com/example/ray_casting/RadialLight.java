package com.example.ray_casting;

import javafx.scene.paint.Color;

public class RadialLight extends LightSource{

    RadialLight(int rayCount, int originX, int originY, boolean stationary, Color lightColor, int strength, boolean isOriginal) {
        super(rayCount, originX, originY, lightColor, stationary, strength);

        for (int a = 0; a < rayCount; a++) {
            Ray r = new Ray(originX, originY, (360.0/rayCount)*a, strength);
            rays[a] = r;
            r.getRay().setOpacity(0.1);
        }

        if (isOriginal) {
            iris = new RadialLight(128, originX, originY, stationary, Color.WHITE, (int) (strength/1.5), false);
        }
    }
}
