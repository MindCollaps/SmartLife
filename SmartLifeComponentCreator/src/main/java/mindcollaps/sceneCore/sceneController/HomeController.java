package mindcollaps.sceneCore.sceneController;

import com.sun.org.apache.bcel.internal.generic.JsrInstruction;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeController extends Controller {

    ArrayList<Module> modules = new ArrayList<>();

    @FXML
    private MenuBar menu;

    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private VBox vContent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

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
    }

    @FXML
    private void onSafeClicked(ActionEvent actionEvent) {
    }

    @FXML
    private void onCloseClicked(ActionEvent actionEvent) {
        engine.closeProgram(true);
    }

    @FXML
    private void onSafeAsClicked(ActionEvent actionEvent) {
        String path;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(engine.getFileUtils().getHome()));
        fileChooser.setInitialFileName("modul");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        try {
            path = fileChooser.showSaveDialog(primaryStage).getAbsolutePath();
        } catch (Exception e) {
            new AllertBox(null, Modality.APPLICATION_MODAL, engine).displayMessage("Error", "Please select a valid path!", "ok", "buttonBlue", false);
            return;
        }
    }

    private HBox putIntoHbox(Node ... a){
        HBox hBox = new HBox();
        hBox.getChildren().addAll(a);
        hBox.setSpacing(10);
        return hBox;
    }

    private VBox putIntoVbox(Node ... a){
        VBox vBox = new VBox();
        vBox.getChildren().addAll(a);
        vBox.setSpacing(25);
        return vBox;
    }

    public void saveContent(String path){

    }

    private JSONObject convertModulToJson(Module module){
        JSONObject jsonModul = new JSONObject();
        jsonModul.put("name", module.getModulNameTxt());
        JSONArray devices = new JSONArray();

        for (Device device:module.getDeviceArrayList()) {
            devices.add(convertDeviceToJson(device));
        }

        jsonModul.put("devices", devices);
        return jsonModul;
    }

    private JSONObject convertDeviceToJson(Device device){
        JSONObject jsonDevice = new JSONObject();
        jsonDevice.put("name",device.getDeviceNameTxt());
        jsonDevice.put("path",device.getDevicePathTxt());
        JSONArray features = new JSONArray();

        for(Feature feature:device.getFeaturesArray()){
            features.add(convertFeatureToJson(feature));
        }

        jsonDevice.put("features", features);
        return jsonDevice;
    }

    private JSONObject convertFeatureToJson(Feature feature){
        JSONObject jsonFeature = new JSONObject();
        jsonFeature.put("name", feature.getFeatureNameTxt());

        for(VariableFeatureLine variableFeatureLine: feature.getVariableFeatureLines()){
            jsonFeature.put(variableFeatureLine.getTextFieldName(), variableFeatureLine.getTextField());
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

        public void removeDevice(Device device){
            deviceArrayList.remove(device);
            devicesBox.getChildren().remove(device.getDeviceBox());
        }

        public VBox getModulBox() {
            return modulBox;
        }

        public ArrayList<Device> getDeviceArrayList() {
            return deviceArrayList;
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
            HBox featuresHBox = putIntoHbox(features,featureScroll,addFeature);

            remove.setId("buttonRed");
            remove.setOnMouseClicked(event1 -> {
                module.removeDevice(this);
            });

            deviceBox = putIntoVbox(deviceNameBox,devicePathBox,featuresHBox,remove);
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

        public void removeFeature(Feature feature){
            featuresBox.getChildren().remove(feature.getFeatureBox());
            featuresArray.remove(feature);
        }

        public ArrayList<Feature> getFeaturesArray() {
            return featuresArray;
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

        public Feature(Device device) {
            this.device = device;
            featureTypeChoiceBox.getItems().add("new feature");
            featureTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                variableFeatureContent.getChildren().clear();
                if(newValue.equals(featureTypeChoiceBox.getItems().get(0))){
                    Button addField = new Button("Add");
                    addField.setId("buttonGreen");
                    variableFeatureContent.getChildren().add(addField);

                    addField.setOnMouseClicked(event -> {
                        VariableFeatureLine variableFeatureLine = new VariableFeatureLine(this);
                        variableFeatureLines.add(variableFeatureLine);
                        variableFeatureContent.getChildren().add(variableFeatureLine.getVariableFeatureLineBox());
                    });
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

        public ArrayList<VariableFeatureLine> getVariableFeatureLines() {
            return variableFeatureLines;
        }
    }

    public class VariableFeatureLine {
        Label label;
        TextField textField;

        Feature feature;
        VBox variableFeatureLineBox = new VBox();

        public VariableFeatureLine(Feature feature) {
            this.feature = feature;
            String fieldName = new AllertBox(null, Modality.APPLICATION_MODAL, engine).displayTextField("Name of field", "Add field", "ok", false);
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

        public String getTextFieldName(){
            return label.getText();
        }
    }
}
