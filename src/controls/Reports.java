/*
C195 - Started March 2020
Brandon Blackburn
Last updated: 09/25/2020

    *Note*
    Consultant was not given a definition in our requirements, but in the FAQs for the assignment it suggested
    that we use "createdBy" for each appointment as the consultant, and then specify in a comment that we did.
    So here is that.


Requirement Criteria in this class:

G.  Write two or more lambda expressions to make your program more efficient, justifying the use of each lambda expression with an in-line comment.
    - In "populateConsultantTable" - goes through all of the appointments and adds those who have the selected consultant as their createdBy value.
    - In "populateCustomerTable" - gores through all of the appointments and adds those who have the same customerId as the selected customer.


I.   Provide the ability to generate each  of the following reports:
    •   number of appointment types by month
        - Provided in first tab "Types" Displays how many of each type there are in the month, and the total different amount of types. Just in case.

    •   the schedule for each consultant
        - Consultant = createdBy for each appointment. In "Consultant" tab, select a consultant via combo box. All appointments in the next month are displayed.

    •   one additional report of your choice
        - I chose to make a tab to display the amount of customers named "Taylor Swift"
            If the amount is 0, Taylor Swift is sad and so is the emoji.
            If the amount is > 0, Taylor Swift is happy. The emoji turns that frown upside down. For each customer named Taylor swift - the smiley face gets bigger.

    •   one additional report of your choice
        - If the previous report of my choice was an unsuitable one (or the grader does not appreciate the humor) I have prepared a different report of my choice.
        - Appointments are displayed based on customer. Just like the consultant schedule, but with each customer instead.

 */






package controls;
import Model.Appointment;
import Model.Customer;
import Model.Type;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.shape.QuadCurve;
import javafx.stage.Stage;
import utils.Query;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Reports implements Initializable {
    /* #######################################################
                     Class Variables & FXML
    ####################################################### */
    Parent scene;
    Stage stage;
    private ObservableList<Type> types = FXCollections.observableArrayList();                                           //Types and amount of each
    private ObservableList<String> consultants = FXCollections.observableArrayList();                                   //Names of consultants, for consultant combo box
    private ObservableList<Appointment> selectedConsultantAppointments = FXCollections.observableArrayList();           //List of appointments for selected consultant
    private ObservableList<Appointment> selectedCustomerAppointments = FXCollections.observableArrayList();             //List of appointments for selected customer
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();                             //List of all appointments
    private ObservableList<Customer> customers = FXCollections.observableArrayList();                                   //List of all customers

    @FXML private ImageView swiftPic;
    @FXML private TableView<Type> typeTableView;
    @FXML private TableColumn<Type, String> typeColumn;
    @FXML private TableColumn<Type, ?> typeNumColumn;
    @FXML private Label numDifferentTypes;
    @FXML private ComboBox<String> consultantComboBox = new ComboBox<>(consultants);
    @FXML private TableView<Appointment> consultantTableView;
    @FXML private TableColumn<?, ?> dateConsultant;
    @FXML private TableColumn<?, ?> startConsultant;
    @FXML private TableColumn<?, ?> endConsultant;
    @FXML private TableColumn<?, ?> titleConsultant;
    @FXML private TableColumn<?, ?> customerConsultant;
    @FXML private ComboBox<Customer> customerComboBox = new ComboBox<>(customers);
    @FXML private TableView<Appointment> customerTableView;
    @FXML private TableColumn<?, ?> dateCustomer;
    @FXML private TableColumn<?, ?> startCustomer;
    @FXML private TableColumn<?, ?> endCustomer;
    @FXML private TableColumn<?, ?> titleCustomer;
    @FXML private Label tSwizzleCounter;
    @FXML private QuadCurve emotion;


    public void initialize(URL url, ResourceBundle rb){
        try {
            populateTypesTable();
            populateConsultantComboBox();
            populateConsultantTable();
            populateCustomerComboBox();
            populateCustomerTable();
            tSwiftinator();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /* #######################################################
                             Actions
    ####################################################### */
    @FXML void consultantComboBoxOA(ActionEvent event) {
        populateConsultantTable();
    }
    @FXML void customerComboBoxOA(ActionEvent event) {
        populateCustomerTable();
    }

    @FXML void exitOA(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void sendInfo(ObservableList<Appointment> appointmentsIn, ObservableList<Customer> customersIn){
        appointments.setAll(appointmentsIn);
        customers.setAll(customersIn);
    }



    /* #######################################################
                           Populate
    ####################################################### */

                                    //Types
    public void populateTypesTable() throws SQLException {
        types.clear();
        int x = 0;
        ResultSet rs = Query.groupBy("appointment", "type, COUNT(*)", "type ORDER BY COUNT(*) DESC");

        while (rs.next()){
            Type type = new Type(rs.getString("type"), rs.getInt("COUNT(*)"));
            types.add(type);
            x++;
        }
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeNumColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeTableView.setItems(types);

        numDifferentTypes.setText(String.valueOf(x));
    }


                                //Consultant
    public void populateConsultantComboBox() throws SQLException {
        consultants.clear();
        ResultSet rs = Query.groupBy("appointment", "createdBy", "createdBy");
        while (rs.next())
            consultants.add(rs.getString("createdBy"));
        consultantComboBox.setItems(consultants);
    }
    public void populateConsultantTable() {
        selectedConsultantAppointments.clear();
        String selectedConsultant = consultantComboBox.getSelectionModel().getSelectedItem();
        if (!consultantComboBox.getSelectionModel().isEmpty()) {

            //From my list of appointments passed in, find each creator and if it matches the selected Consultant, it adds it to the selected appointments to be displayed.
            appointments.forEach(appointment -> { if (appointment.getCreatedBy().equalsIgnoreCase(selectedConsultant)) selectedConsultantAppointments.add(appointment); });

            dateConsultant.setCellValueFactory(new PropertyValueFactory<>("date"));
            startConsultant.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            endConsultant.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            titleConsultant.setCellValueFactory(new PropertyValueFactory<>("title"));
            customerConsultant.setCellValueFactory(new PropertyValueFactory<>("customer"));
            consultantTableView.setItems(selectedConsultantAppointments);
        }
    }

                                //Customer
    public void populateCustomerComboBox() {
        customerComboBox.setItems(customers);
    }
    public void populateCustomerTable() {
        selectedCustomerAppointments.clear();
        Customer selectedCustomer = customerComboBox.getSelectionModel().getSelectedItem();
        if (!customerComboBox.getSelectionModel().isEmpty()) {

            //From my list of appointments passed in, find each customerId and if it matches the selected Customer's Id, it adds it to the selected appointments to be displayed.
            appointments.forEach(appointment -> { if (selectedCustomer.getCustomerId() == appointment.getCustomerId()) selectedCustomerAppointments.add(appointment); });

            dateCustomer.setCellValueFactory(new PropertyValueFactory<>("date"));
            startCustomer.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            endCustomer.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            titleCustomer.setCellValueFactory(new PropertyValueFactory<>("title"));
            customerTableView.setItems(selectedCustomerAppointments);
        }
    }




                                //Taylor Swift
    public void tSwiftinator () throws SQLException {
        ResultSet rs = Query.select("customer", "COUNT(*)", "customerName = 'Taylor Swift'").getResultSet();
        int swifty = 0;
        if (rs.next())
            swifty = rs.getInt("COUNT(*)");

        if (swifty > 0) {
            tSwizzleCounter.setText(String.valueOf(swifty));
            emotion.setControlY((double) swifty * 26);
            swiftPic.setOpacity(0);
        }
    }


}
