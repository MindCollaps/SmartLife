package mindcollaps.sceneCore.sceneController;

import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mindcollaps.engines.Engine;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    Engine engine;
    Stage stage;
    Stage primaryStage;
    Scene scene;

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void initController(Engine engine, Stage primaryStage, Scene scene, Stage mainStage) {
        this.primaryStage = mainStage;
        this.stage = primaryStage;
        this.engine = engine;
        this.scene = scene;
        primaryStage.setScene(scene);
    }

    public void initialize(URL location, ResourceBundle resources) {

    }
}
