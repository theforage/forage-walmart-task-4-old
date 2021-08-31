package api;

import database.BadQueryParameterException;
import database.DatabaseConnector;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;

import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * Defines a web API that can be used to store and retrieve information regarding VR Headset Purchases.
 */
public class VrHeadsetTrackingAPI {
    private final DatabaseConnector databaseConnector;

    public VrHeadsetTrackingAPI() {
        // create a new component to manage the database connection
        this.databaseConnector = new DatabaseConnector();

        // create a javalin app and register endpoints
        Javalin javalinApp = Javalin.create().start(7000);
        javalinApp.post("record_purchase", this::recordPurchase);
        javalinApp.post("collect_purchase_info", this::collectPurchaseInfo);
    }

    /**
     * This method is called when the /record_purchase endpoint is hit.
     * Records a purchase with the given info to the database.
     */
    private void recordPurchase(Context ctx) {
        // collect purchase info from JSON body of request
        RecordPurchaseRequest recordPurchaseRequest = ctx.bodyValidator(RecordPurchaseRequest.class)
                .check(obj -> Pattern.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d", obj.getPurchaseDate()))
                .get();

        try {
            // attempt to record the purchase to the database
            this.databaseConnector.addPurchase(recordPurchaseRequest);
        } catch (BadQueryParameterException error) {
            // if recording the purchase fails due to a bad parameter, it is the client's fault
            throw new BadRequestResponse(error.getMessage());
        } catch (SQLException error) {
            // if recording the purchase fails due to a sql error, something is wrong with the server
            throw new InternalServerErrorResponse(error.getMessage());
        }

        // if no exception has been thrown, adding the purchase was a success - send back a corresponding response
        RecordPurchaseResponse recordPurchaseResponse = new RecordPurchaseResponse(true);
        ctx.json(recordPurchaseResponse);
    }

    /**
     * This method is called when the /collect_purchase_info endpoint is hit.
     * Queries the database for information about the given purchase.
     */
    private void collectPurchaseInfo(Context ctx) {
        // collect purchase info from JSON body of request
        CollectPurchaseInfoRequest collectPurchaseInfoRequest = ctx.bodyValidator(CollectPurchaseInfoRequest.class)
                .check(obj -> obj.getPurchaseId() > 0)
                .get();

        CollectPurchaseInfoResponse purchaseInfo;
        try {
            // collect information about the purchase from the database
            purchaseInfo = databaseConnector.collectPurchaseInfo(collectPurchaseInfoRequest);
        } catch (BadQueryParameterException error) {
            // if collecting purchase info fails due to a bad parameter, it is the client's fault
            throw new BadRequestResponse(error.getMessage());
        } catch (SQLException error) {
            // if collecting purchase info fails due to a sql error, something is wrong with the server
            throw new InternalServerErrorResponse(error.getMessage());
        }

        // return collected info to the client
        ctx.json(purchaseInfo);
    }

}
