package mindcollaps.sceneController;

import javafx.scene.Scene;
import javafx.stage.Stage;
import mindcollaps.engines.Engine;

public class Controller {

    Engine engine;
    Stage primaryStage;
    Stage mainStage;
    Scene scene;

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void initController(Engine engine, Stage primaryStage, Scene scene, Stage mainStage) {
        this.mainStage = mainStage;
        this.primaryStage = primaryStage;
        this.engine = engine;
        this.scene = scene;
    }
}
