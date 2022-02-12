
/*
C195 - Started March 2020
Brandon Blackburn
Last updated: 09/25/2020

Requirement Criteria in this class:


 */

package utils;


import java.sql.*;

public class Query {
        private static Connection conn = ConnectDB.getConnection();
        private static Timestamp timestamp = new Timestamp(System.currentTimeMillis());


        /*########################################################
                                 Methods
         #########################################################*/
        //Sends one of the query methods below to the Database conn, returns the ResultSet to the query method to return.
        public static Statement sendQuery(StringBuilder query) throws SQLException {
                Statement statement = conn.createStatement();
                statement.execute(query.toString());
                return statement;
        }





        //SELECT queries, 3 arguments for a WHERE clause. The WHERE could have been made as a List<> to include multiple conditions,
        //    but for this application it is not needed and/or could just use multiple queries.

        public static Statement select(String table, String search) throws SQLException {
                StringBuilder statement = new StringBuilder("SELECT " + search + " FROM " + table);
                return sendQuery(statement);
        }

        public static Statement select(String table, String search, String where) throws SQLException {
                StringBuilder statement = new StringBuilder("SELECT " + search + " FROM " + table + " WHERE " + where);
                return sendQuery(statement);
        }




        //DELETE query. Since we are not allowed to edit the database, I made it impossible to delete an entire table :)

        public static Statement deleteAppointment(int appointmentId) throws SQLException {
                StringBuilder statement = new StringBuilder("DELETE FROM appointment WHERE appointmentId =  " + appointmentId);
                return sendQuery(statement);
        }

        public static Statement deleteCustomer(int customerId) throws SQLException {
                StringBuilder statement = new StringBuilder("DELETE FROM customer WHERE customerId =  " + customerId);
                return sendQuery(statement);
        }


        /* #######################################################
                               Insert
        ####################################################### */

        public static boolean insertCountry(String country, String username) throws SQLException {
                PreparedStatement ps = conn.prepareStatement ("INSERT INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                        "VALUES (?, ?, ?, ?, ?)");

                ps.setString(1, country);
                ps.setTimestamp(2, timestamp);
                ps.setString(3, username);
                ps.setTimestamp(4, timestamp);
                ps.setString(5, username);

                return ps.execute();
        }


        public static boolean insertCity(int countryId, String city, String username) throws SQLException {
                PreparedStatement ps = conn.prepareStatement ("INSERT INTO city (countryId, city, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                        "VALUES (?, ?, ?, ?, ?, ?)");

                ps.setInt(1, countryId);
                ps.setString(2, city);
                ps.setTimestamp(3, timestamp);
                ps.setString(4, username);
                ps.setTimestamp(5, timestamp);
                ps.setString(6, username);

                return ps.execute();
        }


        public static boolean insertAddress(int cityId, String address,  String address2, String postalCode, String phone, String username) throws SQLException {
                PreparedStatement ps = conn.prepareStatement ("INSERT INTO address (cityId, address, address2, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");


                ps.setInt(1, cityId);
                ps.setString(2,address);
                ps.setString(3, address2);
                ps.setString(4, postalCode);
                ps.setString(5, phone);
                ps.setTimestamp(6, timestamp);
                ps.setString(7, username);
                ps.setTimestamp(8, timestamp);
                ps.setString(9, username);

                return ps.execute();
        }


        public static boolean insertCustomer(int addressId, String customerName, int active, String username) throws SQLException {
                PreparedStatement ps = conn.prepareStatement ("INSERT INTO customer (addressId, customerName, active, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)");

                ps.setInt(1, addressId);
                ps.setString(2, customerName);
                ps.setInt(3, active);
                ps.setTimestamp(4, timestamp);
                ps.setString(5, username);
                ps.setTimestamp(6, timestamp);
                ps.setString(7, username);


                return ps.execute();
        }


        public static boolean insertAppointment(int customerId, String title, String description, String located, String contact, String type, String url, Timestamp start, Timestamp end, String username) throws SQLException {
                PreparedStatement ps = conn.prepareStatement ("INSERT INTO appointment (customerId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy, userId) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                ps.setInt(1, customerId);
                ps.setString(2, title);
                ps.setString(3,description);
                ps.setString(4,located);
                ps.setString(5, contact);
                ps.setString(6, type);
                ps.setString(7, url);
                ps.setTimestamp(8,start);
                ps.setTimestamp(9, end);
                ps.setTimestamp(10, timestamp);
                ps.setString(11, username);
                ps.setTimestamp(12, timestamp);
                ps.setString(13, username);
                ps.setInt(14, getUserId(username));


                return ps.execute();
        }


        public static boolean insertUser(String password, int active, String username) throws SQLException {
                PreparedStatement ps = conn.prepareStatement ("INSERT INTO user (userName, password, active, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                        "VALUES (?, ?, ?,  ?, ?, ?, ?)");

                ps.setString(1, username);
                ps.setString(2, password);
                ps.setInt(3, active);
                ps.setTimestamp(4, timestamp);
                ps.setString(5, username);
                ps.setTimestamp(6, timestamp);
                ps.setString(7, username);


                return ps.execute();
        }







        //Update statements


        public static boolean updateCountry(String country, String username, int countryId) throws SQLException {
                PreparedStatement ps = conn.prepareStatement ("UPDATE country SET country = ?, lastUpdate = ?, lastUpdateBy = ? WHERE country.countryId = ?");

                ps.setString(1, country);
                ps.setTimestamp(2, timestamp);
                ps.setString(3, username);
                ps.setInt(4, countryId);


                return ps.execute();
        }
        public static boolean updateCity(int countryId, String city, String username, int cityId) throws SQLException {
                PreparedStatement ps = conn.prepareStatement ("UPDATE city SET countryId = ?, city = ?, lastUpdate = ?, lastUpdateBy = ? WHERE city.cityId = ?");

                ps.setInt(1, countryId);
                ps.setString(2, city);
                ps.setTimestamp(3, timestamp);
                ps.setString(4, username);
                ps.setInt(5, cityId);


                return ps.execute();
        }
        public static boolean updateAddress(int cityId, String address,  String address2, String postalCode, String phone, String username, int addressId) throws SQLException {
                PreparedStatement ps = conn.prepareStatement ("UPDATE address SET cityId = ?, address = ?, address2 = ?, postalCode = ?, phone = ?, lastUpdate = ?, lastUpdateBy = ? WHERE address.addressId = ?");


                ps.setInt(1, cityId);
                ps.setString(2,address);
                ps.setString(3, address2);
                ps.setString(4, postalCode);
                ps.setString(5, phone);
                ps.setTimestamp(6, timestamp);
                ps.setString(7, username);
                ps.setInt(8, addressId);


                return ps.execute();
        }







        /* #######################################################
                                Update
          ####################################################### */
        public static boolean updateCustomer(int addressId, String customerName, int active, String username, int customerId) throws SQLException {
                PreparedStatement ps = conn.prepareStatement ("UPDATE customer SET addressId = ?, customerName = ?, active = ?, lastUpdate = ?, lastUpdateBy = ? WHERE customer.customerId = ?");

                ps.setInt(1, addressId);
                ps.setString(2, customerName);
                ps.setInt(3, active);
                ps.setTimestamp(4, timestamp);
                ps.setString(5, username);
                ps.setInt(6, customerId);


                return ps.execute();
        }


        public static boolean updateAppointment(int customerId, String title, String description, String located, String contact, String type, String url, Timestamp start, Timestamp end, String username, int appointmentId ) throws SQLException {
                PreparedStatement ps = conn.prepareStatement (
                        "UPDATE appointment SET customerId = ?, userId = ?, title = ?, description = ?, location = ?, " +
                                "contact = ?, type = ?, url = ?, start = ?, end = ?, lastUpdate = ?, lastUpdateBy = ? " +
                                "WHERE appointment.appointmentId = ?");


                ps.setInt(1, customerId);
                ps.setInt(2, getUserId(username));
                ps.setString(3, title);
                ps.setString(4,description);
                ps.setString(5,located);
                ps.setString(6, contact);
                ps.setString(7, type);
                ps.setString(8, url);
                ps.setTimestamp(9, start);
                ps.setTimestamp(10, end);
                ps.setTimestamp(11, timestamp);
                ps.setString(12, username);
                ps.setInt(13, appointmentId);


                return ps.execute();
        }

        /* Made before I realized it had no use.
        public static boolean updateUser(String password, int active, String username, int userId) throws SQLException {
                PreparedStatement ps = conn.prepareStatement ("UPDATE user SET userName = ?, password = ?, active = ?, lastUpdate = ?, lastUpdateBy = ? WHERE user.userId = ? ");

                ps.setString(1, username);
                ps.setString(2, password);
                ps.setInt(3, active);
                ps.setTimestamp(4, AdjustTime.toUTCts(timestamp));
                ps.setString(5, username);
                ps.setInt(6, userId);

                return ps.execute();
        }
         */




        /* #######################################################
                                Utils
          ####################################################### */

        //finds all required information about customer
        public static ResultSet customer(int customerId) throws SQLException {
                PreparedStatement ps = conn.prepareStatement("SELECT customer.customerId, customer.customerName, customer.active, customer.createDate, customer.createdBy, customer.lastUpdate, customer.lastUpdateBy, address.addressId, address.address, address.address2, address.postalCode, address.phone, city.cityId, city.city, city.countryId " +
                        "FROM ((address " +
                        "INNER JOIN customer " +
                        "ON address.addressId = customer.addressId) " +
                        "INNER JOIN city " +
                        "ON address.cityId = city.cityId) " +
                        "WHERE customerId = " + customerId);

                ps.execute();
                ResultSet rs = ps.getResultSet();
                return rs;
        }


        public static String country(int countryId) throws SQLException{
                PreparedStatement ps = conn.prepareStatement("SELECT country FROM country WHERE countryId = " + countryId);
                ps.execute();
                ResultSet rs = ps.getResultSet();
                rs.next();
                return rs.getString("country");
        }


        private static int getUserId(String user) throws SQLException {                      //This method assumes that each userName is unique.
                int userId = Checker.existingUser(user);
                if (userId == 0) {
                        System.out.println("This username isn't in the data base, please speak with your network admin.");
                        insertUser("test", 1, user);
                        userId = Checker.existingUser(user);
                }
                return userId;

                //      This method could assign a userId or check the validity of the person accessing the database, but since we
                //              are required to use the user/pass as "test"/"test", it would be futile.
        }



        public static ResultSet groupBy (String table, String select, String groupBy) throws SQLException {
                StringBuilder statement = new StringBuilder("SELECT " + select + " FROM " + table + " GROUP BY " + groupBy);
                ResultSet rs = sendQuery(statement).getResultSet();
                return rs;
        }

}
