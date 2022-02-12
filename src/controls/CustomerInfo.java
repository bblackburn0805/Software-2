/*
C195 - Started March 2020
Brandon Blackburn
Last updated: 09/25/2020

Requirement Criteria in this class:


(Shared with AddCustomer class)
F.   Write exception controls to prevent each of the following.
You may use the same mechanism of exception control more than once, but you must incorporate at least two different mechanisms of exception control.

    •   entering nonexistent or invalid customer data
        - Customer information also cannot be left blank/invalid in the AddCustomer/CustomerInfo classes. Mechanism: Try-Catch (InvalidEntry)
        - The Checker utils class is used to check the validity of customer information. No exceptions, just If statements.

 */


package controls;

import Exceptions.InvalidEntry;
import Model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.Checker;
import utils.Query;

import java.io.IOException;
import java.sql.SQLException;

public class CustomerInfo {
    /* #######################################################
                     Class Variables & FXML
    ####################################################### */
    Stage stage;
    Parent scene;
    private String username;
    private int customerId;
    private static Alert editSuccess = new Alert(Alert.AlertType.INFORMATION, "Edited Successfully!");

    @FXML private TextField name;
    @FXML private TextField address1;
    @FXML private TextField address2;
    @FXML private TextField city;
    @FXML private TextField country;
    @FXML private TextField zip;
    @FXML private TextField phone;
    @FXML private TextField createDate;
    @FXML private TextField createdBy;
    @FXML private TextField lastUpdate;
    @FXML private TextField updatedBy;
    @FXML private CheckBox activeBox;




    /* #######################################################
                             Actions
    ####################################################### */

    @FXML void cancelOA(ActionEvent event) throws IOException{
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    @FXML void saveOA(ActionEvent event) throws SQLException {
        String nameInput = name.getText();
        String address1Input = address1.getText();
        String address2Input = address2.getText();
        String cityInput = city.getText();
        String countryInput = country.getText();
        String zipInput = zip.getText();
        String phoneInput = phone.getText();
        int active;

        int countryId = Checker.existingCountry(countryInput);
        int cityId = Checker.existingCity(cityInput, countryId);
        int addressId = Checker.existingAddress(address1Input, address2Input, cityId);

        if (activeBox.isSelected())
            active = 1;
        else
            active = 0;

        //Make sure that none of the fields are blank or exceed the allowed characters limited by the database
        try {
            if (!Checker.checkCustomer(nameInput, address1Input, address2Input, cityInput, countryInput, zipInput, phoneInput))
                throw new InvalidEntry("Please enter valid information for all fields");

                //Now checking to see if all of the inputs already exist. If not, it makes a new entry into the appropriate table and get the id from there (since it is auto incremented)
                if (countryId == 0) {
                    Query.insertCountry(countryInput, username);
                    countryId = Checker.existingCountry(countryInput);
                }
                else
                    Query.updateCountry(countryInput, username, countryId);

                if (cityId == 0) {
                    Query.insertCity(countryId, cityInput, username);
                    cityId = Checker.existingCity(cityInput, countryId);
                }
                else
                    Query.updateCity(countryId, cityInput, username, cityId);

                if (addressId == 0) {
                    Query.insertAddress(cityId, address1Input, address2Input, zipInput, phoneInput, username);
                    addressId = Checker.existingAddress(address1Input, address2Input, cityId);
                }
                else
                    Query.updateAddress(cityId, address1Input, address2Input, zipInput, phoneInput, username, addressId);

                //Now insert this customer into the customer table
                Query.updateCustomer(addressId, nameInput, active, username, customerId);

            editSuccess.setHeaderText("edit Successful!");
            editSuccess.showAndWait();

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        }catch(InvalidEntry | IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.showAndWait();
        }
    }




    public void sendCustomer(Customer customer, String user) {
        name.setText(String.valueOf(customer.getName()));
        if (customer.getActive() == 1)
            activeBox.setSelected(true);
        address1.setText(customer.getAddress());
        address2.setText(String.valueOf(customer.getAddress2()));
        city.setText(String.valueOf(customer.getCity()));
        country.setText(String.valueOf(customer.getCountry()));
        zip.setText(String.valueOf(customer.getPostal()));
        phone.setText(String.valueOf(customer.getPhone()));
        createDate.setText(String.valueOf(customer.getCreateDate()));
        createdBy.setText(String.valueOf(customer.getCreatedBy()));
        lastUpdate.setText(String.valueOf(customer.getLastUpdate()));
        updatedBy.setText(String.valueOf(customer.getLastUpdateBy()));
        username = user;
        customerId = customer.getCustomerId();
    }
}
