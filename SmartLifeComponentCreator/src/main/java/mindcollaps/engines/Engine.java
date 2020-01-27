package mindcollaps.engines;

import javafx.stage.Stage;
import mindcollaps.utils.FileUtils;

public class Engine {

    Stage primaryStage;
    FileUtils fileUtils = new FileUtils(this);
    ViewEngine viewEngine;


    public void boot(Stage primary){
        primaryStage = primary;
        viewEngine = new ViewEngine(this, primaryStage);

        viewEngine.boot();
    }

    public void closeProgram(boolean safe) {
        if(safe)safeAllFiles();
        System.exit(0);
    }

    private void safeAllFiles() {
    }
}
