package controller;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
<<<<<<< HEAD
import javafx.scene.Parent;
import javafx.scene.Scene;
=======
import javafx.geometry.Pos;
import javafx.scene.Node;
>>>>>>> 7dbe942c9458921e2c52bf5d72739f42aa218ed1
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import metier.*;



public class Controller implements Initializable{
    static final String baseName="Sensor N°";

    ObservableList<ISensor> sensors= FXCollections.observableArrayList();
    private ListProperty<ISensor> lSensors=new SimpleListProperty<>(sensors);

    @FXML    private ListView<ISensor> listSensors=new ListView<>();
    @FXML    Tab tabDigits;
    @FXML    HBox contentDigital;
    @FXML    Label lbDigital;
    @FXML    Button updateButton;
    @FXML    Tab tabThermometer;
    @FXML    ProgressBar thermometer;
    @FXML    Tab tabIcone;
    @FXML    VBox contentImg;
    @FXML    Label lbIndicator;
    @FXML    ImageView imgThermo = new ImageView();



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listSensors.itemsProperty().bind(lSensors);

        listSensors.setCellFactory(unused -> new ListCell<ISensor>(){
            @Override
            protected void updateItem(ISensor item, boolean empty) {
                super.updateItem(item, empty);
                if(!isEmpty()){
                    textProperty().bind(item.nameProperty());
                }else{
                    textProperty().unbind();
                    textProperty().setValue(null);
                }
            }
        });

        listSensors.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<ISensor>() {
                    public void changed(ObservableValue<? extends ISensor> observable,
                                        ISensor oldValue, ISensor newValue) {
                        if(oldValue!=null)
                            unbindDetail(oldValue);
                        if(newValue!=null)
                            bindDetail(newValue);
                    }
                });

        viewTab(contentDigital,tabDigits);
        viewTab(thermometer,tabThermometer);
        viewTab(contentImg,tabIcone);
    }

    private void viewTab(Node children, Tab tab){

        final VBox display=new VBox();
        display.getChildren().add(children);
        display.setAlignment(Pos.CENTER);
        tab.setContent(display);
    }


    @FXML
    public void changeGeneration(){

    }

    @FXML
    public void newSensor() throws Exception{
        Stage stage=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/vues/sensor_creation.fxml"));
        Scene scene=new Scene(root);
        stage.setTitle("Création d'un capteur");
        stage.setScene(scene);
        stage.show();


        String name=baseName+((List)sensors).size();
        ISensor c=new SimpleSensor(name, 10);
        sensors.add(c);
        SensorThread initializedThread = new SensorThread(c);
        initializedThread.start();
    }

    public void unbindDetail(ISensor oldValue){
        lbDigital.textProperty().unbind();
        thermometer.progressProperty().unbind();
        imgThermo.imageProperty().unbind();
        lbIndicator.textProperty().unbind();
    }

    public void bindDetail(ISensor newValue){
        lbDigital.textProperty().bind(newValue.temperatureProperty().asString());
        thermometer.progressProperty().bind(newValue.progressTemperatureProperty());
        imgThermo.imageProperty().bind(newValue.imageProperty());
        lbIndicator.textProperty().bind(newValue.temperatureProperty().asString());
    }

    @FXML
    public void updateSensor(){
        listSensors.getSelectionModel().getSelectedItem().update();
    }
}
