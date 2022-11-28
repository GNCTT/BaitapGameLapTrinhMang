package Animation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AnimationMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(GloVariables.GAME_NAME);
        board.setupScene();
        Scene s = board.getScene();
        primaryStage.setScene(s);
        primaryStage.show();
    }
}
