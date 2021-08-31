package database;

import api.CollectPurchaseInfoRequest;
import api.CollectPurchaseInfoResponse;
import api.RecordPurchaseRequest;

import java.sql.*;

/**
 * Manages a connection to a sqlite database.
 */
public class DatabaseConnector {
    private final String DATABASE_PATH = "vr_headset.db";
    private String connectionURL;

    public DatabaseConnector() {
        this.connectionURL = String.format("jdbc:sqlite:%s", this.DATABASE_PATH);
    }

    /**
     * Adds a purchase to the database.
     */
    public void addPurchase(RecordPurchaseRequest purchaseInfo) throws BadQueryParameterException, SQLException {
        try (Connection connection = DriverManager.getConnection(connectionURL)) {
            // collect vr headset id
            int vrHeadsetId;
            try {
                vrHeadsetId = this.lookupVrHeadsetId(connection, purchaseInfo.getVrHeadsetName());
            } catch (BadQueryParameterException exception) {
                // if a vr headset cannot be found using its name, the request is bad - raise the exception
                throw exception;
            }

            // collect customer id
            int customerId = 0;
            try {
                customerId = this.lookupCustomerId(connection, purchaseInfo.getCustomerEmail());
            } catch (BadQueryParameterException exception) {
                // if a customer cannot be found using their email, add them to the database and continue execution
                this.addCustomer(
                        connection,
                        purchaseInfo.getCustomerFirstName(),
                        purchaseInfo.getCustomerLastName(),
                        purchaseInfo.getCustomerEmail()
                );
            }

            // insert the purchase into the database
            String query = """
                        INSERT INTO purchase (customer_id, vr_headset_id, purchase_date)
                        VALUES (?, ?, ?);
                    """;
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, customerId);
                statement.setInt(2, vrHeadsetId);
                statement.setString(3, purchaseInfo.getPurchaseDate());
                statement.executeUpdate();
            }
        }
    }

    /**
     * Queries the ID of a VR headset using its name.
     */
    private int lookupVrHeadsetId(Connection connection, String vrHeadsetName)
            throws SQLException, BadQueryParameterException {
        String query = """
                    SELECT id
                    FROM vr_headset
                    WHERE name = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, vrHeadsetName);
            ResultSet queryResult = statement.executeQuery();

            // determine if a result was found
            boolean hasResult = queryResult.next();
            if (!hasResult) {
                // if no result was found, throw an exception
                throw new BadQueryParameterException(String.format("no vr headset with name: %s exists", vrHeadsetName));
            }

            // if a result was found, return just the id
            return queryResult.getInt(1);
        }
    }

    /**
     * Queries the ID of a customer using their email.
     */
    private int lookupCustomerId(Connection connection, String email) throws SQLException, BadQueryParameterException {
        String query = """
                    SELECT id
                    FROM customer
                    WHERE email = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet queryResult = statement.executeQuery();

            // determine if a result was found
            boolean hasResult = queryResult.next();
            if (!hasResult) {
                // if no result was found, throw an exception
                throw new BadQueryParameterException(String.format("no customer with email: %s exists", email));
            }

            // if a result was found, return just the id
            return queryResult.getInt(1);
        }
    }

    /**
     * Adds a new customer to the database.
     */
    private void addCustomer(Connection connection, String firstName, String lastName, String email)
            throws SQLException {
        String query = """
                    INSERT OR IGNORE INTO customer (first_name, last_name, email)
                    VALUES (?, ?, ?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.executeUpdate();
        }
    }

    /**
     * Collects information about a given purchase.
     */
    public CollectPurchaseInfoResponse collectPurchaseInfo(CollectPurchaseInfoRequest purchaseInfo)
            throws BadQueryParameterException, SQLException {
        // establish a database connection
        try (Connection connection = DriverManager.getConnection(connectionURL)) {
            String query = """
                        SELECT customer.first_name, customer.last_name, customer.email, purchase.purchase_date,
                            vr_headset.name, vr_headset.price, vr_headset.trial_days
                        FROM purchase
                            JOIN customer ON customer.id = purchase.customer_id
                            JOIN vr_headset ON vr_headset.id = purchase.vr_headset_id
                        WHERE purchase.id = ?
                    """;
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, purchaseInfo.getPurchaseId());
                ResultSet queryResult = statement.executeQuery();

                // determine if a result was found
                boolean hasResult = queryResult.next();
                if (!hasResult) {
                    // if no result was found, throw an exception
                    throw new BadQueryParameterException(
                            String.format("no purchase with id: %d exists", purchaseInfo.getPurchaseId())
                    );
                }

                // if a result was found, construct a returnable object
                CollectPurchaseInfoResponse collectPurchaseInfoResponse = new CollectPurchaseInfoResponse(
                        queryResult.getString(1),
                        queryResult.getString(2),
                        queryResult.getString(3),
                        queryResult.getString(4),
                        queryResult.getString(5),
                        queryResult.getString(6),
                        queryResult.getInt(7)
                );
                return collectPurchaseInfoResponse;
            }
        }
    }
}
