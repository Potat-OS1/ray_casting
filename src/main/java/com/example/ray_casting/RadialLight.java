package com.example.ray_casting;

import javafx.scene.paint.Color;

public class RadialLight extends LightSource{

    RadialLight(int rayCount, int originX, int originY, boolean stationary, Color lightColor, int strength, boolean isOriginal) {
        super(rayCount, originX, originY, lightColor, stationary, strength);


        for (int a = 0; a < rayCount; a++) {
            rays.add(new Ray(originX, originY, (360.0/rayCount)*a, strength));
        }
//        for (Ray ray : rays) {
//            Controller.p.getChildren().add(ray.getRay());
//        }

        if (isOriginal) {
            iris = new RadialLight(rayCount, originX, originY, stationary, Color.WHITE, (int) (strength/2.5), false);
        }
    }
}
