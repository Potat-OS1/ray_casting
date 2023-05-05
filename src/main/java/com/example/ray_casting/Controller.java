package com.example.ray_casting;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

public class Controller extends Application {
    public static ArrayList<LightSource> Lights = new ArrayList<>();
    public static ArrayList<Obstactle> obstacles = new ArrayList<>();
    public static double mousex;
    public static double mousey;
    public static Shape shadows = new Rectangle(600, 600);
    //public static Pane top = new Pane(shadows);
    public static Pane top = new Pane();
    public static Pane p = new Pane();

    public static final int sceneX = 800;
    public static final int sceneY = 800;


    @Override
    public void start(Stage stage) {
        ImageView iv = new ImageView(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("/image.jpg"))));
        p.getChildren().addAll(iv, top);


        Scene scene = new Scene(p, sceneX, sceneY);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        //1154 / 256
        //RadialLight one = new RadialLight(1154, 200, 200, false, Color.rgb(0, 0, 255, 1), 200, true);
        //RadialLight two = new RadialLight(512, 275, 275, true, Color.rgb(0, 255, 0, 1), 100, true);
        //RadialLight three = new RadialLight(512, 275, 500, true, Color.TRANSPARENT, 100, true);
        //Lights.add(one);
        //Lights.add(two);
        //Lights.add(three);
        BeamLight bl1 = new BeamLight(10, 275, 275, Color.BLUE, true, 200, 30, 100);
        Lights.add(bl1);
        obstactles(p);
        mouseTracking(scene);

        AnimationTimer timer = new Update();
        //timer.start();
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
        };
        Obstactle obs2 = new Obstactle(points2, Color.RED);
        obstacles.add(obs2);
        p.getChildren().add(obs2.getObs());

        Point2D[] points3 = {
                new Point2D(500, 800),
                new Point2D(500, 0),
                new Point2D(550, 0),
                new Point2D(550, 800),
        };
        Obstactle obs3 = new Obstactle(points3, Color.RED);
        obstacles.add(obs3);
        p.getChildren().add(obs3.getObs());
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