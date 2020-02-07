package mindcollaps.sceneCore.sceneController;

import com.sun.org.apache.bcel.internal.generic.JsrInstruction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mindcollaps.engines.Engine;
import mindcollaps.sceneCore.AllertBox;
import mindcollaps.sceneCore.MoveListener;
import mindcollaps.utils.Utils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

public class HomeController extends Controller {

    ArrayList<Module> modules = new ArrayList<>();
    HashMap<String, JSONObject> loadedFeatures = new HashMap<>();

    @FXML
    private MenuBar menu;

    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private VBox vContent;

    @Override
    public void initController(Engine engine, Stage primaryStage, Scene scene, Stage mainStage) {
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Module Creater");
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Close button on view clicked!");
            engine.closeProgram(true);
        });
        new MoveListener(mainAnchor, primaryStage);
        new MoveListener(menu, primaryStage);
        super.initController(engine, primaryStage, scene, mainStage);
    }

    @FXML
    private void onNewModulClicked(ActionEvent actionEvent) {
        //TODO:Sicherheit dass die Daten nicht verloren gehen
        vContent.getChildren().clear();
        modules.clear();
        Module newModul = new Module();
        modules.add(newModul);
        vContent.getChildren().add(newModul.getModulBox());
    }

    @FXML
    private void onOpenModulClicked(ActionEvent actionEvent) {
        String path = null;
        JSONObject modul = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(engine.getFileUtils().getHome()));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        try{
            File file = fileChooser.showOpenDialog(primaryStage);
            path = file.getAbsolutePath();
        } catch (Exception e){
            new AllertBox(null, Modality.APPLICATION_MODAL, engine).displayMessage("Error", "Please select a valid path!", "ok", "buttonBlue", false);
            return;
        }

        try {
            modul = engine.getFileUtils().loadJsonFile(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        vContent.getChildren().clear();
        modules.clear();
        Module newModul = new Module();
        modules.add(newModul);
        vContent.getChildren().add(newModul.getModulBox());
        addModulFromJson(modul,newModul);
    }

    @FXML
    private void onCloseClicked(ActionEvent actionEvent) {
        engine.closeProgram(true);
    }

    @FXML
    private void onSafeModulClicked(ActionEvent actionEvent) {
        Module module = null;
        if (modules.size() > 1) {
            String[] modulesNames = new String[modules.size()];
            for (int i = 0; i < modules.size(); i++) {
                modulesNames[i] = modules.get(i).getModulNameTxt();
            }
            int ret = new AllertBox(null, Modality.APPLICATION_MODAL, engine).displayChoiceBox("Which modul", "Safe modul", modulesNames, false);
            for (Module m : modules) {
                if (modulesNames[ret].equals(m.getModulNameTxt())) {
                    module = m;
                    break;
                }
            }
        } else {
            module = modules.get(0);
        }
        String path = showSafeDialog("modul");
        engine.getFileUtils().saveJsonFile(path, convertModulToJson(module));
    }

    @FXML
    private void onSafeFeatureClicked(ActionEvent actionEvent) {
        Feature feature = null;
        ArrayList<String> names = new ArrayList();
        for (Module m : modules) {
            for (Device d : m.getDeviceArrayList()) {
                for (Feature f : d.getFeaturesArray()) {
                    names.add(f.getFeatureNameTxt());
                }
            }
        }

        if (names.size() > 1) {
            int ret = new AllertBox(null, Modality.APPLICATION_MODAL, engine).displayChoiceBox("Which feature?", "Safe feature", Utils.convertArrayListToStringArray(names), false);
            for (Module m : modules) {
                for (Device d : m.getDeviceArrayList()) {
                    for (Feature f : d.getFeaturesArray()) {
                        if (f.getFeatureNameTxt().equals(names.get(ret))) {
                            feature = f;
                            break;
                        }
                    }
                }

                if (feature != null)
                    break;
            }
        } else {
            for (Module m : modules) {
                for (Device d : m.getDeviceArrayList()) {
                    for (Feature f : d.getFeaturesArray()) {
                        feature = f;
                    }
                    break;
                }
                if (feature != null)
                    break;
            }
        }

        String path = showSafeDialog("feature");
        engine.getFileUtils().saveJsonFile(path, convertFeatureToJson(feature));
    }

    @FXML
    private void onSafeAllModulesInOneClicked(ActionEvent actionEvent) {
        saveModulInOnce(showSafeDialog("modul"));
    }

    @FXML
    private void onReloadFilesClicked(ActionEvent actionEvent) {
        loadFeatures();
    }

    private String showSafeDialog(String type) {
        String path = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(engine.getFileUtils().getHome()));
        fileChooser.setInitialFileName(type);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        try {
            path = fileChooser.showSaveDialog(primaryStage).getAbsolutePath();
        } catch (Exception e) {
            new AllertBox(null, Modality.APPLICATION_MODAL, engine).displayMessage("Error", "Please select a valid path!", "ok", "buttonBlue", false);
        }
        return path;
    }

    public void loadFeatures() {
        File folder = new File(engine.getFileUtils().getHome() + "/features");
        File[] listOfFeatures = folder.listFiles();

        for (File file : listOfFeatures) {
            if (file.isFile()) {
                try {
                    loadedFeatures.put(file.getName(), engine.getFileUtils().loadJsonFile(file.getAbsolutePath()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveModulInOnce(String path) {
        JSONObject modulCompilation = new JSONObject();
        modulCompilation.put("name", "modules");
        JSONArray moduls = new JSONArray();
        for (Module module : modules) {
            JSONObject modul = convertModulToJson(module);
            moduls.add(modul);
        }
        modulCompilation.put("modules", moduls);
        engine.getFileUtils().saveJsonFile(path, modulCompilation);
    }

    private HBox putIntoHbox(Node... a) {
        HBox hBox = new HBox();
        hBox.getChildren().addAll(a);
        hBox.setSpacing(10);
        return hBox;
    }

    private VBox putIntoVbox(Node... a) {
        VBox vBox = new VBox();
        vBox.getChildren().addAll(a);
        vBox.setSpacing(25);
        return vBox;
    }

    private void addFeatureFromJson(JSONObject feature, Feature editFeature) {
        String s = "";
        for (Object o : feature.keySet().toArray()) {
            s = (String) o;
            if(s.equals("name")){
                editFeature.setFeatureNameTxt((String) feature.get("name"));
            } else {
                try {
                    //if this works, the key is a array!
                    JSONArray array = (JSONArray) feature.get(s);
                    Iterator<String> stringIterator = array.iterator();
                    FeatureLineArray featureLineArray = new FeatureLineArray(editFeature, false);
                    featureLineArray.setArrayName(s);
                    while (stringIterator.hasNext()) {
                        FeatureLineArrayContent featureLineArrayContent = new FeatureLineArrayContent(featureLineArray);
                        featureLineArrayContent.setTextField(stringIterator.next());
                        featureLineArray.addFeatureLineArrayContents(featureLineArrayContent);
                    }
                    editFeature.addArrayLine(featureLineArray);
                    //if it doesn't work, it is a normal line

                } catch (Exception e) {
                    VariableFeatureLine variableFeatureLine = new VariableFeatureLine(editFeature, false);
                    variableFeatureLine.setLabel(s);
                    variableFeatureLine.setTextField((String)feature.get(s));
                    editFeature.addVariableFeatureLine(variableFeatureLine);
                }
            }
        }
    }

    private void addModulFromJson(JSONObject module, Module editModule){
        String s = "";
        for (Object o : module.keySet().toArray()) {
            s = (String) o;
            if(s.equals("name")){
                editModule.setModulNameTxt((String) module.get(s));
            } else {
                try {
                    //if this works, the key is a array!
                    JSONArray array = (JSONArray) module.get(s);
                    Iterator<JSONObject> stringIterator = array.iterator();
                    while (stringIterator.hasNext()) {
                        Device device = new Device(editModule);
                        addDeviceFromJson(stringIterator.next(), device);
                        editModule.addDevice(device);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addDeviceFromJson(JSONObject device, Device editDevice){
        String s = "";
        for(Object o: device.keySet().toArray()){
            s = (String) o;
            if(s.equals("name")){
                editDevice.setDeviceNameTxt((String) device.get(s));
            } else if(s.equals("path")){
                editDevice.setDevicePathTxt((String) device.get(s));
            } else {
                try {
                    //if this works, the key is a array!
                    JSONArray array = (JSONArray) device.get(s);
                    Iterator<JSONObject> stringIterator = array.iterator();
                    while (stringIterator.hasNext()) {
                        Feature feature = new Feature(editDevice);
                        addFeatureFromJson(stringIterator.next(), feature);
                        editDevice.addFeature(feature);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private JSONObject convertModulToJson(Module module) {
        JSONObject jsonModul = new JSONObject();
        jsonModul.put("name", module.getModulNameTxt());
        JSONArray devices = new JSONArray();

        for (Device device : module.getDeviceArrayList()) {
            devices.add(convertDeviceToJson(device));
        }

        jsonModul.put("devices", devices);
        return jsonModul;
    }

    private JSONObject convertDeviceToJson(Device device) {
        JSONObject jsonDevice = new JSONObject();
        jsonDevice.put("name", device.getDeviceNameTxt());
        jsonDevice.put("path", device.getDevicePathTxt());
        JSONArray features = new JSONArray();

        for (Feature feature : device.getFeaturesArray()) {
            features.add(convertFeatureToJson(feature));
        }

        jsonDevice.put("features", features);
        return jsonDevice;
    }

    private JSONObject convertFeatureToJson(Feature feature) {
        JSONObject jsonFeature = new JSONObject();
        jsonFeature.put("name", feature.getFeatureNameTxt());

        for (VariableFeatureLine variableFeatureLine : feature.getVariableFeatureLines()) {
            jsonFeature.put(variableFeatureLine.getTextFieldName(), variableFeatureLine.getTextField());
        }

        for (FeatureLineArray array : feature.getFeatureLineArrayContents()) {
            JSONArray jsonArray = new JSONArray();
            for (FeatureLineArrayContent arrayContent : array.getFeatureLineArrayContents()) {
                jsonArray.add(arrayContent.getTextField());
            }
            jsonFeature.put(array.getName(), jsonArray);
        }
        return jsonFeature;
    }

    public class Module {
        Label modulName = new Label("Module name");
        TextField modulNameTxt = new TextField();
        Label devices = new Label("devices");
        VBox devicesBox = new VBox();
        Button addDevice = new Button("Add device");

        VBox modulBox;

        ArrayList<Device> deviceArrayList = new ArrayList<>();

        public Module() {
            HBox modulNameBox = putIntoHbox(modulName, modulNameTxt);
            devicesBox.setSpacing(25);
            ScrollPane deviceScroll = new ScrollPane();
            deviceScroll.setContent(devicesBox);
            addDevice.setId("buttonGreen");
            addDevice.setOnMouseClicked(event -> {
                Device newDevice = new Device(this);
                deviceArrayList.add(newDevice);
                devicesBox.getChildren().add(newDevice.getDeviceBox());
            });

            HBox devicesHbox = putIntoHbox(devices, deviceScroll, addDevice);
            modulBox = putIntoVbox(modulNameBox, devicesHbox);
        }

        public String getModulNameTxt() {
            return modulNameTxt.getText();
        }

        public void removeDevice(Device device) {
            deviceArrayList.remove(device);
            devicesBox.getChildren().remove(device.getDeviceBox());
        }

        public VBox getModulBox() {
            return modulBox;
        }

        public ArrayList<Device> getDeviceArrayList() {
            return deviceArrayList;
        }

        public void setModulNameTxt(String modulNameTxt) {
            this.modulNameTxt.setText(modulNameTxt);
        }

        public void addDevice(Device device){
            deviceArrayList.add(device);
            devicesBox.getChildren().add(device.getDeviceBox());
        }
    }

    public class Device {
        Label deviceName = new Label("Name: ");
        TextField deviceNameTxt = new TextField();
        Label devicePath = new Label("Path: ");
        TextField devicePathTxt = new TextField();
        Label features = new Label("Features");
        VBox featuresBox = new VBox();
        Button addFeature = new Button("Add feature");
        Button remove = new Button("remove");

        ArrayList<Feature> featuresArray = new ArrayList<>();

        Module module;

        VBox deviceBox;

        public Device(Module module) {
            this.module = module;
            HBox deviceNameBox = putIntoHbox(deviceName, deviceNameTxt);
            HBox devicePathBox = putIntoHbox(devicePath, devicePathTxt);
            featuresBox.setSpacing(25);
            ScrollPane featureScroll = new ScrollPane();
            featureScroll.setContent(featuresBox);
            addFeature.setId("buttonGreen");

            addFeature.setOnMouseClicked(event1 -> {
                Feature newFeature = new Feature(this);
                featuresArray.add(newFeature);
                featuresBox.getChildren().add(newFeature.getFeatureBox());
            });
            HBox featuresHBox = putIntoHbox(features, featureScroll, addFeature);

            remove.setId("buttonRed");
            remove.setOnMouseClicked(event1 -> {
                module.removeDevice(this);
            });

            deviceBox = putIntoVbox(deviceNameBox, devicePathBox, featuresHBox, remove);
        }

        public VBox getDeviceBox() {
            return deviceBox;
        }

        public String getDeviceNameTxt() {
            return deviceNameTxt.getText();
        }

        public String getDevicePathTxt() {
            return devicePathTxt.getText();
        }

        public void removeFeature(Feature feature) {
            featuresBox.getChildren().remove(feature.getFeatureBox());
            featuresArray.remove(feature);
        }

        public ArrayList<Feature> getFeaturesArray() {
            return featuresArray;
        }

        public void setDeviceNameTxt(String deviceNameTxt) {
            this.deviceNameTxt.setText(deviceNameTxt);
        }

        public void setDevicePathTxt(String devicePathTxt) {
            this.devicePathTxt.setText(devicePathTxt);
        }

        public void addFeature(Feature feature){
            featuresArray.add(feature);
            featuresBox.getChildren().add(feature.getFeatureBox());
        }
    }

    public class Feature {
        Device device;
        VBox featureBox;

        ChoiceBox featureTypeChoiceBox = new ChoiceBox();
        Label featureName = new Label("Name: ");
        TextField featureNameTxt = new TextField();
        Button remove = new Button("Remove");
        VBox variableFeatureContent = new VBox();
        ScrollPane variableFeatureContentScroll = new ScrollPane();

        ArrayList<VariableFeatureLine> variableFeatureLines = new ArrayList<>();
        ArrayList<FeatureLineArray> featureLineArrayContents = new ArrayList<>();

        public Feature(Device device) {
            this.device = device;
            featureTypeChoiceBox.getItems().add("new feature");
            featureTypeChoiceBox.getItems().addAll(loadedFeatures.keySet());
            featureTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                variableFeatureContent.getChildren().clear();
                if (newValue.equals(featureTypeChoiceBox.getItems().get(0))) {
                    Button addField = new Button("Add field");
                    addField.setId("buttonGreen");

                    Button addArray = new Button("Add array");
                    addArray.setId("buttonGreen");
                    variableFeatureContent.getChildren().add(putIntoHbox(addField, addArray));

                    addField.setOnMouseClicked(event -> {
                        VariableFeatureLine variableFeatureLine = new VariableFeatureLine(this, true);
                        variableFeatureLines.add(variableFeatureLine);
                        variableFeatureContent.getChildren().add(variableFeatureLine.getVariableFeatureLineBox());
                    });

                    addArray.setOnMouseClicked(event -> {
                        FeatureLineArray featureLineArrayContent = new FeatureLineArray(this, true);
                        featureLineArrayContents.add(featureLineArrayContent);
                        variableFeatureContent.getChildren().add(featureLineArrayContent.getArrayLine());
                    });
                } else {
                    addFeatureFromJson(loadedFeatures.get(newValue), this);
                }
            });

            variableFeatureContent.setSpacing(25);

            remove.setId("buttonRed");
            remove.setOnMouseClicked(event -> {
                device.removeFeature(this);
            });

            variableFeatureContentScroll.setContent(variableFeatureContent);
            featureBox = putIntoVbox(putIntoHbox(featureName, featureNameTxt), featureTypeChoiceBox, variableFeatureContentScroll, remove);
        }

        public VBox getFeatureBox() {
            return featureBox;
        }

        public String getFeatureNameTxt() {
            return featureNameTxt.getText();
        }

        public void removeVariableFeatureLine(VariableFeatureLine variableFeatureLine) {
            variableFeatureLines.remove(variableFeatureLine);
            variableFeatureContent.getChildren().remove(variableFeatureLine.getVariableFeatureLineBox());
        }

        public void removeArray(FeatureLineArray array) {
            featureLineArrayContents.remove(array);
            variableFeatureContent.getChildren().remove(array.getArrayLine());
        }

        public ArrayList<VariableFeatureLine> getVariableFeatureLines() {
            return variableFeatureLines;
        }

        public ArrayList<FeatureLineArray> getFeatureLineArrayContents() {
            return featureLineArrayContents;
        }

        public void setFeatureNameTxt(String featureNameTxt) {
            this.featureNameTxt.setText(featureNameTxt);
        }

        public void addVariableFeatureLine(VariableFeatureLine variableFeatureLine){
            variableFeatureLines.add(variableFeatureLine);
            variableFeatureContent.getChildren().add(variableFeatureLine.getVariableFeatureLineBox());
        }

        public void addArrayLine(FeatureLineArray array){
            featureLineArrayContents.add(array);
            variableFeatureContent.getChildren().add(array.getArrayLine());
        }
    }

    public class VariableFeatureLine {
        Label label;
        TextField textField;

        Feature feature;
        VBox variableFeatureLineBox = new VBox();

        public VariableFeatureLine(Feature feature, boolean initializeNames) {
            this.feature = feature;
            String fieldName = "";
            if(initializeNames)
                fieldName = new AllertBox(null, Modality.APPLICATION_MODAL, engine).displayTextField("Name of field", "Add field", "ok", false);
            label = new Label(fieldName + ":");
            Button remove = new Button("remove");
            remove.setId("buttonRed");
            remove.setOnMouseClicked(event1 -> {
                feature.removeVariableFeatureLine(this);
            });
            textField = new TextField();
            variableFeatureLineBox.setSpacing(25);

            variableFeatureLineBox = putIntoVbox(putIntoHbox(label, textField, remove));
        }

        public VBox getVariableFeatureLineBox() {
            return variableFeatureLineBox;
        }

        public String getTextField() {
            return textField.getText();
        }

        public String getTextFieldName() {
            return label.getText();
        }

        public void setLabel(String label) {
            this.label.setText(label);
        }

        public void setTextField(String textField) {
            this.textField.setText(textField);
        }
    }

    public class FeatureLineArray {
        VBox arrayLine = new VBox();
        Label arrayName;
        Button remove;

        Feature feature;

        ArrayList<FeatureLineArrayContent> featureLineArrayContents = new ArrayList<>();

        public FeatureLineArray(Feature feature, boolean initializeNames) {
            this.feature = feature;
            String fieldName = "";
            if(initializeNames)
                fieldName = new AllertBox(null, Modality.APPLICATION_MODAL, engine).displayTextField("Name of array", "Add field", "ok", false);
            arrayName = new Label(fieldName);
            remove = new Button("remove");
            remove.setId("buttonRed");
            remove.setOnMouseClicked(event -> {
                feature.removeArray(this);
            });

            Button addLine = new Button("Add line");
            addLine.setId("buttonGreen");
            addLine.setOnMouseClicked(event -> {
                FeatureLineArrayContent featureLineArrayContent = new FeatureLineArrayContent(this);
                featureLineArrayContents.add(featureLineArrayContent);
                arrayLine.getChildren().remove(remove);
                arrayLine.getChildren().addAll(featureLineArrayContent.getVariableFeatureLineBox(), remove);
            });
            arrayLine.setSpacing(25);
            arrayLine = putIntoVbox(putIntoHbox(arrayName, arrayLine, addLine), remove);
        }

        public VBox getArrayLine() {
            return arrayLine;
        }

        public void removeArrayLine(FeatureLineArrayContent c) {
            featureLineArrayContents.remove(c);
            arrayLine.getChildren().remove(c.getVariableFeatureLineBox());
        }

        public ArrayList<FeatureLineArrayContent> getFeatureLineArrayContents() {
            return featureLineArrayContents;
        }

        public String getName() {
            return arrayName.getText();
        }

        public void setArrayName(String arrayName) {
            this.arrayName.setText(arrayName);
        }

        public void addFeatureLineArrayContents(FeatureLineArrayContent featureLineArrayContents) {
            this.featureLineArrayContents.add(featureLineArrayContents);
            arrayLine.getChildren().remove(remove);
            arrayLine.getChildren().addAll(featureLineArrayContents.getVariableFeatureLineBox(), remove);
        }
    }

    public class FeatureLineArrayContent {
        TextField textField;

        FeatureLineArray feature;
        VBox variableFeatureLineBox = new VBox();

        public FeatureLineArrayContent(FeatureLineArray feature) {
            this.feature = feature;
            Button remove = new Button("remove");
            remove.setId("buttonRed");
            remove.setOnMouseClicked(event1 -> {
                feature.removeArrayLine(this);
            });
            textField = new TextField();
            variableFeatureLineBox.setSpacing(25);

            variableFeatureLineBox = putIntoVbox(putIntoHbox(textField, remove));
        }

        public VBox getVariableFeatureLineBox() {
            return variableFeatureLineBox;
        }

        public String getTextField() {
            return textField.getText();
        }

        public void setTextField(String textField) {
            this.textField.setText(textField);
        }
    }
}
