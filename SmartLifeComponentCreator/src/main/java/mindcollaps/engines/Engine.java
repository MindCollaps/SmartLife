package mindcollaps.engines;

import javafx.stage.Stage;
import mindcollaps.utils.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

public class Engine {

    Stage primaryStage;
    FileUtils fileUtils = new FileUtils(this, "data");
    ViewEngine viewEngine;

    ArrayList<JSONObject> modules = new ArrayList<>();
    ArrayList<JSONObject> features = new ArrayList<>();


    public void boot(Stage primary) {
        primaryStage = primary;
        viewEngine = new ViewEngine(this, primaryStage);
        loadAllFiels();
        viewEngine.boot();
    }

    public void closeProgram(boolean safe) {
        if (safe) safeAllFiles();
        System.exit(0);
    }

    public void safeAllFiles() {
        safeFeatures();
        safeModuls();
    }

    public void loadAllFiels() {
        loadFeatures();
        loadModules();
    }

    public void loadFeatures() {
        File folder = new File(fileUtils.getHome() + "/features");
        JSONObject object;
        JSONParser parser = new JSONParser();
        if(folder.exists()){
            for (final File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    if(file.getAbsolutePath().endsWith(".json")){
                        try {
                            features.add(fileUtils.loadJsonFile(file.getAbsolutePath()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            System.out.println("Feature folder doesnt exist...creating");
            folder.mkdirs();
        }
    }

    public void loadModules() {
        File folder = new File(fileUtils.getHome() + "/modules");
        JSONObject object;
        JSONParser parser = new JSONParser();
        if(folder.exists()){
            for (final File file : folder.listFiles()) {
                if (file.isDirectory()) {
                    if(file.getAbsolutePath().endsWith(".json")){
                        try {
                            features.add(fileUtils.loadJsonFile(file.getAbsolutePath()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            System.out.println("Feature folder doesnt exist...creating");
            folder.mkdirs();
        }
    }

    public void safeFeatures(){
        File folder = new File(fileUtils.getHome() + "/features");
        for (JSONObject object:features) {
            fileUtils.saveJsonFile(folder.getAbsolutePath() + "/" + object.get("name") + ".json", object);
        }
    }

    public void safeModuls(){
        File folder = new File(fileUtils.getHome() + "/moduls");
        for (JSONObject object:features) {
            fileUtils.saveJsonFile(folder.getAbsolutePath() + "/" + object.get("name") + ".json", object);
        }
    }

    public FileUtils getFileUtils() {
        return fileUtils;
    }
}
