package mindcollaps.engines;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mindcollaps.sceneCore.sceneController.HomeController;

import java.io.IOException;

public class ViewEngine {

    Engine engine;
    
    private HomeController homeController;

    boolean viewLoaded = false;
    Stage primaryStage;

    public ViewEngine(Engine engine, Stage primaryStage) {
        this.engine = engine;
        this.primaryStage = primaryStage;
    }
    
    public void boot(){
        setupView();
    }

    private void setupView() {
        loadScenes();
        homeController.loadFeatures();
        openHome();
        viewLoaded = true;
    }

    private void openHome() {
        homeController.getStage().show();
        homeController.getStage().toFront();
    }

    private void loadScenes() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/home.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            engine.closeProgram(false);
        }
        homeController = loader.getController();
        Scene scene = new Scene(root);
        homeController.initController(engine, new Stage(), scene, primaryStage);
    }
}
