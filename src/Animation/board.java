package Animation;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Vector;

public class board {
    static Scene scene;
    static Group root;
    static Canvas canvas;
    static GraphicsContext gc;
    static boolean sceneStarted = false;
    public static int enemy;
//    static Player player;
    static int CELL_SIZE = GloVariables.CELL_SIZE;
    static Label textLEVEL;
    static Label textPOINT;

    public static Vector<Tile> tiles = new Vector<>();
    public static Vector<Entity> entities = new Vector<>();
    static Comparator<Entity> layerComparator = Comparator.comparingInt(Entity::getLayer);

    public static void setupScene() {
        if (!sceneStarted) {
            init();
            sceneStarted = true;
        }
    }

    public static Scene getScene() {
        return scene;
    }

    public static GraphicsContext getGraphicsContext() {
        return gc;
    }

    public static Vector<Entity> getEntities() {
        return entities;
    }

    private static void init() {
        root = new Group();
        scene = new Scene(root, GloVariables.SCENE_WIDTH, GloVariables.SCENE_HEIGHT);
        canvas = new Canvas(GloVariables.CANVAS_WIDTH, GloVariables.CANVAS_HEIGHT);
        canvas.setLayoutX(0);
        canvas.setLayoutY(48);
        textLEVEL = new Label("LEVEL: " + GloVariables.Level);
        textLEVEL.setLayoutX(0);
        textLEVEL.setLayoutY(0);
        textLEVEL.setFont(Font.font("Bauhaus 93", 32));

        textPOINT = new Label("POINT: " + GloVariables.point);
        textPOINT.setLayoutX(144);
        textPOINT.setLayoutY(0);
        textPOINT.setFont(Font.font("Bauhaus 93", 32));

        root.getChildren().addAll(textLEVEL,textPOINT);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();
        gc.fillRect(0,0, GloVariables.CANVAS_WIDTH, GloVariables.CANVAS_HEIGHT);
        GameLoop.start(gc);
        try {
            loadMap();
        } catch (IOException e) {
            System.err.println("Unable to load map");
        }
    }

    public static void loadMap() throws IOException {
        String path = "src/Resources/map.txt";
        try (BufferedReader inputStream = new BufferedReader(new FileReader(path))) {
            String line;
            int y = 0;
            while ((line = inputStream.readLine()) != null) {
                line += "c";
                for (int x = 0; x < line.length(); x++) {
                    tiles.add(new Grass(x * GloVariables.CELL_SIZE, y * GloVariables.CELL_SIZE));
                    switch (line.charAt(x)) {
                        case '*' : tiles.add(new Wall(x * CELL_SIZE, y * CELL_SIZE));
                    }
                }
                y++;
            }
        }
        for (Tile tile : tiles) {
            addEntityToGame(tile);
        }
        System.gc();
    }

    public static void addEntityToGame(Entity e) {
        if (!entities.contains(e)) {
            entities.add(e);
            entities.sort(layerComparator);
        }
    }
}
