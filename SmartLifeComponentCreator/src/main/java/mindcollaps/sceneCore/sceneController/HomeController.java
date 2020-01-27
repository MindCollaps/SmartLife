package mindcollaps.sceneCore.sceneController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mindcollaps.engines.Engine;
import mindcollaps.sceneCore.MoveListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeController extends Controller {

    ArrayListy<>

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


        addDevice.setOnMouseClicked(event -> {
            Label deviceName = new Label("Name: ");
            TextField deviceNameTxt = new TextField();
            HBox deviceNameBox = putIntoHbox(deviceName, deviceNameTxt);

            Label devicePath = new Label("Path: ");
            TextField devicePathTxt = new TextField();
            HBox devicePathBox = putIntoHbox(devicePath, devicePathTxt);

            Label features = new Label("Features");
            VBox featuresBox = new VBox();
            featuresBox.setSpacing(25);
            ScrollPane featureScroll = new ScrollPane();
            featureScroll.setContent(featuresBox);
            Button addFeature = new Button("Add feature");
            addFeature.setId("buttonGreen");

            addFeature.setOnMouseClicked(event1 -> {
                //TODO:lode from json
            });
            HBox featuresHBox = putIntoHbox(features,featureScroll,addFeature);

            Button remove = new Button("remove");
            remove.setId("buttonRed");
            VBox newDevice = putIntoVbox(deviceNameBox,devicePathBox,featuresHBox,remove);

            devicesBox.getChildren().add(newDevice);

            remove.setOnMouseClicked(event1 -> {
                devicesBox.getChildren().remove(newDevice);
            });
        });

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

    private class Module {
        Label modulName = new Label("Module name");
        TextField modulNameTxt = new TextField();
        Label devices = new Label("devices");
        VBox devicesBox = new VBox();
        Button addDevice = new Button("Add device");

        ArrayList<Device> deviceArrayList = new ArrayList<>();

        public Module() {
            HBox modulNameBox = putIntoHbox(modulName, modulNameTxt);
            devicesBox.setSpacing(25);
            ScrollPane deviceScroll = new ScrollPane();
            deviceScroll.setContent(devicesBox);
            addDevice.setId("buttonrGreen");
            addDevice.setOnMouseClicked(event -> {

            });

            HBox devicesHbox = putIntoHbox(devices, deviceScroll, addDevice);
            vContent.getChildren().addAll(modulNameBox, devicesHbox);
        }
    }

    private class Device {
        Label deviceName = new Label("Name: ");
        TextField deviceNameTxt = new TextField();
        Label devicePath = new Label("Path: ");
        TextField devicePathTxt = new TextField();
        Label features = new Label("Features");
        VBox featuresBox = new VBox();
        Button addFeature = new Button("Add feature");
        Button remove = new Button("remove");

        Module module;

        public Device(Module module) {
            this.module = module;
            HBox deviceNameBox = putIntoHbox(deviceName, deviceNameTxt);
            HBox devicePathBox = putIntoHbox(devicePath, devicePathTxt);
            featuresBox.setSpacing(25);
            ScrollPane featureScroll = new ScrollPane();
            featureScroll.setContent(featuresBox);
            addFeature.setId("buttonGreen");

            addFeature.setOnMouseClicked(event1 -> {
                //TODO:lode from json
            });
            HBox featuresHBox = putIntoHbox(features,featureScroll,addFeature);
            remove.setId("buttonRed");
            VBox newDevice = putIntoVbox(deviceNameBox,devicePathBox,featuresHBox,remove);

            remove.setOnMouseClicked(event1 -> {
            });
        }
    }
}
