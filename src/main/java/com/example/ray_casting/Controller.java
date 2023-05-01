package com.example.ray_casting;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Controller extends Application {
    public static ArrayList<Light> Lights = new ArrayList<>();
    public static ArrayList<Obstactle> obstacles = new ArrayList<>();
    public static double mousex;
    public static double mousey;
    public static Rectangle shadows = new Rectangle(600, 600);
    public static Pane top = new Pane(shadows);
    public static Pane p = new Pane(top);


    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(p, 1000, 1000);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        //1154 / 256
        Light one = new Light(1154, 200, 200, false, Color.BLUE);
        Light two = new Light(256, 275, 275, true, Color.ORANGERED);
        Lights.add(one);
        Lights.add(two);
        obstactles(p);
        mouseTracking(scene);

        AnimationTimer timer = new Update();
        timer.start();
    }

    private void obstactles (Pane p) {
        Point2D[] points1 = {
                new Point2D(100,200),
                new Point2D(150, 250),
                new Point2D(200,200),
                new Point2D(200,100),
                new Point2D(100,100),
        };
        Obstactle obs1 = new Obstactle(points1, Color.RED);
        obstacles.add(obs1);
        p.getChildren().add(obs1.getObs());

        Point2D[] points2 = {
                new Point2D(300,400),
                new Point2D(350, 450),
                new Point2D(400,400),
                new Point2D(400,300),
                new Point2D(300,300),
//                new Point2D(500,400),
//                new Point2D(500,500),
//                new Point2D(400,500),
//                new Point2D(400,400),
        };
        Obstactle obs2 = new Obstactle(points2, Color.RED);
        obstacles.add(obs2);
        p.getChildren().add(obs2.getObs());
    }


    private void mouseTracking (Scene scene) {
        scene.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
            mousex = e.getX();
            mousey = e.getY();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}