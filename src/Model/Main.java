/*
C195 - Started March 2020
Brandon Blackburn
Last updated: 09/25/2020

    *Notes*
All times in database are assumed to be in UTC timezone.
All times displayed in the GUI are in local user time.
Times written into the user log are in local user time.



    *Requirements, and where they are satisfied.
     Explainations are included in Class comments*

A.   Create a log-in form that can determine the user’s location and translate log-in and error control messages (e.g., “The username and password did not match.”) into two languages.
    - controls/Login

B.   Provide the ability to add, update, and delete customer records in the database, including name, address, and phone number.
    - controls/MainMenu. Also in -> AddAppointment, AddCustomer, AppointmentInfo, CustomerInfo

C.   Provide the ability to add, update, and delete appointments, capturing the type of appointment and a link to the specific customer record in the database.
    - controls/MainMenu. Also in -> AddAppointment, AppointmentInfo


D.   Provide the ability to view the calendar by month and by week.
    - controls/MainMenu.


E.    Provide the ability to automatically adjust appointment times based on user time zones and daylight saving time.
    - utils/AdjustTime. Implemented in most methods.


F.   Write exception controls to prevent each of the following. You may use the same mechanism of exception control more than once, but you must incorporate at least  two different mechanisms of exception control.
    •   scheduling an appointment outside business hours
        -controls/AddAppointment, AppointmentInfo
    •   scheduling overlapping appointments
        -controls/AddAppointment, AppointmentInfo
    •   entering nonexistent or invalid customer data
        -controls/AddCustomer, CustomerInfo
    •   entering an incorrect username and password
        -controls/Login


G.  Write two or more lambda expressions to make your program more efficient, justifying the use of each lambda expression with an in-line comment.
    - controls/MainMenu, CustomerInfo, Reports.


H.   Write code to provide an alert if there is an appointment within 15 minutes of the user’s log-in.
    controls/Login


I.   Provide the ability to generate each  of the following reports:
    •   number of appointment types by month
        -controls/Report -> Types tab
    •   the schedule for each consultant
        -controls/Report -> Consultant tab
    •   one additional report of your choice
        -controls/Report -> either Customers tab or Taylor Swift tab.


J.   Provide the ability to track user activity by recording timestamps for user log-ins in a .txt file. Each new record should be appended to the log file, if the file already exists.
    - controls/Login


K. Demonstrate professional communication in the content and presentation of your submission.
    - Can be found in every Class, every Method, even every lambda expression.
 */





package Model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application {
    private static String username;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View/Login.fxml"));
        primaryStage.setTitle("Scheduler 3000");
        primaryStage.setScene(new Scene(root, 467, 288));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);

    }

    public static void setUsername(String user){username = user;}
    public static String getUsername(){return username;}

}
