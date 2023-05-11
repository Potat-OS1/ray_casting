package com.example.ray_casting;

import javafx.scene.paint.Color;

public class RadialLight extends LightSource{

    RadialLight(int rayCount, int originX, int originY, boolean stationary, Color lightColor, int strength) {
        super(rayCount, originX, originY, lightColor, stationary, strength);

        for (int a = 0; a < rayCount; a++) {
            rays.add(new Ray(originX, originY, (360.0/rayCount)*a, strength));
            this.lightDropOff = Tools.imageGradientToBlack(lightColor, strength * 2, strength * 2, 120, 0);
        }
//        for (Ray ray : rays) {
//            Controller.p.getChildren().add(ray.getRay());
//        }
    }

    @Override
    void lightDropOffImage() {

    }
}
