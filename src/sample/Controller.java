package sample;

import app.dao.DaoFactory;
import app.dao.ModelDao;
import app.model.Place;
import app.model.Trip;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import java.net.URL;
import javafx.util.Duration;
import java.util.ResourceBundle;
import java.util.Scanner;

import static javafx.collections.FXCollections.*;

public class Controller implements Initializable {
    @FXML
    private ListView<Trip> tripsListview;
    @FXML
    private ListView<Place> placesListView;
    @FXML
    private TextField TripNameField;
    @FXML
    private ComboBox<Place> TripDepartureBox;
    @FXML
    private ComboBox<Place> TripTerminalBox;
    @FXML
    private TextField TripCostBox;
    @FXML
    private Button TripCreateButton;
    @FXML
    private TextField PlaceNameField;
    @FXML
    private Button PlaceCreateButton;
    @FXML
    private Button PlaceEditButton;
    @FXML
    private TextField departureTextField;
    @FXML
    private TextField TerminalTextField;
    @FXML
    private TabPane beanTabPane;
    @FXML
    private Tab tripTab;
    @FXML
    private Button removeButton;



    private static final ModelDao<Long,Place> jpd = DaoFactory.getPlaceDao();
    private static final ModelDao<Long,Trip> jtd = DaoFactory.getTripDao();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshLists();
        refreshBoxes();
    }

    @FXML
    private void selectTrip() {
        updateTrip(tripsListview.getSelectionModel().getSelectedItem());
        removeButton.setDisable(false);
    }

    private void updateTrip(Trip t) {
        TripCreateButton.setDisable(true);
        TripDepartureBox.setVisible(false);
        TripTerminalBox.setVisible(false);
        TripCostBox.setText(String.valueOf(t.getCost()));
        TerminalTextField.setText(jpd.findById(t.getTerminus()).toString());
        departureTextField.setText(jpd.findById(t.getDeparture()).toString());
        TripNameField.setText(t.getName());

    }

    @FXML
    private void selectPlace() {
        updatePlace(placesListView.getSelectionModel().getSelectedItem());
        removeButton.setDisable(false);
    }

    private void updatePlace(Place p) {
        PlaceCreateButton.setDisable(true);
        PlaceEditButton.setDisable(false);
        PlaceNameField.setText(p.getName());
    }

    @FXML
    private void addUnlock() {
        Tab selectedItem = beanTabPane.getSelectionModel().getSelectedItem();
        if (tripTab.equals(selectedItem)) {
            initTrip();
        } else  {
            initPlace();
        }
    }

    @FXML
    private void removeBean() {
        Tab selectedItem = beanTabPane.getSelectionModel().getSelectedItem();
        if (tripTab.equals(selectedItem)) {
            if(jtd.remove(tripsListview.getSelectionModel().getSelectedItem())){
                initTrip();
            } else {
                showSQLError();
            }
        } else  {
            if(jpd.remove(placesListView.getSelectionModel().getSelectedItem())){
                refreshBoxes();
                initPlace();
            } else {
                showSQLError();
            }
        }
        refreshLists();
    }

    @FXML
    private void createPlace() {
        if (controlField(PlaceNameField)) {
            if(jpd.create(new Place(null,PlaceNameField.getText())) != -1){
                initPlace();
                refreshPlaces();
            } else {
                showSQLError();
            }
        }
        initPlace();
        refreshPlaces();
        refreshBoxes();
    }

    @FXML
    private void editPlace() {
        if (controlField(PlaceNameField)) {
            Place p = placesListView.getSelectionModel().getSelectedItem();
            p.setName(PlaceNameField.getText());
            if(jpd.update(p)){
                initPlace();
                refreshPlaces();
            } else {
                showSQLError();
            }
        }
        refreshBoxes();
    }

    @FXML
    private void createTrip() {
        boolean ok = (controlField(TripNameField) && controlBox(TripDepartureBox) && controlBox(TripTerminalBox) &&  controlFloat());
        if (ok){
            if(jtd.create(new Trip(null,TripNameField.getText(),
                    TripDepartureBox.getSelectionModel().getSelectedItem().getId(),
                    TripTerminalBox.getSelectionModel().getSelectedItem().getId(),
                    new Scanner(TripCostBox.getText()).nextFloat())) != 1){
                initTrip();
                refreshTrips();
            } else {
                showSQLError();
            }
        }
    }

    private boolean controlFloat(){
        Scanner s = new Scanner(TripCostBox.getText());
        if(s.hasNextFloat())return true;
        else  {
            errorOnField(TripCostBox);
            return false;
        }
    }

    private void initTrip(){
        TripCreateButton.setDisable(false);
        TripDepartureBox.setVisible(true);
        TripTerminalBox.setVisible(true);
        refreshBoxes();
        TripCostBox.setText("");
        TerminalTextField.setText("");
        departureTextField.setText("");
        TripNameField.setText("");
    }

    private void initPlace(){
        PlaceCreateButton.setDisable(false);
        PlaceEditButton.setDisable(true);
        PlaceNameField.setText("");
    }

    private boolean controlField(TextField t){
        if(!t.getText().equals(""))return true;
        else  {
            errorOnField(t);
            return false;
        }
    }

    private boolean controlBox(ComboBox<Place> b){
        if(b.getSelectionModel().getSelectedItem() != null)return true;
        else  {
            errorOnField(b);
            return false;
        }
    }

    private void errorOnField(Control c){
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(200), ev -> errorBorder(c)));
        tl.setCycleCount(6);
        tl.play();
    }

    private void errorBorder(Control c){
        if(c.getBorder() == null) c.setBorder(new Border(new BorderStroke(Paint.valueOf("RED"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        else c.setBorder(null);
    }

    private void showSQLError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("400 Error : Bad Request");
        alert.show();
    }

    private void refreshLists(){
        refreshTrips();
        refreshPlaces();
    }

    private void refreshTrips(){
        tripsListview.setItems(observableArrayList(jtd.findAll()));
        removeButton.setDisable(true);
    }

    private void refreshPlaces(){
        placesListView.setItems(observableArrayList(jpd.findAll()));
        PlaceEditButton.setDisable(true);
        removeButton.setDisable(true);
    }

    private void refreshBoxes(){
        TripDepartureBox.setItems(observableArrayList(jpd.findAll()));
        TripTerminalBox.setItems(observableArrayList(jpd.findAll()));
    }
}
