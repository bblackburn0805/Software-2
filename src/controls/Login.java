/*
C195 - Started March 2020
Brandon Blackburn
Last updated: 09/25/2020

Requirement Criteria in this class:

A.   Create a log-in form that can determine the user’s location and translate log-in and error control messages (e.g., “The username and password did not match.”) into two languages.
    -Determines and translate language via Resource Bundle. Languages are French and Spanish, aside from English.


F.   Write exception controls to prevent each of the following.
You may use the same mechanism of exception control more than once, but you must incorporate at least two different mechanisms of exception control.
    •   entering an incorrect username and password
        - Prevented in actionLogin method. Mechanism: Try-Catch (IncorrectUsernamePassword).
          I wrote the methods as if the log-in process checked a database to verify username/password. But the required test/test were hardcoded.


H.   Write code to provide an alert if there is an appointment within 15 minutes of the user’s log-in.
    - Provided in "upcomingAppointment" method.


J.   Provide the ability to track user activity by recording timestamps for user log-ins in a .txt file. Each new record should be appended to the log file, if the file already exists.
    - Provided in "updateUserLog" method. Timestamps are provided in the users own timezone, not UTC.

 */


package controls;

        import Exceptions.IncorrectUsernamePassword;
        import Model.Main;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.fxml.Initializable;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.control.*;
        import javafx.stage.Stage;
        import utils.AdjustTime;
        import utils.ConnectDB;
        import utils.Query;

        import java.io.*;
        import java.net.URL;

        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.sql.Statement;
        import java.sql.Timestamp;
        import java.text.SimpleDateFormat;
        import java.time.LocalDateTime;
        import java.time.temporal.ChronoUnit;
        import java.util.Locale;
        import java.util.ResourceBundle;





public class Login implements Initializable {
    /* #######################################################
                     Class Variables & FXML
    ####################################################### */
    Stage stage;
    Parent scene;
    ResourceBundle rb = ResourceBundle.getBundle("Languages/Nat", Locale.getDefault());
    Alert incorrectUP = new Alert(Alert.AlertType.ERROR,"Incorrect Username and Password", ButtonType.CLOSE);
    Alert upcoming = new Alert(Alert.AlertType.INFORMATION);
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
    LocalDateTime rightNow = AdjustTime.toUTC(LocalDateTime.now());
    int loginUserId = 0;                                                                                                //UserId would be found with username/password validation, but there is no such requirement.

    @FXML private Label helloLabel;
    @FXML private Label queryLabel;
    @FXML private Label usernameLabel;
    @FXML private TextField usernameBox;
    @FXML private Label passwordLabel;
    @FXML private TextField passwordBox;
    @FXML private Button login;
    @FXML private Button cancel;





        /* #######################################################
                             Actions
        ####################################################### */
    @FXML void actionCancel(ActionEvent event) throws SQLException {
        ConnectDB.closeConnection();
        System.exit(0);
    }

    @FXML void actionLogin(ActionEvent event) {
        String userName = usernameBox.getText();
        String password = passwordBox.getText();

        try {
            Signin(userName, password);                                                                               //Sign in with username and password
            Main.setUsername(userName);
            upcomingAppt();
            updateUserLog(userName, loginUserId);
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));                               //Sends to Main Menu if both username and password match records
            stage.setScene(new Scene(scene));
            stage.show();
        }catch (IncorrectUsernamePassword | SQLException | IOException e) {
            e.printStackTrace();
            incorrectUP.showAndWait();
        }
    }






        /* #######################################################
                                Utils
         ####################################################### */
    public void Signin(String username, String password) throws IncorrectUsernamePassword, SQLException {
        if( !validateUsernamePassword(username, password))
            throw new IncorrectUsernamePassword("Incorrect Username and Password");


    }


    private void upcomingAppt() throws SQLException {                                                                   //Checks if there is an appointment within 15 minutes.
        ResultSet rs = Query.select("appointment", "title, start",
                "start between '" + rightNow +
                        "' AND '" + rightNow.plus(15, ChronoUnit.MINUTES)
                        + "' ").getResultSet();

        if (rs.next()) {
            Timestamp start = Timestamp.valueOf(rs.getString("start"));
            upcoming.setHeaderText("Upcoming appointment!");
            upcoming.setContentText(rs.getString("title") +
                    " starts at: " +
                    sdf.format(AdjustTime.toLocal(start)));
            upcoming.showAndWait();
        }
    }


    private void updateUserLog(String userName, int userId) throws IOException {
        File file = new File("src/UserActivity/" + userId + "_" + userName + ".txt");
        String timestamp = LocalDateTime.now() + "\n";
        FileWriter writer;
        if(!file.exists()){
            file.createNewFile();
            writer = new FileWriter(file);
            writer.write("UserId: " + userId + " Username: " + userName + "\n Log-in Timestamps \n\n + " + timestamp);  //If file doesn't exist, creates it and then writes files first line info.
        }
        else {
            writer = new FileWriter(file, true);
            writer.write(timestamp);
        }
        writer.close();                                                                                                 //I didn't forget ;)
    }

    public void initialize(URL url, ResourceBundle resourceBundle){

        if(Locale.getDefault().getLanguage().equals("fr")                                                               //Sets up log-in screen with either english, spanish, or french.
                || Locale.getDefault().getLanguage().equals("es")
                || Locale.getDefault().getLanguage().equals("en_US")){
            helloLabel.setText(rb.getString("hello") + "! " + rb.getString("welcome")
                    + " " + rb.getString("to") + " " + rb.getString("the") + " Scheduler 3000!" );
            queryLabel.setText(rb.getString("please") + " " + rb.getString("log-in"));
            usernameLabel.setText(rb.getString("username"));
            passwordLabel.setText(rb.getString("password"));
            login.setText(rb.getString("log-in"));
            cancel.setText(rb.getString("cancel"));
            incorrectUP.setContentText(rb.getString("incorrect") + " " + rb.getString("password"));
            incorrectUP.setTitle(rb.getString("oops"));                                                             //Sets the wrong password screen to that language as well.
            incorrectUP.setHeaderText(rb.getString("oops"));
        }
        ConnectDB.startConnection();
    }

    private boolean validateUsernamePassword(String userName, String password) throws SQLException {
        ResultSet rs = Query.select("user", "userId", "userName = '" + userName + "' AND password = '" + password + "' ").getResultSet();
        if (rs.next()){
            loginUserId = rs.getInt("userId");
            return true;
        }
        return false;
    }
}
