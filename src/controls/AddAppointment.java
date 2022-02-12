/*
C195 - Started March 2020
Brandon Blackburn
Last updated: 09/25/2020

Requirement Criteria in this class:


(Shared with AppointmentInfo class)
F.   Write exception controls to prevent each of the following.
You may use the same mechanism of exception control more than once, but you must incorporate at least two different mechanisms of exception control.

    •   scheduling an appointment outside business hours
        - Prevented in saveOA method. Mechanism: Try-Catch (BusinessHoursException)

    •   scheduling overlapping appointments
        - Prevented using checkBetween method. Mechanism: Method Throws (ConflictingScheduleException)

    •   entering nonexistent or invalid customer data
        - Prevented in saveOA method. Cannot leave pre-populated customer combo box empty. Mechanism: Try-Catch (InvalidEntry)
        - Customer information also cannot be left blank/invalid in the AddCustomer/CustomerInfo classes. Mechanism: Try-Catch (InvalidEntry)
        - The Checker utils class is used to check the validity of customer information. No exceptions, just If statements.

    •   entering an incorrect username and password

 */



package controls;

import Exceptions.BusinessHoursException;
import Exceptions.ConflictingScheduleException;
import Exceptions.InvalidEntry;
import Exceptions.InvalidTimesException;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import utils.Checker;
import utils.Query;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;


public class AddAppointment {
    /* #######################################################
                     Class Variables & FXML
     ####################################################### */
    Stage stage;
    Parent scene;
    ObservableList<Customer> customerList = FXCollections.observableArrayList();
    String username;
    final int open = 9;
    final int close = 17;

    StringConverter<Customer> stringConverter = new StringConverter<Customer>() {
        @Override public String toString(Customer customer) {
            return customer.getName();
        }
        @Override public Customer fromString(String s) {
            return null;
        }
    };

    @FXML private TextField title;
    @FXML private TextField type;
    @FXML private TextField located;
    @FXML private TextField start;
    @FXML private TextField end;
    @FXML private TextField customer;
    @FXML private TextField contact;
    @FXML private TextField url;
    @FXML private TextArea description;
    @FXML private Button save;
    @FXML private Button customerAdd;
    @FXML private ComboBox<Customer> customerComboBox = new ComboBox<>(customerList);                                   //holy cow this took me way longer than it should have to figure out


    /* #######################################################
                             Actions
    ####################################################### */
    @FXML void customerAddOA(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML void cancelOA(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML void saveOA(ActionEvent event) throws IOException, SQLException {
        Timestamp startTS = Timestamp.valueOf(start.getText());
        LocalDateTime startDT = startTS.toLocalDateTime();
        Timestamp endTS = Timestamp.valueOf(end.getText());
        LocalDateTime endDT = endTS.toLocalDateTime();

        try {
            if (startDT.getHour() < open || startDT.getHour() > close)
                throw new BusinessHoursException("Start time is out of business hours. : " + open + " - " + close);     //Checks business start

            if (endDT.getHour() < open || endDT.getHour() > close)
                throw new BusinessHoursException("End time is out of business hours. : " + open + " - " + close);       //Checks business end

            if (startDT.isAfter(endDT))
                throw new InvalidTimesException("Invalid start and end times.");                                        //Checks if start and end are mismatched

            if (customerComboBox.getSelectionModel().isEmpty())
                throw new InvalidEntry("You must choose a customer.");

            if (checkBetweenTimes(startTS, endTS)) {                                                                    //Checks if times conflict with other appointments
                if (Checker.checkAppointment(title.getText(), url.getText())) {                                         //Checks if database will accept title and url

                    Customer selected = customerComboBox.getValue();
                    Query.insertAppointment(
                            selected.getCustomerId(),
                            title.getText(),
                            description.getText(),
                            located.getText(),
                            contact.getText(),
                            type.getText(),
                            url.getText(),
                            startTS,
                            endTS,
                            username
                    );
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            }

        } catch (BusinessHoursException | InvalidTimesException | ConflictingScheduleException | InvalidEntry e) {      //Catch will prevent saving and prompt user to try again.
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.showAndWait();
        }

    }




    /* #######################################################
                    Populate & Utils
    ####################################################### */
    public boolean checkBetweenTimes(Timestamp start, Timestamp end) throws SQLException, ConflictingScheduleException {
        /*
        There are three ways that schedules can overlap.
        1. Another start time is in between this start and end
        2. Another end time is in between this start and end
        3. Another start AND end over lap the entire appointment being made.
         */

        ResultSet startTimes = Query.select("appointment", "start, end", "(start BETWEEN '" + start + "' AND '" + end + "' )").getResultSet();
        ResultSet endTimes = Query.select("appointment", "start, end", "(start BETWEEN '" + start + "' AND '" + end + "' )").getResultSet();
        ResultSet middle = Query.select("appointment", "start, end", "(start <= '" + start + "' AND '" + end + "' >= end)").getResultSet();

        if (startTimes.next())      //1.
            throw new ConflictingScheduleException("Times are conflicting with an appointment: " + startTimes.getTimestamp("start") + " - " + startTimes.getTimestamp("end"));

        else if (endTimes.next())   //2.
            throw new ConflictingScheduleException("Times are conflicting with an appointment: " + endTimes.getTimestamp("start") + " - " + endTimes.getTimestamp("end"));

        else if (middle.next())     //3.
            throw new ConflictingScheduleException("Times are conflicting with an appointment: " + middle.getTimestamp("start") + " - " + middle.getTimestamp("end"));

        return true;
    }


    public void sendCustomers(ObservableList <Customer> customers, String user) throws SQLException {               //sets the customer combo box from the main menu.
        customerList.setAll(customers);
        customerComboBox.setConverter(stringConverter);
        customerComboBox.setItems(customers);
        username = user;
    }
}
