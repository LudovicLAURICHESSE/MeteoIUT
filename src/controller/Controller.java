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
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.stage.Stage;
import metier.generation.IntervalGeneration;
import metier.sensor.ISensor;
import metier.sensor.SimpleSensor;
import metier.thread.SensorThread;


public class Controller implements Initializable{
    static final String baseName="Sensor N°";
    static final String TITLE="Station Météo";

    ObservableList<ISensor> sensors= FXCollections.observableArrayList();
    private ListProperty<ISensor> lSensors=new SimpleListProperty<>(sensors);

    @FXML    private ListView<ISensor> listSensors=new ListView<>();

    private ISensor selectedSensor;

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
                    textProperty().setValue("");
                }
            }
        });

        listSensors.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<ISensor>() {
                    public void changed(ObservableValue<? extends ISensor> observable,
                                        ISensor oldValue, ISensor newValue) {
                        if(newValue!=null)
                            selectedSensor=newValue;
                    }
                });
    }



    @FXML
    public void changeGeneration(){


    }

    @FXML
    public void newSensor(){
        String name=baseName+((List)sensors).size();
        ISensor isensor=new SimpleSensor(name,new IntervalGeneration());
        sensors.add(isensor);
        SensorThread initializedThread = new SensorThread(isensor,1);
        initializedThread.start();
    }

    @FXML
    public void digitalView() throws Exception{
        ControllerDigital ctrlDigital;
        Stage stage=new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/digital_view.fxml"));
        stage.setScene(new Scene(loader.load()));
        ctrlDigital=loader.getController();
        ctrlDigital.setSensor(selectedSensor);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setTitle(TITLE);
        stage.show();
    }
    @FXML
    public void iconeView()throws Exception{
        ControllerIcone ctrlIcone;
        Stage stage=new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/icone_view.fxml"));
        stage.setScene(new Scene(loader.load()));
        ctrlIcone=loader.getController();
        ctrlIcone.setSensor(selectedSensor);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setTitle(TITLE);
        stage.show();
    }
    @FXML
    public void thermometerView()throws Exception{
        ControllerThermometer ctrlThermometer;
        Stage stage=new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/thermometer_view.fxml"));
        stage.setScene(new Scene(loader.load()));
        ctrlThermometer=loader.getController();
        ctrlThermometer.setSensor(selectedSensor);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setTitle(TITLE);
        stage.show();
    }

}
