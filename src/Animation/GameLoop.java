package Animation;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

import java.util.Vector;

public class GameLoop {

    static double currentTime;
    static double prevTime;
    final static long startTime = System.nanoTime();

    public static double getCurrentTime() {
        return currentTime;
    }

    public static void start(GraphicsContext gc) {
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                prevTime = currentTime;
                currentTime = (currentNanoTime - startTime) / (1000000000.0);
                gc.clearRect(0, 0, GloVariables.CANVAS_WIDTH, GloVariables.CANVAS_WIDTH);
                renderGame();
            }
        }.start();
    }

    public static void renderGame() {
        Vector<Entity> entities = board.getEntities();
        for (int i = 0; i < entities.size(); ++i) {
            Entity e = entities.elementAt(i);
            e.render();
        }
    }
}

