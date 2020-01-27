package mindcollaps.core;

import javafx.application.Application;
import javafx.stage.Stage;
import mindcollaps.engines.Engine;

public class Main extends Application {

    public static void main(String[] args){
        launch();
    }

    public void start(Stage primaryStage) throws Exception {
        new Engine().boot(primaryStage);
    }
}
