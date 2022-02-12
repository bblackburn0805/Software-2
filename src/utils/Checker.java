
/*
C195 - Started March 2020
Brandon Blackburn
Last updated: 09/25/2020

Requirement Criteria in this class:


 */



package utils;
import javafx.scene.control.Alert;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Checker {

    /* #######################################################
                        Customer Verification
     ####################################################### */

    public static boolean nameCheck (String name){
        if (name.length() > 45
        || name.isEmpty()) {
            sendError("Invalid customer name");
            return false;
        }
        return true;
    }

    public static boolean address1Check (String string){
        if (string.length() > 50
            || string.isEmpty()){
            sendError("Invalid address 1");
        return false;
        }
        return true;
    }

    public static boolean address2Check (String string){
        if (string.length() > 50
                || string.isEmpty()){
            sendError("Invalid address 2");
            return false;
        }
        return true;
    }

    public static boolean postalCodeCheck (String string){
        if (string.length() > 10
                || string.isEmpty()){
            sendError("Invalid postal code");
            return false;
        }
        return true;
    }

    public static boolean phoneCheck (String string){
        if (string.length() > 20
                || string.isEmpty()){
            sendError("Invalid phone number");
            return false;
        }
        return true;
    }

    public static boolean cityCheck (String string){
        if (string.length() > 50
                || string.isEmpty()){
            sendError("Invalid city");
            return false;
        }
        return true;
    }

    public static boolean countryCheck (String string){
        if (string.length() > 50
                || string.isEmpty()){
            sendError("Invalid country");
            return false;
        }
        return true;
    }

    public static boolean checkCustomer(String name, String address1, String address2, String postalCode, String phone, String city, String country){
        if (
            nameCheck(name) &&
            address1Check(address1) &&
            address2Check(address2) &&
            postalCodeCheck(postalCode) &&
            phoneCheck(phone) &&
            cityCheck(city) &&
            countryCheck(country)
        )
            return true;
        return false;
    }










    /* #######################################################
                        Customer Verification
     ####################################################### */

    public static boolean titleCheck(String string){
        if (string.length() > 255
                || string.isEmpty()){
            sendError("Invalid title");
            return false;
        }
        return true;
    }

    public static boolean urlCheck(String string){
        if (string.length() > 50
                || string.isEmpty()){
            sendError("Invalid URL");
            return false;
        }
        return true;
    }

    public static boolean checkAppointment(String title, String url ){
        if (titleCheck(title) && urlCheck(url))
            return true;
        return false;
    }




    /* #######################################################
                         New entries
     ####################################################### */


    public static int existingAddress (String address, String address2, int cityId) throws SQLException {
        ResultSet rs = Query.select("address", "*", "address = '" + address + "' " +
        "AND address2 = '" + address2 + "' " +                                                  //There can be two different apt, suites, etc. at one single address.
        "AND cityId = " + cityId).getResultSet();                                               //There can be the exact address in two different cities.

        if (rs.next())
            return rs.getInt("addressId");
        else
            return 0;
    }

    public static int existingCity (String city, int countryId) throws SQLException {
        ResultSet rs = Query.select("city", "*", "city = '" + city + "' "+
        "AND countryId = " + countryId).getResultSet();                                         //There can be cities with the same name but in different countries.

        if (rs.next())
            return rs.getInt("cityId");
        else
            return 0;
    }

    public static int existingCountry (String country) throws SQLException {
        ResultSet rs = Query.select("country", "*", "country = '" + country + "' ").getResultSet();
        if (rs.next())
            return rs.getInt("countryId");
        else
            return 0;
    }

    public static int existingUser (String user) throws SQLException {
        ResultSet rs = Query.select("user", "*", "userName = '" + user + "' ").getResultSet();
        if (rs.next())
            return rs.getInt("userId");
        else
            return 0;
    }








    private static void sendError(String string){
        Alert trigger = new Alert(Alert.AlertType.ERROR);
        trigger.setTitle("Error");
        trigger.setHeaderText(string);
        trigger.setContentText(null);

        trigger.showAndWait();
    }
}
