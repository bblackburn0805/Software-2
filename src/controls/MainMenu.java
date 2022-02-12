
/*
C195 - Started March 2020
Brandon Blackburn
Last updated: 09/25/2020

Requirement Criteria in this class:

B.   Provide the ability to add, update, and delete customer records in the database, including name, address, and phone number.
    - Add customers with AddCustomer class via AddCustomer button. If address, city, or country are new, creates a new entry for each new case.
    - Update customers with CustomerInfo class via Edit button on Customers tab. Will update their address/phone/city/country in reference to addressId.
    - Delete customers via Delete button on Customers tab. Confirmation required.

C.   Provide the ability to add, update, and delete appointments, capturing the type of appointment and a link to the specific customer record in the database.
    - Add an appointment with AddAppointment class via Add Appointment button. All fields required (as database values cannot be null.) Customers can be selected or added in Add Appointment menu.
    - Update an appointment with AppointmentInfo class via Edit button for each appointment TableView.
    - Delete an appointment via Delete button for each appointment TableView.

D.   Provide the ability to view the calendar by month and by week.
    - Calender can be displayed in TableViews by Month and Week tabs.


G.  Write two or more lambda expressions to make your program more efficient, justifying the use of each lambda expression with an in-line comment.
    - Used in "setLabels" method. It takes the customerId from the Appointment, then searches all customers for a matching customerId.

K. Demonstrate professional communication in the content and presentation of your submission.
    - Displayed in all classes.


   OOPSIES (that turned into extra features):
   *I made a Today appointment's tab that was intended to be used to select week or month, but ended up as today. Week and Month appointments are still available.
   *You can view more info of a selected appointment by clicking the View button. It displays all info of an appointment without opening a new scene.
   *Information displayed via View button, the customer also has a hyperlink that leads into that customers info. This was done before implementing a Customer TableView.


 */

package controls;

import Model.Appointment;
import Model.Customer;
import Model.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.ConnectDB;
import utils.Query;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;


public class MainMenu implements Initializable {
    /* #######################################################
                      Class Variables & FXML
      ####################################################### */
    Stage stage;
    Parent scene;
    private static LocalDate rightNow;
    protected  ObservableList<Customer> customers = FXCollections.observableArrayList();
    protected  ObservableList<Appointment> todayAppointments = FXCollections.observableArrayList();
    private  ObservableList<Appointment> weekAppointments = FXCollections.observableArrayList();
    private  ObservableList<Appointment> monthAppointments = FXCollections.observableArrayList();
    private Alert delete = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to delete this entry?");
    private Customer selectedCustomer;
    private String username;


    @FXML private TableView<Appointment> todayTableView;
    @FXML private TableColumn<Appointment, String> dateToday;
    @FXML private TableColumn<Appointment, String> startToday;
    @FXML private TableColumn<Appointment, String> endToday;
    @FXML private TableColumn<Appointment, String> titleToday;
    @FXML private TableColumn<Appointment, String> customerToday;
    @FXML private Label numApptToday;

    @FXML private TableView<Appointment> weekTableView;
    @FXML private TableColumn<Appointment, String> dateWeek;
    @FXML private TableColumn<Appointment, String> startWeek;
    @FXML private TableColumn<Appointment, String> endWeek;
    @FXML private TableColumn<Appointment, String> titleWeek;
    @FXML private TableColumn<Appointment, String> customerWeek;
    @FXML private Label numApptWeek;

    @FXML private TableView<Appointment> monthTableView;
    @FXML private TableColumn<Appointment, String> dateMonth;
    @FXML private TableColumn<Appointment, String> startMonth;
    @FXML private TableColumn<Appointment, String> endMonth;
    @FXML private TableColumn<Appointment, String> titleMonth;
    @FXML private TableColumn<Appointment, String> customerMonth;
    @FXML private Label numApptMonth;

    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, String> customerName;
    @FXML private TableColumn<Customer, String> customerCity;
    @FXML private TableColumn<Customer, String> customerCountry;
    @FXML private TableColumn<Customer, String> customerPhone;

    @FXML private Label title;
    @FXML private Label type;
    @FXML private Label located;
    @FXML private Label start;
    @FXML private Label end;
    @FXML private Hyperlink customer;
    @FXML private Label contact;
    @FXML private Label createDate;
    @FXML private Label createdBy;
    @FXML private Label update;
    @FXML private Label updateBy;
    @FXML private Label url;
    @FXML private Label description;
    @FXML private Label date;

    public void initialize(URL url, ResourceBundle rb){
        rightNow = LocalDateTime.now().toLocalDate();
        try {
            populateTodayTable();
            populateWeekTable();
            populateMonthTable();
            populateCustomers();
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        username = Main.getUsername();
    }





    /* #######################################################
                           Menu Loading
     ####################################################### */
    @FXML
    void exitOA(ActionEvent event) throws SQLException {
        ConnectDB.closeConnection();
        System.exit(0);
    }

    @FXML
    void newAppointmentOA(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AddAppointment.fxml"));
        loader.load();

        AddAppointment info = loader.getController();
        info.sendCustomers(customers, username);

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void newCustomerOA(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AddCustomer.fxml"));
        loader.load();

        AddCustomer info = loader.getController();
        info.sendUser(username);

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }


    @FXML
    void signOutOA(ActionEvent event)throws IOException
    {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void reportsOA(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/Reports.fxml"));
        loader.load();

        Reports info = loader.getController();
        info.sendInfo(monthAppointments, customers);

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void viewCustomerOA(ActionEvent event) throws IOException {
        if (!customerTableView.getSelectionModel().isEmpty()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/CustomerInfo.fxml"));
            loader.load();

            CustomerInfo info = loader.getController();
            info.sendCustomer(customerTableView.getSelectionModel().getSelectedItem(), username);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    @FXML
    void customerHyperlink(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/CustomerInfo.fxml"));
        loader.load();

        CustomerInfo info = loader.getController();
        info.sendCustomer(selectedCustomer, username);

        stage = (Stage) ((Hyperlink) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }





    /* #######################################################
                          Table Buttons
     ####################################################### */
                            //Customer
    @FXML
    void deleteCustomerOA(ActionEvent event) throws SQLException {
        if (delete.showAndWait().get() == ButtonType.OK && !customerTableView.getSelectionModel().isEmpty())
            Query.deleteCustomer(customerTableView.getSelectionModel().getSelectedItem().getCustomerId());
        refreshCustomers();
    }


                            //Today
    @FXML
    void viewTodayOA(ActionEvent event) {
        if (!todayTableView.getSelectionModel().isEmpty()) {
            setLabels(todayTableView.getSelectionModel().getSelectedItem());
        }
    }
    @FXML
    void editTodayOA(ActionEvent event) throws IOException {
        if (!todayTableView.getSelectionModel().isEmpty()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/AppointmentInfo.fxml"));
            loader.load();

            AppointmentInfo info = loader.getController();
            info.sendAppointment(todayTableView.getSelectionModel().getSelectedItem(), customers, username);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    @FXML
    void deleteTodayOA(ActionEvent event) throws SQLException {
        if (delete.showAndWait().get() == ButtonType.OK && !todayTableView.getSelectionModel().isEmpty())
            Query.deleteAppointment(todayTableView.getSelectionModel().getSelectedItem().getAppointmentId());
        numApptToday.setText(String.valueOf(refreshSchedule(rightNow.toString(), todayAppointments)));
        numApptWeek.setText(String.valueOf(refreshSchedule(rightNow.plus(1, ChronoUnit.WEEKS).toString(), weekAppointments)));
        numApptMonth.setText(String.valueOf(refreshSchedule(rightNow.plus(1, ChronoUnit.MONTHS).toString(), monthAppointments)));
    }



                                //Week
    @FXML
    void viewWeekOA(ActionEvent event) {
        if (!weekTableView.getSelectionModel().isEmpty())
            setLabels(weekTableView.getSelectionModel().getSelectedItem());
    }
    @FXML
    void editWeekOA(ActionEvent event) throws IOException {
        if (!weekTableView.getSelectionModel().isEmpty()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/AppointmentInfo.fxml"));
            loader.load();

            AppointmentInfo info = loader.getController();
            info.sendAppointment(weekTableView.getSelectionModel().getSelectedItem(), customers, username);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    @FXML
    void deleteWeekOA(ActionEvent event) throws SQLException {
        if (delete.showAndWait().get() == ButtonType.OK && !weekTableView.getSelectionModel().isEmpty())
            Query.deleteAppointment(weekTableView.getSelectionModel().getSelectedItem().getAppointmentId());
        numApptWeek.setText(String.valueOf(refreshSchedule(rightNow.plus(1, ChronoUnit.WEEKS).toString(), weekAppointments)));
        numApptMonth.setText(String.valueOf(refreshSchedule(rightNow.plus(1, ChronoUnit.MONTHS).toString(), monthAppointments)));

    }



                                //Month
    @FXML
    void viewMonthOA(ActionEvent event) {
        if (!monthTableView.getSelectionModel().isEmpty())
            setLabels(monthTableView.getSelectionModel().getSelectedItem());

    }
    @FXML
    void editMonthOA(ActionEvent event) throws IOException {
        if (!monthTableView.getSelectionModel().isEmpty()) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/AppointmentInfo.fxml"));
            loader.load();

            AppointmentInfo info = loader.getController();
            info.sendAppointment(monthTableView.getSelectionModel().getSelectedItem(), customers, username);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    @FXML
    void deleteMonthOA(ActionEvent event) throws SQLException {
        if (delete.showAndWait().get() == ButtonType.OK && !monthTableView.getSelectionModel().isEmpty())
            Query.deleteAppointment(monthTableView.getSelectionModel().getSelectedItem().getAppointmentId());
        numApptMonth.setText(String.valueOf(refreshSchedule(rightNow.plus(1, ChronoUnit.MONTHS).toString(), monthAppointments)));
    }










    /* #######################################################
                      Refreshers and Populate
     ####################################################### */
        private int refreshSchedule(String until, ObservableList<Appointment> schedule) throws SQLException {
            //Searches from this morning until the specified time.
            schedule.clear();
            Statement statement = Query.select("appointment", "*", "(start between '"
                    + rightNow.toString() + " 00:00:00' and '" + until + " 23:59:59') ");


            ResultSet rs = statement.getResultSet();

            while (rs.next()) {     //goes through each of the items from the result set

                int appointmentId = rs.getInt("appointmentId");
                int userId = rs.getInt("userId");
                String title = rs.getString("title");
                String type = rs.getString("type");
                String located = rs.getString("location");
                Timestamp start = rs.getTimestamp("start");
                Timestamp end = rs.getTimestamp("end");

                //Finds customerName rather than customerID
                int customerId = rs.getInt("customerId");
                ResultSet customerNameRS = Query.select("customer "," customerName ", " customerId = " + customerId).getResultSet();
                customerNameRS.next();
                String customerName = customerNameRS.getString("customerName");

                String contact = rs.getString("contact");
                Timestamp createDate = rs.getTimestamp("createDate");
                String createdBy = rs.getString("createdBy");
                Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
                String updatedBy = rs.getString("lastUpdateBy");
                String url = rs.getString("url");
                String description = rs.getString("description");


                //Times are grabbed from Database in UTC. Times are converted to local time in Appointment class.
                Appointment appt = new Appointment(appointmentId, customerId, userId, title, type, located, start, end, customerName, contact, createDate, createdBy, lastUpdate, updatedBy, url, description);
                schedule.add(appt);
            }//end while loop
            return schedule.size();
        }//refreshed schedule



    private void refreshCustomers() throws SQLException{
            customers.clear();
            ResultSet rs = Query.select("customer", "*").getResultSet();

        while (rs.next()){
            ResultSet customerRS = Query.customer(rs.getInt("customerId"));
            customerRS.next();
            int customerId = customerRS.getInt("customerId");
            String name = customerRS.getString("customerName");
            String address = customerRS.getString("address");
            String address2 = customerRS.getString("address2");
            int active = customerRS.getInt("active");
            String postal = customerRS.getString("postalCode");
            String phone = customerRS.getString("phone");
            String city = customerRS.getString("city");

            int addressId = customerRS.getInt("addressId");
            int cityId = customerRS.getInt("cityId");
            int countryId = customerRS.getInt("countryId");
            String country = Query.country(countryId);

            Timestamp createDate = customerRS.getTimestamp("createDate");
            String createdBy = customerRS.getString("createdBy");
            Timestamp lastUpdate = customerRS.getTimestamp("lastUpdate");
            String lastUpdateBy = customerRS.getString("lastUpdateBy");

            Customer customer = new Customer(customerId, name, address, address2, active, createDate, createdBy, lastUpdate, lastUpdateBy, phone, city, country, postal, cityId, countryId, addressId);
            customers.add(customer);
        }//end while loop
    }//enjoy the refreshed customers


    private void populateCustomers () throws SQLException{
            refreshCustomers();
            customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
            customerCity.setCellValueFactory(new PropertyValueFactory<>("city"));
            customerCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
            customerPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
            customerTableView.setItems(customers);
        }

    private void populateTodayTable () throws SQLException {

        numApptToday.setText(String.valueOf(refreshSchedule(rightNow.toString(), todayAppointments)));
        dateToday.setCellValueFactory((new PropertyValueFactory<>("date")));
        startToday.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endToday.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        titleToday.setCellValueFactory(new PropertyValueFactory<>("title"));
        customerToday.setCellValueFactory(new PropertyValueFactory<>("customer"));
        todayTableView.setItems(todayAppointments);
    }

    private void populateWeekTable () throws SQLException {

        numApptWeek.setText(String.valueOf(refreshSchedule(rightNow.plus(1, ChronoUnit.WEEKS).toString(), weekAppointments)));
        dateWeek.setCellValueFactory((new PropertyValueFactory<>("date")));
        startWeek.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endWeek.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        titleWeek.setCellValueFactory(new PropertyValueFactory<>("title"));
        customerWeek.setCellValueFactory(new PropertyValueFactory<>("customer"));
        weekTableView.setItems(weekAppointments);
    }

    private void populateMonthTable () throws SQLException {

        numApptMonth.setText(String.valueOf(refreshSchedule(rightNow.plus(1, ChronoUnit.MONTHS).toString(), monthAppointments)));
        dateMonth.setCellValueFactory((new PropertyValueFactory<>("date")));
        startMonth.setCellValueFactory((new PropertyValueFactory<>("startTime")));
        endMonth.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        titleMonth.setCellValueFactory(new PropertyValueFactory<>("title"));
        customerMonth.setCellValueFactory(new PropertyValueFactory<>("customer"));
        monthTableView.setItems(monthAppointments);
    }



        private void setLabels (Appointment selected){

            title.setText(String.valueOf(selected.getTitle()));
            type.setText(String.valueOf(selected.getType()));
            located.setText(String.valueOf(selected.getLocated()));

            start.setText(selected.getStartTime());
            end.setText(selected.getEndTime());
            date.setText(String.valueOf(selected.getStart().toLocalDateTime().toLocalDate()));
            customer.setText(String.valueOf(selected.getCustomer()));


            //Uses the selected appointments customerId to find the customer from the customers list, and then put the customer's name into the hyperlink
            //Gives a quick shortcut to view a customer's information and change it if needed :)
            customers.forEach(customer1 -> {if(customer1.getCustomerId() == selected.getCustomerId())  {selectedCustomer=customer1; customer.setText(selectedCustomer.getName());}});

            contact.setText(String.valueOf(selected.getContact()));
            createDate.setText(String.valueOf(selected.getCreateDate()));
            createdBy.setText(String.valueOf(selected.getCreatedBy()));
            update.setText(String.valueOf(selected.getLastUpdate()));
            updateBy.setText(String.valueOf(selected.getUpdatedBy()));
            url.setText(String.valueOf(selected.getUrl()));
            description.setText(String.valueOf(selected.getDescription()));
        }



    /*
        private Customer findCustomer(){
            for(Customer search : customers){
                if (search.getName().equals(customerNameHyperlink))
                    return search;
            }
            return null;
        }
    */// this is obsolete because I can use lambda expressions. I'm a big programmer now!

}
